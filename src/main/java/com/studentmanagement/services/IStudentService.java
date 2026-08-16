package com.studentmanagement.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.studentmanagement.dto.StudentDTO;

public interface IStudentService {
    Page<StudentDTO> getAllStudents(String search, Pageable pageable);
    StudentDTO getStudentById(Long id);
    StudentDTO createStudent(StudentDTO student);
    StudentDTO updateStudent(StudentDTO student);
    void deleteStudent(Long id);
}
