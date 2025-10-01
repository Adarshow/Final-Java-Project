package com.healthcare.repository;

import com.healthcare.entity.Appointment;
import com.healthcare.entity.Doctor;
import com.healthcare.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatient(Patient patient);
    List<Appointment> findByDoctor(Doctor doctor);
    List<Appointment> findByDoctorAndDateTimeBetween(Doctor doctor, LocalDateTime start, LocalDateTime end);
    List<Appointment> findByStatus(Appointment.AppointmentStatus status);
}