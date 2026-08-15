package com.studentmanagement.services;

import com.studentmanagement.dto.DepartmentDTO;
import com.studentmanagement.entities.Department;
import com.studentmanagement.entities.Student;
import com.studentmanagement.exceptions.ResourceNotFoundException;
import com.studentmanagement.repositories.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    @Test
    void getDepartmentById_whenNotFound_throwsResourceNotFoundException() {
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> departmentService.getDepartmentById(99L));
    }

    @Test
    void getAllDepartments_reportsStudentCount() {
        Department department = new Department();
        department.setIdDepartment(1L);
        department.setName("Computer Science");
        department.setStudents(List.of(new Student(), new Student()));

        Pageable pageable = PageRequest.of(0, 10);
        when(departmentRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(department)));

        List<DepartmentDTO> content = departmentService.getAllDepartments(null, pageable).getContent();

        assertThat(content).hasSize(1);
        assertThat(content.get(0).getStudentCount()).isEqualTo(2);
    }

    @Test
    void getAllDepartments_withSearchTerm_usesSearchQueryNotFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        when(departmentRepository.findByNameContainingIgnoreCase("Math", pageable))
                .thenReturn(new PageImpl<>(List.of()));

        departmentService.getAllDepartments("Math", pageable);

        verify(departmentRepository).findByNameContainingIgnoreCase("Math", pageable);
        verify(departmentRepository, never()).findAll(pageable);
    }
}
