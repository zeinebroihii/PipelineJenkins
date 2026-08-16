package com.studentmanagement.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import com.studentmanagement.dto.DepartmentDTO;
import com.studentmanagement.services.IDepartmentService;

@RestController
@RequestMapping("/Department")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class DepartmentController {
    private final IDepartmentService departmentService;

    @GetMapping("/getAllDepartment")
    public Page<DepartmentDTO> getAllDepartment(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 10) Pageable pageable) {
        return departmentService.getAllDepartments(search, pageable);
    }

    @GetMapping("/getDepartment/{id}")
    public DepartmentDTO getDepartment(@PathVariable Long id) { return departmentService.getDepartmentById(id); }

    @PostMapping("/createDepartment")
    public DepartmentDTO createDepartment(@Valid @RequestBody DepartmentDTO department) { return departmentService.createDepartment(department); }

    @PutMapping("/updateDepartment")
    public DepartmentDTO updateDepartment(@Valid @RequestBody DepartmentDTO department) {
        return departmentService.updateDepartment(department);
    }

    @DeleteMapping("/deleteDepartment/{id}")
    public void deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
    }
}
