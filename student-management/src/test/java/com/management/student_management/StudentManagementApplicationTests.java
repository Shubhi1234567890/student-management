package com.management.student_management;

import com.management.student_management.entity.Student;
import com.management.student_management.service.StudentService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
class StudentManagementApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockitoBean
    private StudentService studentService;

    @Test
    void contextLoads() {
    }

    @Test
    void rendersStudentEntityDetailsInDirectory() throws Exception {
        Student student = new Student();
        student.setId(42L);
        student.setName("Jamie Chen");
        student.setEmail("jamie@example.com");
        student.setCourse("Spring Boot");
        when(studentService.getAllStudents()).thenReturn(List.of(student));

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MvcResult result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();
        String page = result.getResponse().getContentAsString();

        assertThat(page)
                .contains("Jamie Chen")
                .contains("Student #42")
                .contains("jamie@example.com")
                .contains("Spring Boot");
    }

}
