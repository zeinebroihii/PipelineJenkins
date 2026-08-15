package com.studentmanagement.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCourse;
    private String name;
    private String code;           // exemple : CS101
    private int credit;            // nombre de crédits
    private String description;
    private Integer capacity;      // null = unlimited

    @OneToMany(mappedBy = "course")
    private List<Enrollment> enrollments;

}
