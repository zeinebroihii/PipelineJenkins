package com.studentmanagement.services;

import com.studentmanagement.dto.EnrollmentDTO;
import com.studentmanagement.entities.Course;
import com.studentmanagement.entities.Enrollment;
import com.studentmanagement.entities.Status;
import com.studentmanagement.entities.Student;
import com.studentmanagement.exceptions.InvalidRequestException;
import com.studentmanagement.exceptions.ResourceNotFoundException;
import com.studentmanagement.repositories.CourseRepository;
import com.studentmanagement.repositories.EnrollmentRepository;
import com.studentmanagement.repositories.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    @Test
    void createEnrollment_whenStudentAlreadyEnrolled_throwsInvalidRequestException() {
        EnrollmentDTO dto = validEnrollmentDto();
        when(enrollmentRepository.existsByStudent_IdStudentAndCourse_IdCourseAndStatusIn(
                1L, 1L, List.of(Status.ACTIVE, Status.COMPLETED))).thenReturn(true);

        assertThrows(InvalidRequestException.class, () -> enrollmentService.createEnrollment(dto));
    }

    @Test
    void createEnrollment_whenCourseAtCapacity_throwsInvalidRequestException() {
        EnrollmentDTO dto = validEnrollmentDto();
        Course course = new Course();
        course.setIdCourse(1L);
        course.setName("Algorithms");
        course.setCapacity(2);

        when(enrollmentRepository.existsByStudent_IdStudentAndCourse_IdCourseAndStatusIn(
                1L, 1L, List.of(Status.ACTIVE, Status.COMPLETED))).thenReturn(false);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.countByCourse_IdCourseAndStatus(1L, Status.ACTIVE)).thenReturn(2L);

        assertThrows(InvalidRequestException.class, () -> enrollmentService.createEnrollment(dto));
    }

    @Test
    void createEnrollment_whenCourseHasNoCapacityLimit_succeeds() {
        EnrollmentDTO dto = validEnrollmentDto();
        Course course = new Course();
        course.setIdCourse(1L);
        course.setName("Algorithms");
        course.setCapacity(null);

        Student student = new Student();
        student.setIdStudent(1L);
        student.setFirstName("Alice");
        student.setLastName("Doe");

        when(enrollmentRepository.existsByStudent_IdStudentAndCourse_IdCourseAndStatusIn(
                1L, 1L, List.of(Status.ACTIVE, Status.COMPLETED))).thenReturn(false);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(invocation -> {
            Enrollment saved = invocation.getArgument(0);
            saved.setIdEnrollment(100L);
            return saved;
        });

        EnrollmentDTO result = enrollmentService.createEnrollment(dto);

        assertThat(result.getIdEnrollment()).isEqualTo(100L);
        assertThat(result.getStudentFullName()).isEqualTo("Alice Doe");
    }

    @Test
    void createEnrollment_whenCourseBelowCapacity_succeeds() {
        EnrollmentDTO dto = validEnrollmentDto();
        Course course = new Course();
        course.setIdCourse(1L);
        course.setName("Algorithms");
        course.setCapacity(5);

        Student student = new Student();
        student.setIdStudent(1L);
        student.setFirstName("Alice");
        student.setLastName("Doe");

        when(enrollmentRepository.existsByStudent_IdStudentAndCourse_IdCourseAndStatusIn(
                1L, 1L, List.of(Status.ACTIVE, Status.COMPLETED))).thenReturn(false);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.countByCourse_IdCourseAndStatus(1L, Status.ACTIVE)).thenReturn(3L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EnrollmentDTO result = enrollmentService.createEnrollment(dto);

        assertThat(result.getStudentFullName()).isEqualTo("Alice Doe");
    }

    @Test
    void deleteEnrollment_whenNotFound_throwsResourceNotFoundException() {
        when(enrollmentRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> enrollmentService.deleteEnrollment(404L));
    }

    @Test
    void updateEnrollment_whenIdMissing_throwsInvalidRequestException() {
        EnrollmentDTO dto = validEnrollmentDto();
        dto.setIdEnrollment(null);

        assertThrows(InvalidRequestException.class, () -> enrollmentService.updateEnrollment(dto));
    }

    private EnrollmentDTO validEnrollmentDto() {
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setEnrollmentDate(LocalDate.now());
        dto.setStatus(Status.ACTIVE);
        dto.setStudentId(1L);
        dto.setCourseId(1L);
        return dto;
    }
}
