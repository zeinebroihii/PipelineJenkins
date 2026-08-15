package com.studentmanagement.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.studentmanagement.entities.Enrollment;
import com.studentmanagement.entities.Status;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByStudent_IdStudentAndCourse_IdCourseAndStatusIn(Long studentId, Long courseId, List<Status> statuses);
    long countByCourse_IdCourseAndStatus(Long courseId, Status status);
    long countByCourse_IdCourse(Long courseId);
    List<Enrollment> findByCourse_IdCourse(Long courseId);
    List<Enrollment> findByStudent_IdStudent(Long studentId);

    Page<Enrollment> findByStudent_IdStudent(Long studentId, Pageable pageable);
    Page<Enrollment> findByCourse_IdCourse(Long courseId, Pageable pageable);
    Page<Enrollment> findByStudent_IdStudentAndCourse_IdCourse(Long studentId, Long courseId, Pageable pageable);
}
