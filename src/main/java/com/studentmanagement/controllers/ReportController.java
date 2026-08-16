package com.studentmanagement.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.studentmanagement.dto.CourseAverageGradeDTO;
import com.studentmanagement.dto.GradeDistributionDTO;
import com.studentmanagement.services.IReportService;

import java.util.List;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class ReportController {
    private final IReportService reportService;

    @GetMapping("/average-grade-per-course")
    public List<CourseAverageGradeDTO> averageGradePerCourse() { return reportService.averageGradePerCourse(); }

    @GetMapping("/grade-distribution")
    public List<GradeDistributionDTO> gradeDistribution() { return reportService.gradeDistribution(); }
}
