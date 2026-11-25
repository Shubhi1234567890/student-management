package com.management.student_management;

import com.management.student_management.entity.Student;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StudentValidationTests {

    @Test
    void acceptsInternationalNamesAndValidEmailAddresses() {
        Student student = createStudent("Zoë O'Connor", "zoe@example.org");

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            assertThat(factory.getValidator().validate(student)).isEmpty();
        }
    }

    @Test
    void rejectsMalformedNamesAndEmailAddresses() {
        Student student = createStudent("123 Rivera", "not-an-email");

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Set<String> invalidFields = factory.getValidator().validate(student).stream()
                    .map(violation -> violation.getPropertyPath().toString())
                    .collect(Collectors.toSet());

            assertThat(invalidFields).contains("name", "email");
        }
    }

    @Test
    void rejectsBlankRequiredFields() {
        Student student = createStudent(" ", " ");
        student.setCourse(" ");

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Set<String> invalidFields = factory.getValidator().validate(student).stream()
                    .map(violation -> violation.getPropertyPath().toString())
                    .collect(Collectors.toSet());

            assertThat(invalidFields).contains("name", "email", "course");
        }
    }

    @Test
    void rejectsNamesAndEmailsThatExceedTheirMaximumLengths() {
        Student student = createStudent(
                "A".repeat(101),
                "a".repeat(245) + "@example.com"
        );

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Set<String> invalidFields = factory.getValidator().validate(student).stream()
                    .map(violation -> violation.getPropertyPath().toString())
                    .collect(Collectors.toSet());

            assertThat(invalidFields).contains("name", "email");
        }
    }

    private Student createStudent(String name, String email) {
        Student student = new Student();
        student.setName(name);
        student.setEmail(email);
        student.setCourse("Java");
        return student;
    }
}
