package com.healthcare.controller;

import com.healthcare.entity.Prescription;
import com.healthcare.entity.Medication;
import com.healthcare.service.DoctorService;
import com.healthcare.service.PatientService;
import com.healthcare.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;
    
    @Autowired
    private PatientService patientService;
    
    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public String listPrescriptions(Model model) {
        model.addAttribute("prescriptions", prescriptionService.findAll());
        return "prescriptions/list";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("prescription", new Prescription());
        model.addAttribute("patients", patientService.getAllPatients());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        return "prescriptions/form";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Prescription prescription = prescriptionService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid prescription Id:" + id));
        model.addAttribute("prescription", prescription);
        model.addAttribute("patients", patientService.getAllPatients());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        return "prescriptions/form";
    }

    @PostMapping("/save")
    public String savePrescription(@ModelAttribute Prescription prescription, 
                                 @RequestParam Long patientId,
                                 @RequestParam Long doctorId,
                                 RedirectAttributes redirectAttributes) {
        // Set patient and doctor
        prescription.setPatient(patientService.getPatientById(patientId).orElse(null));
        prescription.setDoctor(doctorService.getDoctorById(doctorId).orElse(null));
        
        prescriptionService.save(prescription);
        redirectAttributes.addFlashAttribute("successMessage", "Prescription saved successfully");
        return "redirect:/prescriptions";
    }

    @GetMapping("/{id}/print")
    public String printPrescription(@PathVariable Long id, Model model) {
        Prescription prescription = prescriptionService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid prescription Id:" + id));
        model.addAttribute("prescription", prescription);
        return "prescriptions/print";
    }
}