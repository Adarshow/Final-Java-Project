package com.healthcare.controller;

import com.healthcare.entity.Doctor;
import com.healthcare.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public String getAllDoctors(Model model) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return "doctors/list";
    }

    @GetMapping("/{id}")
    public String getDoctorDetails(@PathVariable Long id, Model model) {
        Optional<Doctor> doctor = doctorService.getDoctorById(id);
        if (doctor.isPresent()) {
            model.addAttribute("doctor", doctor.get());
            return "doctors/details";
        }
        return "redirect:/doctors";
    }

    @GetMapping("/new")
    public String showNewDoctorForm(Model model) {
        model.addAttribute("doctor", new Doctor());
        return "doctors/form";
    }

    @PostMapping
    public String saveDoctor(@ModelAttribute Doctor doctor, RedirectAttributes redirectAttributes) {
        doctorService.saveDoctor(doctor);
        redirectAttributes.addFlashAttribute("successMessage", "Doctor saved successfully!");
        return "redirect:/doctors";
    }

    @GetMapping("/edit/{id}")
    public String showEditDoctorForm(@PathVariable Long id, Model model) {
        Optional<Doctor> doctor = doctorService.getDoctorById(id);
        if (doctor.isPresent()) {
            model.addAttribute("doctor", doctor.get());
            return "doctors/form";
        }
        return "redirect:/doctors";
    }

    @GetMapping("/delete/{id}")
    public String deleteDoctor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        doctorService.deleteDoctor(id);
        redirectAttributes.addFlashAttribute("successMessage", "Doctor deleted successfully!");
        return "redirect:/doctors";
    }

    @GetMapping("/search")
    public String searchDoctors(@RequestParam String specialization, Model model) {
        List<Doctor> doctors = doctorService.searchDoctorsBySpecialization(specialization);
        model.addAttribute("doctors", doctors);
        model.addAttribute("searchTerm", specialization);
        return "doctors/list";
    }
}