package com.healthcare.repository;

import com.healthcare.entity.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
    List<Pharmacy> findByMedicineNameContainingIgnoreCase(String medicineName);
    List<Pharmacy> findByExpiryDateBefore(LocalDate date);
    List<Pharmacy> findByStockLessThan(Integer minStock);
}