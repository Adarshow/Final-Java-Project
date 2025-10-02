package com.healthcare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "billing_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "billing_id")
    private Billing billing;
    
    private String description;
    private Integer quantity;
    private Double unitPrice;
    
    public Double getAmount() {
        return quantity != null && unitPrice != null ? quantity * unitPrice : 0.0;
    }
}
