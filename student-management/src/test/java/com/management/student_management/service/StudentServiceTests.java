package com.management.student_management.service;

import com.management.student_management.entity.Student;
import com.management.student_management.repository.StudentRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTests {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void savesStudent() {
        Student student = createStudent(1L, "Rahul", "rahul@example.com", "Java");
        when(studentRepository.save(student)).thenReturn(student);

        Student savedStudent = studentService.saveStudent(student);

        assertThat(savedStudent).isSameAs(student);
        verify(studentRepository).save(student);
    }

    @Test
    void returnsAllStudents() {
        List<Student> students = List.of(
                createStudent(1L, "Rahul", "rahul@example.com", "Java"),
                createStudent(2L, "Priya", "priya@example.com", "Spring Boot")
        );
        when(studentRepository.findAll()).thenReturn(students);

        List<Student> result = studentService.getAllStudents();

        assertThat(result).containsExactlyElementsOf(students);
        verify(studentRepository).findAll();
    }

    @Test
    void returnsEmptyListWhenNoStudentsExist() {
        when(studentRepository.findAll()).thenReturn(List.of());

        List<Student> result = studentService.getAllStudents();

        assertThat(result).isEmpty();
        verify(studentRepository).findAll();
    }

    @Test
    void deletesStudentById() {
        studentService.deleteStudent(1L);

        verify(studentRepository).deleteById(1L);
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
