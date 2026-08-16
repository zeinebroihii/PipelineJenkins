package com.studentmanagement.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import com.studentmanagement.dto.CourseDTO;
import com.studentmanagement.services.ICourseService;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class CourseController {
    private final ICourseService courseService;

    @GetMapping("/getAllCourses")
    public Page<CourseDTO> getAllCourses(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 10) Pageable pageable) {
        return courseService.getAllCourses(search, pageable);
    }

    @GetMapping("/getCourse/{id}")
    public CourseDTO getCourse(@PathVariable Long id) { return courseService.getCourseById(id); }

    @PostMapping("/createCourse")
    public CourseDTO createCourse(@Valid @RequestBody CourseDTO course) { return courseService.createCourse(course); }

    @PutMapping("/updateCourse")
    public CourseDTO updateCourse(@Valid @RequestBody CourseDTO course) { return courseService.updateCourse(course); }

    @DeleteMapping("/deleteCourse/{id}")
    public void deleteCourse(@PathVariable Long id) { courseService.deleteCourse(id); }
}
