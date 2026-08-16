package com.studentmanagement.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import com.studentmanagement.dto.EnrollmentDTO;
import com.studentmanagement.services.IEnrollment;

@RestController
@RequestMapping("/Enrollment")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class EnrollmentController {
    private final IEnrollment enrollmentService;

    @GetMapping("/getAllEnrollment")
    public Page<EnrollmentDTO> getAllEnrollment(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long courseId,
            @PageableDefault(size = 10) Pageable pageable) {
        return enrollmentService.getAllEnrollments(studentId, courseId, pageable);
    }

    @GetMapping("/getEnrollment/{id}")
    public EnrollmentDTO getEnrollment(@PathVariable Long id) { return enrollmentService.getEnrollmentById(id); }

    @PostMapping("/createEnrollment")
    public EnrollmentDTO createEnrollment(@Valid @RequestBody EnrollmentDTO enrollment) { return enrollmentService.createEnrollment(enrollment); }

    @PutMapping("/updateEnrollment")
    public EnrollmentDTO updateEnrollment(@Valid @RequestBody EnrollmentDTO enrollment) {
        return enrollmentService.updateEnrollment(enrollment);
    }

    @DeleteMapping("/deleteEnrollment/{id}")
    public void deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
    }
}
