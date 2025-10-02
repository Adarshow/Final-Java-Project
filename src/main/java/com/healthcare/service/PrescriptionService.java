package com.healthcare.service;

import com.healthcare.entity.Doctor;
import com.healthcare.entity.Patient;
import com.healthcare.entity.Prescription;
import com.healthcare.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;
    
    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }
    
    public List<Prescription> findAll() {
        return prescriptionRepository.findAll();
    }
    
    public Optional<Prescription> getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id);
    }
    
    public Optional<Prescription> findById(Long id) {
        return prescriptionRepository.findById(id);
    }
    
    public Prescription savePrescription(Prescription prescription) {
        return prescriptionRepository.save(prescription);
    }
    
    public Prescription save(Prescription prescription) {
        return prescriptionRepository.save(prescription);
    }
    
    public void deletePrescription(Long id) {
        prescriptionRepository.deleteById(id);
    }
    
    public List<Prescription> getPrescriptionsByPatient(Patient patient) {
        return prescriptionRepository.findByPatient(patient);
    }
    
    public List<Prescription> getPrescriptionsByDoctor(Doctor doctor) {
        return prescriptionRepository.findByDoctor(doctor);
    }
    
    public Prescription createPrescription(Patient patient, Doctor doctor, String diagnosis, String notes) {
        Prescription prescription = new Prescription();
        prescription.setPatient(patient);
        prescription.setDoctor(doctor);
        prescription.setDiagnosis(diagnosis);
        prescription.setNotes(notes);
        return prescriptionRepository.save(prescription);
    }
}