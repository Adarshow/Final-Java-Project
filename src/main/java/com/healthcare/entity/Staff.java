package com.healthcare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "staff")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Staff {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String role; // Receptionist, Nurse
    private String contact;
}