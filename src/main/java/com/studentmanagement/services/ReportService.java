package com.studentmanagement.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.studentmanagement.dto.CourseAverageGradeDTO;
import com.studentmanagement.dto.GradeDistributionDTO;
import com.studentmanagement.entities.Course;
import com.studentmanagement.entities.Enrollment;
import com.studentmanagement.repositories.CourseRepository;
import com.studentmanagement.repositories.EnrollmentRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportService implements IReportService {
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    public List<CourseAverageGradeDTO> averageGradePerCourse() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream().map(course -> {
            List<Double> grades = enrollmentRepository.findByCourse_IdCourse(course.getIdCourse()).stream()
                    .map(Enrollment::getGrade)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            CourseAverageGradeDTO dto = new CourseAverageGradeDTO();
            dto.setCourseId(course.getIdCourse());
            dto.setCourseName(course.getName());
            dto.setGradedEnrollmentCount(grades.size());
            grades.stream().mapToDouble(Double::doubleValue).average()
                    .ifPresent(dto::setAverageGrade);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<GradeDistributionDTO> gradeDistribution() {
        List<Double> grades = enrollmentRepository.findAll().stream()
                .map(Enrollment::getGrade)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return List.of(
                bucket(grades, "0-5", 0, 5),
                bucket(grades, "5-10", 5, 10),
                bucket(grades, "10-15", 10, 15),
                bucket(grades, "15-20", 15, 20.0001)
        );
    }

    private GradeDistributionDTO bucket(List<Double> grades, String label, double lowInclusive, double highExclusive) {
        long count = grades.stream().filter(g -> g >= lowInclusive && g < highExclusive).count();
        return new GradeDistributionDTO(label, count);
    }
}
