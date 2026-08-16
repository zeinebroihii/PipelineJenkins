package com.studentmanagement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentmanagement.dto.CourseDTO;
import com.studentmanagement.dto.DepartmentDTO;
import com.studentmanagement.dto.EnrollmentDTO;
import com.studentmanagement.dto.StudentDTO;
import com.studentmanagement.entities.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createEnrollment_whenCourseAtCapacity_returns400() throws Exception {
        Long departmentId = createDepartment("Math", "Building B");
        Long courseId = createCourse("Linear Algebra", "MATH201", 1);
        Long student1Id = createStudent("Alice", "One", "alice.one@example.com", departmentId);
        Long student2Id = createStudent("Bob", "Two", "bob.two@example.com", departmentId);

        createEnrollment(student1Id, courseId)
                .andExpect(status().isOk());

        createEnrollment(student2Id, courseId)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Course \"Linear Algebra\" is at full capacity"));
    }

    @Test
    void createEnrollment_whenDuplicateForSameStudentAndCourse_returns400() throws Exception {
        Long departmentId = createDepartment("Physics", "Building C");
        Long courseId = createCourse("Mechanics", "PHYS101", null);
        Long studentId = createStudent("Carol", "Three", "carol.three@example.com", departmentId);

        createEnrollment(studentId, courseId)
                .andExpect(status().isOk());

        createEnrollment(studentId, courseId)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Student is already enrolled in this course"));
    }

    @Test
    void createEnrollment_whenCourseDoesNotExist_returns404() throws Exception {
        Long departmentId = createDepartment("Chemistry", "Building D");
        Long studentId = createStudent("Dave", "Four", "dave.four@example.com", departmentId);

        createEnrollment(studentId, 999999L)
                .andExpect(status().isNotFound());
    }

    private org.springframework.test.web.servlet.ResultActions createEnrollment(Long studentId, Long courseId) throws Exception {
        EnrollmentDTO enrollment = new EnrollmentDTO();
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus(Status.ACTIVE);
        enrollment.setStudentId(studentId);
        enrollment.setCourseId(courseId);

        return mockMvc.perform(post("/Enrollment/createEnrollment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(enrollment)));
    }

    private Long createDepartment(String name, String location) throws Exception {
        DepartmentDTO department = new DepartmentDTO();
        department.setName(name);
        department.setLocation(location);

        String response = mockMvc.perform(post("/Department/createDepartment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(department)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, DepartmentDTO.class).getIdDepartment();
    }

    private Long createCourse(String name, String code, Integer capacity) throws Exception {
        CourseDTO course = new CourseDTO();
        course.setName(name);
        course.setCode(code);
        course.setCredit(3);
        course.setCapacity(capacity);

        String response = mockMvc.perform(post("/courses/createCourse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, CourseDTO.class).getIdCourse();
    }

    private Long createStudent(String first, String last, String email, Long departmentId) throws Exception {
        StudentDTO student = new StudentDTO();
        student.setFirstName(first);
        student.setLastName(last);
        student.setEmail(email);
        student.setPhone("555-0000");
        student.setDateOfBirth(LocalDate.of(2000, 1, 1));
        student.setDepartmentId(departmentId);

        String response = mockMvc.perform(post("/students/createStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, StudentDTO.class).getIdStudent();
    }
}
