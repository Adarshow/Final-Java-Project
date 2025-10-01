package com.healthcare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
    
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    
    private LocalDateTime dateTime;
    
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
    
    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
    private Billing billing;
    
    // Explicit setters
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    
    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
    
    public enum AppointmentStatus {
        SCHEDULED, COMPLETED, CANCELLED
    }
}