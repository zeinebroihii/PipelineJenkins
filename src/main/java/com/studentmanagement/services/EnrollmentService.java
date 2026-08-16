package com.studentmanagement.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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

import java.util.List;

@Service
@AllArgsConstructor
public class EnrollmentService implements IEnrollment {
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Override
    public Page<EnrollmentDTO> getAllEnrollments(Long studentId, Long courseId, Pageable pageable) {
        Page<Enrollment> page;
        if (studentId != null && courseId != null) {
            page = enrollmentRepository.findByStudent_IdStudentAndCourse_IdCourse(studentId, courseId, pageable);
        } else if (studentId != null) {
            page = enrollmentRepository.findByStudent_IdStudent(studentId, pageable);
        } else if (courseId != null) {
            page = enrollmentRepository.findByCourse_IdCourse(courseId, pageable);
        } else {
            page = enrollmentRepository.findAll(pageable);
        }
        return page.map(this::toDTO);
    }

    @Override
    public EnrollmentDTO getEnrollmentById(Long id) {
        return toDTO(findEnrollmentOrThrow(id));
    }

    @Override
    public EnrollmentDTO createEnrollment(EnrollmentDTO dto) {
        if (enrollmentRepository.existsByStudent_IdStudentAndCourse_IdCourseAndStatusIn(
                dto.getStudentId(), dto.getCourseId(), List.of(Status.ACTIVE, Status.COMPLETED))) {
            throw new InvalidRequestException("Student is already enrolled in this course");
        }
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + dto.getCourseId()));
        if (course.getCapacity() != null) {
            long activeCount = enrollmentRepository.countByCourse_IdCourseAndStatus(dto.getCourseId(), Status.ACTIVE);
            if (activeCount >= course.getCapacity()) {
                throw new InvalidRequestException("Course \"" + course.getName() + "\" is at full capacity");
            }
        }
        Enrollment enrollment = new Enrollment();
        applyDto(enrollment, dto);
        return toDTO(enrollmentRepository.save(enrollment));
    }

    @Override
    public EnrollmentDTO updateEnrollment(EnrollmentDTO dto) {
        if (dto.getIdEnrollment() == null) {
            throw new InvalidRequestException("idEnrollment is required to update an enrollment");
        }
        Enrollment enrollment = findEnrollmentOrThrow(dto.getIdEnrollment());
        applyDto(enrollment, dto);
        return toDTO(enrollmentRepository.save(enrollment));
    }

    @Override
    public void deleteEnrollment(Long id) {
        Enrollment enrollment = findEnrollmentOrThrow(id);
        enrollmentRepository.delete(enrollment);
    }

    private Enrollment findEnrollmentOrThrow(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id " + id));
    }

    private void applyDto(Enrollment enrollment, EnrollmentDTO dto) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + dto.getStudentId()));
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + dto.getCourseId()));
        enrollment.setEnrollmentDate(dto.getEnrollmentDate());
        enrollment.setGrade(dto.getGrade());
        enrollment.setStatus(dto.getStatus());
        enrollment.setStudent(student);
        enrollment.setCourse(course);
    }

    private EnrollmentDTO toDTO(Enrollment enrollment) {
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setIdEnrollment(enrollment.getIdEnrollment());
        dto.setEnrollmentDate(enrollment.getEnrollmentDate());
        dto.setGrade(enrollment.getGrade());
        dto.setStatus(enrollment.getStatus());
        if (enrollment.getStudent() != null) {
            dto.setStudentId(enrollment.getStudent().getIdStudent());
            dto.setStudentFullName(enrollment.getStudent().getFirstName() + " " + enrollment.getStudent().getLastName());
        }
        if (enrollment.getCourse() != null) {
            dto.setCourseId(enrollment.getCourse().getIdCourse());
            dto.setCourseName(enrollment.getCourse().getName());
        }
        return dto;
    }
}
