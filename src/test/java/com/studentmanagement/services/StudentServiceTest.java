package com.studentmanagement.services;

import com.studentmanagement.dto.StudentDTO;
import com.studentmanagement.entities.Department;
import com.studentmanagement.entities.Enrollment;
import com.studentmanagement.entities.Student;
import com.studentmanagement.exceptions.InvalidRequestException;
import com.studentmanagement.exceptions.ResourceNotFoundException;
import com.studentmanagement.repositories.DepartmentRepository;
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
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void getStudentById_whenNotFound_throwsResourceNotFoundException() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentById(99L));
    }

    @Test
    void createStudent_whenDepartmentNotFound_throwsResourceNotFoundException() {
        StudentDTO dto = validStudentDto();
        dto.setDepartmentId(5L);
        when(departmentRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.createStudent(dto));
    }

    @Test
    void createStudent_mapsFieldsAndResolvesDepartment() {
        StudentDTO dto = validStudentDto();
        Department department = new Department();
        department.setIdDepartment(1L);
        department.setName("Computer Science");
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> {
            Student saved = invocation.getArgument(0);
            saved.setIdStudent(10L);
            return saved;
        });
        when(enrollmentRepository.findByStudent_IdStudent(10L)).thenReturn(List.of());

        StudentDTO result = studentService.createStudent(dto);

        assertThat(result.getIdStudent()).isEqualTo(10L);
        assertThat(result.getFirstName()).isEqualTo("Alice");
        assertThat(result.getDepartmentName()).isEqualTo("Computer Science");
        assertThat(result.getGpa()).isNull();
    }

    @Test
    void updateStudent_whenIdMissing_throwsInvalidRequestException() {
        StudentDTO dto = validStudentDto();
        dto.setIdStudent(null);

        assertThrows(InvalidRequestException.class, () -> studentService.updateStudent(dto));
    }

    @Test
    void deleteStudent_whenNotFound_throwsResourceNotFoundException() {
        when(studentRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.deleteStudent(404L));
    }

    @Test
    void getStudentById_computesGpaFromNonNullGradesOnly() {
        Department department = new Department();
        department.setIdDepartment(1L);
        department.setName("Computer Science");

        Student student = new Student();
        student.setIdStudent(1L);
        student.setFirstName("Alice");
        student.setLastName("Doe");
        student.setDepartment(department);

        Enrollment graded1 = new Enrollment();
        graded1.setGrade(10.0);
        Enrollment graded2 = new Enrollment();
        graded2.setGrade(20.0);
        Enrollment ungraded = new Enrollment();
        ungraded.setGrade(null);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(enrollmentRepository.findByStudent_IdStudent(1L)).thenReturn(List.of(graded1, graded2, ungraded));

        StudentDTO result = studentService.getStudentById(1L);

        assertThat(result.getGpa()).isEqualTo(15.0);
    }

    @Test
    void getStudentById_whenNoGrades_gpaIsNull() {
        Department department = new Department();
        department.setIdDepartment(1L);
        department.setName("Computer Science");

        Student student = new Student();
        student.setIdStudent(1L);
        student.setDepartment(department);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(enrollmentRepository.findByStudent_IdStudent(1L)).thenReturn(List.of());

        StudentDTO result = studentService.getStudentById(1L);

        assertThat(result.getGpa()).isNull();
    }

    private StudentDTO validStudentDto() {
        StudentDTO dto = new StudentDTO();
        dto.setIdStudent(1L);
        dto.setFirstName("Alice");
        dto.setLastName("Doe");
        dto.setEmail("alice@example.com");
        dto.setPhone("555-2000");
        dto.setDateOfBirth(LocalDate.of(2000, 1, 15));
        dto.setAddress("123 Main St");
        dto.setDepartmentId(1L);
        return dto;
    }
}
