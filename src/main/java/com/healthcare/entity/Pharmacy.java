package com.healthcare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "pharmacy")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pharmacy {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String medicineName;
    private Integer stock;
    private LocalDate expiryDate;
    private Double price;
}