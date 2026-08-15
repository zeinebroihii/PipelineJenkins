package com.studentmanagement.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.studentmanagement.dto.DepartmentDTO;

public interface IDepartmentService {
    Page<DepartmentDTO> getAllDepartments(String search, Pageable pageable);
    DepartmentDTO getDepartmentById(Long idDepartment);
    DepartmentDTO createDepartment(DepartmentDTO department);
    DepartmentDTO updateDepartment(DepartmentDTO department);
    void deleteDepartment(Long idDepartment);
}
