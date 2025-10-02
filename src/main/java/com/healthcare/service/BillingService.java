package com.healthcare.service;

import com.healthcare.entity.Appointment;
import com.healthcare.entity.Billing;
import com.healthcare.entity.Patient;
import com.healthcare.repository.BillingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillingService {

    @Autowired
    private BillingRepository billingRepository;
    
    public List<Billing> getAllBillings() {
        return billingRepository.findAll();
    }
    
    public List<Billing> findAll() {
        return billingRepository.findAll();
    }
    
    public Optional<Billing> getBillingById(Long id) {
        return billingRepository.findById(id);
    }
    
    public Optional<Billing> findById(Long id) {
        return billingRepository.findById(id);
    }
    
    public Billing saveBilling(Billing billing) {
        return billingRepository.save(billing);
    }
    
    public Billing save(Billing billing) {
        return billingRepository.save(billing);
    }
    
    public void deleteBilling(Long id) {
        billingRepository.deleteById(id);
    }
    
    public List<Billing> getBillingsByPatient(Patient patient) {
        return billingRepository.findByPatient(patient);
    }
    
    public Optional<Billing> getBillingByAppointment(Appointment appointment) {
        return billingRepository.findByAppointment(appointment);
    }
    
    public Billing generateBill(Appointment appointment, Double amount) {
        Billing billing = new Billing();
        billing.setAppointment(appointment);
        billing.setPatient(appointment.getPatient());
        billing.setTotalAmount(amount);
        billing.setStatus(Billing.BillingStatus.PENDING);
        return billingRepository.save(billing);
    }
    
    public Billing updatePaymentStatus(Long id, Billing.BillingStatus status) {
        Optional<Billing> billingOpt = billingRepository.findById(id);
        if (billingOpt.isPresent()) {
            Billing billing = billingOpt.get();
            billing.setStatus(status);
            return billingRepository.save(billing);
        }
        return null;
    }
}