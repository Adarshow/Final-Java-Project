package com.healthcare.controller;

import com.healthcare.entity.Billing;
import com.healthcare.entity.BillingItem;
import com.healthcare.service.BillingService;
import com.healthcare.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/billing")
public class BillingController {

    @Autowired
    private BillingService billingService;
    
    @Autowired
    private PatientService patientService;

    @GetMapping
    public String listInvoices(Model model) {
        model.addAttribute("invoices", billingService.findAll());
        return "billing/list";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("invoice", new Billing());
        model.addAttribute("patients", patientService.getAllPatients());
        return "billing/form";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Billing invoice = billingService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid invoice Id:" + id));
        model.addAttribute("invoice", invoice);
        model.addAttribute("patients", patientService.getAllPatients());
        return "billing/form";
    }

    @PostMapping("/save")
    public String saveInvoice(@ModelAttribute Billing invoice, 
                             @RequestParam Long patientId,
                             RedirectAttributes redirectAttributes) {
        // Set patient
        invoice.setPatient(patientService.getPatientById(patientId).orElse(null));
        
        billingService.save(invoice);
        redirectAttributes.addFlashAttribute("successMessage", "Invoice saved successfully");
        return "redirect:/billing";
    }

    @GetMapping("/{id}/print")
    public String printInvoice(@PathVariable Long id, Model model) {
        Billing invoice = billingService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid invoice Id:" + id));
        model.addAttribute("invoice", invoice);
        return "billing/print";
    }
}