package com.healthcare.repository;

import com.healthcare.entity.Appointment;
import com.healthcare.entity.Billing;
import com.healthcare.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {
    List<Billing> findByPatient(Patient patient);
    Optional<Billing> findByAppointment(Appointment appointment);
    List<Billing> findByStatus(Billing.BillingStatus status);
}