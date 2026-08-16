package com.studentmanagement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentmanagement.dto.DepartmentDTO;
import com.studentmanagement.dto.StudentDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createStudent_withInvalidBody_returns400WithFieldErrors() throws Exception {
        StudentDTO invalid = new StudentDTO();

        mockMvc.perform(post("/students/createStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    void getStudent_whenNotFound_returns404() throws Exception {
        mockMvc.perform(get("/students/getStudent/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void createAndFetchStudent_roundTrips() throws Exception {
        Long departmentId = createDepartment("Computer Science", "Building A");

        StudentDTO student = new StudentDTO();
        student.setFirstName("Alice");
        student.setLastName("Doe");
        student.setEmail("alice@example.com");
        student.setPhone("555-2000");
        student.setDateOfBirth(LocalDate.of(2000, 1, 15));
        student.setDepartmentId(departmentId);

        mockMvc.perform(post("/students/createStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Alice"))
                .andExpect(jsonPath("$.departmentName").value("Computer Science"))
                .andExpect(jsonPath("$.gpa").doesNotExist());
    }

    @Test
    void deleteStudent_whenNotFound_returns404() throws Exception {
        mockMvc.perform(delete("/students/deleteStudent/999999"))
                .andExpect(status().isNotFound());
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
}
