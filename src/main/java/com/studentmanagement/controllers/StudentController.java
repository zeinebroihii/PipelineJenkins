package com.studentmanagement.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import com.studentmanagement.dto.StudentDTO;
import com.studentmanagement.services.IStudentService;

@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class StudentController {
    private final IStudentService studentService;

    @GetMapping("/getAllStudents")
    public Page<StudentDTO> getAllStudents(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 10) Pageable pageable) {
        return studentService.getAllStudents(search, pageable);
    }

    @GetMapping("/getStudent/{id}")
    public StudentDTO getStudent(@PathVariable Long id) { return studentService.getStudentById(id); }

    @PostMapping("/createStudent")
    public StudentDTO createStudent(@Valid @RequestBody StudentDTO student) { return studentService.createStudent(student); }

    @PutMapping("/updateStudent")
    public StudentDTO updateStudent(@Valid @RequestBody StudentDTO student) {
        return studentService.updateStudent(student);
    }

    @DeleteMapping("/deleteStudent/{id}")
    public void deleteStudent(@PathVariable Long id) { studentService.deleteStudent(id); }
}
