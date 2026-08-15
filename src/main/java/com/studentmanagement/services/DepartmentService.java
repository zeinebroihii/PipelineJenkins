package com.studentmanagement.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.studentmanagement.dto.DepartmentDTO;
import com.studentmanagement.entities.Department;
import com.studentmanagement.exceptions.InvalidRequestException;
import com.studentmanagement.exceptions.ResourceNotFoundException;
import com.studentmanagement.repositories.DepartmentRepository;

@Service
@AllArgsConstructor
public class DepartmentService implements IDepartmentService {
    private final DepartmentRepository departmentRepository;

    @Override
    public Page<DepartmentDTO> getAllDepartments(String search, Pageable pageable) {
        Page<Department> page = (search == null || search.isBlank())
                ? departmentRepository.findAll(pageable)
                : departmentRepository.findByNameContainingIgnoreCase(search, pageable);
        return page.map(this::toDTO);
    }

    @Override
    public DepartmentDTO getDepartmentById(Long idDepartment) {
        return toDTO(findDepartmentOrThrow(idDepartment));
    }

    @Override
    public DepartmentDTO createDepartment(DepartmentDTO dto) {
        Department department = new Department();
        applyDto(department, dto);
        return toDTO(departmentRepository.save(department));
    }

    @Override
    public DepartmentDTO updateDepartment(DepartmentDTO dto) {
        if (dto.getIdDepartment() == null) {
            throw new InvalidRequestException("idDepartment is required to update a department");
        }
        Department department = findDepartmentOrThrow(dto.getIdDepartment());
        applyDto(department, dto);
        return toDTO(departmentRepository.save(department));
    }

    @Override
    public void deleteDepartment(Long idDepartment) {
        Department department = findDepartmentOrThrow(idDepartment);
        departmentRepository.delete(department);
    }

    private Department findDepartmentOrThrow(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + id));
    }

    private void applyDto(Department department, DepartmentDTO dto) {
        department.setName(dto.getName());
        department.setLocation(dto.getLocation());
        department.setPhone(dto.getPhone());
        department.setHead(dto.getHead());
    }

    private DepartmentDTO toDTO(Department department) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setIdDepartment(department.getIdDepartment());
        dto.setName(department.getName());
        dto.setLocation(department.getLocation());
        dto.setPhone(department.getPhone());
        dto.setHead(department.getHead());
        dto.setStudentCount(department.getStudents() != null ? department.getStudents().size() : 0);
        return dto;
    }
}
