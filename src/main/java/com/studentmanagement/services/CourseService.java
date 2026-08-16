package com.studentmanagement.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.studentmanagement.dto.CourseDTO;
import com.studentmanagement.entities.Course;
import com.studentmanagement.exceptions.InvalidRequestException;
import com.studentmanagement.exceptions.ResourceNotFoundException;
import com.studentmanagement.repositories.CourseRepository;
import com.studentmanagement.repositories.EnrollmentRepository;

@Service
@AllArgsConstructor
public class CourseService implements ICourseService {
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    public Page<CourseDTO> getAllCourses(String search, Pageable pageable) {
        Page<Course> page = (search == null || search.isBlank())
                ? courseRepository.findAll(pageable)
                : courseRepository.findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(search, search, pageable);
        return page.map(this::toDTO);
    }

    @Override
    public CourseDTO getCourseById(Long id) {
        return toDTO(findCourseOrThrow(id));
    }

    @Override
    public CourseDTO createCourse(CourseDTO dto) {
        Course course = new Course();
        applyDto(course, dto);
        return toDTO(courseRepository.save(course));
    }

    @Override
    public CourseDTO updateCourse(CourseDTO dto) {
        if (dto.getIdCourse() == null) {
            throw new InvalidRequestException("idCourse is required to update a course");
        }
        Course course = findCourseOrThrow(dto.getIdCourse());
        applyDto(course, dto);
        return toDTO(courseRepository.save(course));
    }

    @Override
    public void deleteCourse(Long id) {
        Course course = findCourseOrThrow(id);
        courseRepository.delete(course);
    }

    private Course findCourseOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + id));
    }

    private void applyDto(Course course, CourseDTO dto) {
        course.setName(dto.getName());
        course.setCode(dto.getCode());
        course.setCredit(dto.getCredit());
        course.setDescription(dto.getDescription());
        course.setCapacity(dto.getCapacity());
    }

    private CourseDTO toDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setIdCourse(course.getIdCourse());
        dto.setName(course.getName());
        dto.setCode(course.getCode());
        dto.setCredit(course.getCredit());
        dto.setDescription(course.getDescription());
        dto.setCapacity(course.getCapacity());
        dto.setEnrolledCount(enrollmentRepository.countByCourse_IdCourse(course.getIdCourse()));
        return dto;
    }
}
