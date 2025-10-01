package com.healthcare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    
    @OneToMany(mappedBy = "medicine", cascade = CascadeType.ALL)
    private List<Prescription> prescriptions = new ArrayList<>();
}