package com.management.student_management.controller;

import com.management.student_management.entity.Student;
import com.management.student_management.service.StudentService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentControllerTests {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    void showFormAddsStudentsAndCountToModel() {
        List<Student> students = List.of(
                createStudent(1L, "Ava Stone", "ava@example.com", "Java"),
                createStudent(2L, "Noah Reed", "noah@example.com", "Spring")
        );
        when(studentService.getAllStudents()).thenReturn(students);
        ExtendedModelMap model = new ExtendedModelMap();

        String view = studentController.showForm(model);

        assertThat(view).isEqualTo("students");
        assertThat(model.get("student")).isInstanceOf(Student.class);
        assertThat(model.get("students")).isEqualTo(students);
        assertThat(model.get("studentCount")).isEqualTo(2);
        verify(studentService).getAllStudents();
    }

    @Test
    void showFormSetsCountToZeroWhenThereAreNoStudents() {
        when(studentService.getAllStudents()).thenReturn(List.of());
        ExtendedModelMap model = new ExtendedModelMap();

        studentController.showForm(model);

        assertThat(model.get("students")).isEqualTo(List.of());
        assertThat(model.get("studentCount")).isEqualTo(0);
    }

    @Test
    void savesValidStudentAndRedirects() {
        Student student = createStudent(null, "Ava Stone", "ava@example.com", "Java");
        BindingResult bindingResult = new BeanPropertyBindingResult(student, "student");

        String view = studentController.saveStudent(
                student, bindingResult, new ExtendedModelMap());

        assertThat(view).isEqualTo("redirect:/");
        verify(studentService).saveStudent(student);
    }

    @Test
    void returnsFormWithoutSavingWhenStudentHasValidationErrors() {
        Student student = createStudent(null, "", "invalid-email", "");
        BindingResult bindingResult = new BeanPropertyBindingResult(student, "student");
        bindingResult.rejectValue("name", "NotBlank", "Name is required");
        List<Student> existingStudents = List.of(
                createStudent(1L, "Noah Reed", "noah@example.com", "Spring")
        );
        when(studentService.getAllStudents()).thenReturn(existingStudents);
        ExtendedModelMap model = new ExtendedModelMap();

        String view = studentController.saveStudent(student, bindingResult, model);

        assertThat(view).isEqualTo("students");
        assertThat(model.get("students")).isEqualTo(existingStudents);
        assertThat(model.get("studentCount")).isEqualTo(1);
        verify(studentService, never()).saveStudent(student);
        verify(studentService).getAllStudents();
    }

    @Test
    void deletesStudentAndRedirects() {
        String view = studentController.deleteStudent(7L);

        assertThat(view).isEqualTo("redirect:/");
        verify(studentService).deleteStudent(7L);
    }

    private Student createStudent(Long id, String name, String email, String course) {
        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setEmail(email);
        student.setCourse(course);
        return student;
    }
}
