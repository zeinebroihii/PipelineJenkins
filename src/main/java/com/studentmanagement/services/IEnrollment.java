package com.studentmanagement.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.studentmanagement.dto.EnrollmentDTO;

public interface IEnrollment {
    Page<EnrollmentDTO> getAllEnrollments(Long studentId, Long courseId, Pageable pageable);
    EnrollmentDTO getEnrollmentById(Long idEnrollment);
    EnrollmentDTO createEnrollment(EnrollmentDTO enrollment);
    EnrollmentDTO updateEnrollment(EnrollmentDTO enrollment);
    void deleteEnrollment(Long idEnrollment);
}
