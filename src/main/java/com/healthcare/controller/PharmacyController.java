package com.healthcare.controller;

import com.healthcare.entity.Pharmacy;
import com.healthcare.service.PharmacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/pharmacy")
public class PharmacyController {

    @Autowired
    private PharmacyService pharmacyService;

    @GetMapping
    public String listMedicines(Model model) {
        List<Pharmacy> medicines = pharmacyService.findAll();
        model.addAttribute("medicines", medicines);
        return "pharmacy/list";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("medicine", new Pharmacy());
        return "pharmacy/form";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Pharmacy medicine = pharmacyService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid medicine Id:" + id));
        model.addAttribute("medicine", medicine);
        return "pharmacy/form";
    }

    @PostMapping("/save")
    public String saveMedicine(@ModelAttribute Pharmacy medicine, RedirectAttributes redirectAttributes) {
        pharmacyService.save(medicine);
        redirectAttributes.addFlashAttribute("successMessage", "Medicine saved successfully");
        return "redirect:/pharmacy";
    }

    @GetMapping("/{id}/delete")
    public String deleteMedicine(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        pharmacyService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Medicine deleted successfully");
        return "redirect:/pharmacy";
    }

    @GetMapping("/search")
    public String searchMedicines(@RequestParam String name, Model model) {
        List<Pharmacy> medicines = pharmacyService.findByMedicineNameContaining(name);
        model.addAttribute("medicines", medicines);
        model.addAttribute("searchTerm", name);
        return "pharmacy/list";
    }
}
