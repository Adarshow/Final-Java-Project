package com.healthcare.service;

import com.healthcare.entity.Pharmacy;
import com.healthcare.repository.PharmacyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PharmacyService {

    @Autowired
    private PharmacyRepository pharmacyRepository;
    
    public List<Pharmacy> getAllMedicines() {
        return pharmacyRepository.findAll();
    }
    
    public Optional<Pharmacy> getMedicineById(Long id) {
        return pharmacyRepository.findById(id);
    }
    
    public Pharmacy saveMedicine(Pharmacy medicine) {
        return pharmacyRepository.save(medicine);
    }
    
    public void deleteMedicine(Long id) {
        pharmacyRepository.deleteById(id);
    }
    
    public List<Pharmacy> searchMedicinesByName(String name) {
        return pharmacyRepository.findByMedicineNameContainingIgnoreCase(name);
    }
    
    public List<Pharmacy> getExpiredMedicines() {
        return pharmacyRepository.findByExpiryDateBefore(LocalDate.now());
    }
    
    public List<Pharmacy> getLowStockMedicines(Integer minStock) {
        return pharmacyRepository.findByStockLessThan(minStock);
    }
    
    public boolean dispenseMedicine(Long medicineId, Integer quantity) {
        Optional<Pharmacy> medicineOpt = pharmacyRepository.findById(medicineId);
        if (medicineOpt.isPresent()) {
            Pharmacy medicine = medicineOpt.get();
            if (medicine.getStock() >= quantity) {
                medicine.setStock(medicine.getStock() - quantity);
                pharmacyRepository.save(medicine);
                return true;
            }
        }
        return false;
    }
}