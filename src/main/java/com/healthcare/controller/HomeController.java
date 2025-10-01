package com.healthcare.controller;

import com.healthcare.service.AppointmentService;
import com.healthcare.service.DoctorService;
import com.healthcare.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private PatientService patientService;
    
    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("patientCount", patientService.getAllPatients().size());
        model.addAttribute("doctorCount", doctorService.getAllDoctors().size());
        model.addAttribute("appointmentCount", appointmentService.getAllAppointments().size());
        return "index";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("patientCount", patientService.getAllPatients().size());
        model.addAttribute("doctorCount", doctorService.getAllDoctors().size());
        model.addAttribute("appointmentCount", appointmentService.getAllAppointments().size());
        model.addAttribute("recentAppointments", appointmentService.getAllAppointments().subList(0, 
            Math.min(5, appointmentService.getAllAppointments().size())));
        return "dashboard";
    }
}