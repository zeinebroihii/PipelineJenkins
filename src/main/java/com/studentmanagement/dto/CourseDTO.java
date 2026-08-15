package com.studentmanagement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private Long idCourse;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Code is required")
    private String code;

    @Min(value = 1, message = "Credit must be at least 1")
    private int credit;

    private String description;

    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;

    private long enrolledCount;
}
