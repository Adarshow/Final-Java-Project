package com.healthcare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String specialization;
    private String contact;
    private String schedule;
    private String role;
    
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Appointment> appointments = new ArrayList<>();
    
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Prescription> prescriptions = new ArrayList<>();
    
    // Explicit setters
    public void setName(String name) {
        this.name = name;
    }
    
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    public void setContact(String contact) {
        this.contact = contact;
    }
    
    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}