package com.healthcare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "billings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Billing {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
    
    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;
    
    private LocalDate date;
    
    @OneToMany(mappedBy = "billing", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BillingItem> items = new ArrayList<>();
    
    private Double totalAmount;
    
    @Enumerated(EnumType.STRING)
    private BillingStatus status = BillingStatus.PENDING;
    
    @Column(length = 500)
    private String notes;
    
    public enum BillingStatus {
        PENDING, PAID, CANCELLED
    }
}