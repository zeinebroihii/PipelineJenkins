package com.studentmanagement.services;

import com.studentmanagement.dto.CourseAverageGradeDTO;
import com.studentmanagement.dto.GradeDistributionDTO;

import java.util.List;

public interface IReportService {
    List<CourseAverageGradeDTO> averageGradePerCourse();
    List<GradeDistributionDTO> gradeDistribution();
}
