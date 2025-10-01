package com.healthcare.service;

import com.healthcare.entity.Appointment;
import com.healthcare.entity.Doctor;
import com.healthcare.entity.Patient;
import com.healthcare.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
    
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }
    
    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }
    
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
    
    public List<Appointment> getAppointmentsByPatient(Patient patient) {
        return appointmentRepository.findByPatient(patient);
    }
    
    public List<Appointment> getAppointmentsByDoctor(Doctor doctor) {
        return appointmentRepository.findByDoctor(doctor);
    }
    
    public List<Appointment> getDoctorSchedule(Doctor doctor, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByDoctorAndDateTimeBetween(doctor, start, end);
    }
    
    public boolean isDoctorAvailable(Doctor doctor, LocalDateTime dateTime) {
        LocalDateTime startTime = dateTime.minusHours(1);
        LocalDateTime endTime = dateTime.plusHours(1);
        
        List<Appointment> existingAppointments = appointmentRepository.findByDoctorAndDateTimeBetween(
                doctor, startTime, endTime);
        
        return existingAppointments.isEmpty();
    }
    
    public Appointment updateAppointmentStatus(Long id, Appointment.AppointmentStatus status) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);
        if (appointmentOpt.isPresent()) {
            Appointment appointment = appointmentOpt.get();
            appointment.setStatus(status);
            return appointmentRepository.save(appointment);
        }
        return null;
    }
}