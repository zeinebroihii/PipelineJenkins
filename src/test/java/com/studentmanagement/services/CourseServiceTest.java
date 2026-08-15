package com.studentmanagement.services;

import com.studentmanagement.dto.CourseDTO;
import com.studentmanagement.entities.Course;
import com.studentmanagement.exceptions.ResourceNotFoundException;
import com.studentmanagement.repositories.CourseRepository;
import com.studentmanagement.repositories.EnrollmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private CourseService courseService;

    @Test
    void getCourseById_whenNotFound_throwsResourceNotFoundException() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> courseService.getCourseById(99L));
    }

    @Test
    void getCourseById_reportsCapacityAndEnrolledCount() {
        Course course = new Course();
        course.setIdCourse(1L);
        course.setName("Algorithms");
        course.setCapacity(30);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.countByCourse_IdCourse(1L)).thenReturn(5L);

        CourseDTO result = courseService.getCourseById(1L);

        assertThat(result.getCapacity()).isEqualTo(30);
        assertThat(result.getEnrolledCount()).isEqualTo(5L);
    }

    @Test
    void getCourseById_whenCapacityUnset_isNull() {
        Course course = new Course();
        course.setIdCourse(1L);
        course.setName("Open Elective");
        course.setCapacity(null);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.countByCourse_IdCourse(1L)).thenReturn(0L);

        CourseDTO result = courseService.getCourseById(1L);

        assertThat(result.getCapacity()).isNull();
    }
}
