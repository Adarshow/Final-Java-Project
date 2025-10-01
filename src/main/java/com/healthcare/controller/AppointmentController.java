package com.healthcare.controller;

import com.healthcare.entity.Appointment;
import com.healthcare.entity.Doctor;
import com.healthcare.entity.Patient;
import com.healthcare.service.AppointmentService;
import com.healthcare.service.DoctorService;
import com.healthcare.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;
    
    @Autowired
    private PatientService patientService;
    
    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public String getAllAppointments(Model model) {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        model.addAttribute("appointments", appointments);
        return "appointments/list";
    }

    @GetMapping("/{id}")
    public String getAppointmentDetails(@PathVariable Long id, Model model) {
        Optional<Appointment> appointment = appointmentService.getAppointmentById(id);
        if (appointment.isPresent()) {
            model.addAttribute("appointment", appointment.get());
            return "appointments/details";
        }
        return "redirect:/appointments";
    }

    @GetMapping("/new")
    public String showNewAppointmentForm(Model model) {
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("patients", patientService.getAllPatients());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("statuses", Appointment.AppointmentStatus.values());
        return "appointments/form";
    }

    @PostMapping
    public String saveAppointment(@ModelAttribute Appointment appointment, 
                                 @RequestParam Long patientId,
                                 @RequestParam Long doctorId,
                                 RedirectAttributes redirectAttributes) {
        Optional<Patient> patient = patientService.getPatientById(patientId);
        Optional<Doctor> doctor = doctorService.getDoctorById(doctorId);
        
        if (patient.isPresent() && doctor.isPresent()) {
            appointment.setPatient(patient.get());
            appointment.setDoctor(doctor.get());
            
            if (appointmentService.isDoctorAvailable(doctor.get(), appointment.getDateTime())) {
                appointmentService.saveAppointment(appointment);
                redirectAttributes.addFlashAttribute("successMessage", "Appointment scheduled successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Doctor is not available at the selected time!");
                return "redirect:/appointments/new";
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid patient or doctor selected!");
            return "redirect:/appointments/new";
        }
        
        return "redirect:/appointments";
    }

    @GetMapping("/edit/{id}")
    public String showEditAppointmentForm(@PathVariable Long id, Model model) {
        Optional<Appointment> appointment = appointmentService.getAppointmentById(id);
        if (appointment.isPresent()) {
            model.addAttribute("appointment", appointment.get());
            model.addAttribute("patients", patientService.getAllPatients());
            model.addAttribute("doctors", doctorService.getAllDoctors());
            model.addAttribute("statuses", Appointment.AppointmentStatus.values());
            return "appointments/form";
        }
        return "redirect:/appointments";
    }

    @GetMapping("/delete/{id}")
    public String deleteAppointment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        appointmentService.deleteAppointment(id);
        redirectAttributes.addFlashAttribute("successMessage", "Appointment deleted successfully!");
        return "redirect:/appointments";
    }
    
    @GetMapping("/doctor/{doctorId}")
    public String getDoctorAppointments(@PathVariable Long doctorId, Model model) {
        Optional<Doctor> doctor = doctorService.getDoctorById(doctorId);
        if (doctor.isPresent()) {
            List<Appointment> appointments = appointmentService.getAppointmentsByDoctor(doctor.get());
            model.addAttribute("appointments", appointments);
            model.addAttribute("doctor", doctor.get());
            return "appointments/doctor-schedule";
        }
        return "redirect:/appointments";
    }
    
    @GetMapping("/patient/{patientId}")
    public String getPatientAppointments(@PathVariable Long patientId, Model model) {
        Optional<Patient> patient = patientService.getPatientById(patientId);
        if (patient.isPresent()) {
            List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patient.get());
            model.addAttribute("appointments", appointments);
            model.addAttribute("patient", patient.get());
            return "appointments/patient-appointments";
        }
        return "redirect:/appointments";
    }
    
    @PostMapping("/{id}/status")
    public String updateAppointmentStatus(@PathVariable Long id, 
                                         @RequestParam Appointment.AppointmentStatus status,
                                         RedirectAttributes redirectAttributes) {
        appointmentService.updateAppointmentStatus(id, status);
        redirectAttributes.addFlashAttribute("successMessage", "Appointment status updated successfully!");
        return "redirect:/appointments/" + id;
    }
}