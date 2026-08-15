package com.studentmanagement.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.studentmanagement.dto.StudentDTO;
import com.studentmanagement.entities.Department;
import com.studentmanagement.entities.Enrollment;
import com.studentmanagement.entities.Student;
import com.studentmanagement.exceptions.InvalidRequestException;
import com.studentmanagement.exceptions.ResourceNotFoundException;
import com.studentmanagement.repositories.DepartmentRepository;
import com.studentmanagement.repositories.EnrollmentRepository;
import com.studentmanagement.repositories.StudentRepository;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class StudentService implements IStudentService {
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    public Page<StudentDTO> getAllStudents(String search, Pageable pageable) {
        Page<Student> page = (search == null || search.isBlank())
                ? studentRepository.findAll(pageable)
                : studentRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(search, search, pageable);
        return page.map(this::toDTO);
    }

    @Override
    public StudentDTO getStudentById(Long id) {
        return toDTO(findStudentOrThrow(id));
    }

    @Override
    public StudentDTO createStudent(StudentDTO dto) {
        Student student = new Student();
        applyDto(student, dto);
        return toDTO(studentRepository.save(student));
    }

    @Override
    public StudentDTO updateStudent(StudentDTO dto) {
        if (dto.getIdStudent() == null) {
            throw new InvalidRequestException("idStudent is required to update a student");
        }
        Student student = findStudentOrThrow(dto.getIdStudent());
        applyDto(student, dto);
        return toDTO(studentRepository.save(student));
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = findStudentOrThrow(id);
        studentRepository.delete(student);
    }

    private Student findStudentOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
    }

    private Department findDepartmentOrThrow(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + id));
    }

    private void applyDto(Student student, StudentDTO dto) {
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setPhone(dto.getPhone());
        student.setDateOfBirth(dto.getDateOfBirth());
        student.setAddress(dto.getAddress());
        student.setDepartment(findDepartmentOrThrow(dto.getDepartmentId()));
    }

    private StudentDTO toDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setIdStudent(student.getIdStudent());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());
        dto.setPhone(student.getPhone());
        dto.setDateOfBirth(student.getDateOfBirth());
        dto.setAddress(student.getAddress());
        if (student.getDepartment() != null) {
            dto.setDepartmentId(student.getDepartment().getIdDepartment());
            dto.setDepartmentName(student.getDepartment().getName());
        }
        List<Enrollment> enrollments = enrollmentRepository.findByStudent_IdStudent(student.getIdStudent());
        enrollments.stream()
                .map(Enrollment::getGrade)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average()
                .ifPresent(dto::setGpa);
        return dto;
    }
}
