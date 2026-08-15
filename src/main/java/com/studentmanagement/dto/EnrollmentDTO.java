package com.studentmanagement.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.studentmanagement.entities.Status;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDTO {
    private Long idEnrollment;

    @NotNull(message = "Enrollment date is required")
    private LocalDate enrollmentDate;

    @DecimalMin(value = "0.0", message = "Grade must be at least 0")
    @DecimalMax(value = "20.0", message = "Grade must be at most 20")
    private Double grade;

    @NotNull(message = "Status is required")
    private Status status;

    @NotNull(message = "Student id is required")
    private Long studentId;

    private String studentFullName;

    @NotNull(message = "Course id is required")
    private Long courseId;

    private String courseName;
}
