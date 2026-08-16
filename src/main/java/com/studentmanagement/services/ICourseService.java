package com.studentmanagement.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.studentmanagement.dto.CourseDTO;

public interface ICourseService {
    Page<CourseDTO> getAllCourses(String search, Pageable pageable);
    CourseDTO getCourseById(Long id);
    CourseDTO createCourse(CourseDTO course);
    CourseDTO updateCourse(CourseDTO course);
    void deleteCourse(Long id);
}
