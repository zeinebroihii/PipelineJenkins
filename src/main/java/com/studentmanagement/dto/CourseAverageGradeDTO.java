package com.studentmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseAverageGradeDTO {
    private Long courseId;
    private String courseName;
    private Double averageGrade;
    private long gradedEnrollmentCount;
}
