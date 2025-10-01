package com.healthcare.config;

import com.healthcare.entity.*;
import com.healthcare.repository.*;
import com.healthcare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create admin user
        if (userRepository.count() == 0) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin"));
            adminUser.setFullName("System Administrator");
            adminUser.setRole(User.Role.ADMIN);
            adminUser.setEnabled(true);
            userRepository.save(adminUser);
            
            User doctorUser = new User();
            doctorUser.setUsername("doctor");
            doctorUser.setPassword(passwordEncoder.encode("doctor"));
            doctorUser.setFullName("Dr. John Smith");
            doctorUser.setRole(User.Role.DOCTOR);
            doctorUser.setEnabled(true);
            userRepository.save(doctorUser);
            
            User staffUser = new User();
            staffUser.setUsername("staff");
            staffUser.setPassword(passwordEncoder.encode("staff"));
            staffUser.setFullName("Jane Wilson");
            staffUser.setRole(User.Role.STAFF);
            staffUser.setEnabled(true);
            userRepository.save(staffUser);
        }
        
        // Create sample doctors
        if (doctorRepository.count() == 0) {
            Doctor doctor1 = new Doctor();
            doctor1.setName("Dr. John Smith");
            doctor1.setSpecialization("Cardiology");
            doctor1.setContact("555-123-4567");
            doctor1.setSchedule("Mon-Fri, 9AM-5PM");
            doctorRepository.save(doctor1);
            
            Doctor doctor2 = new Doctor();
            doctor2.setName("Dr. Sarah Johnson");
            doctor2.setSpecialization("Pediatrics");
            doctor2.setContact("555-987-6543");
            doctor2.setSchedule("Mon-Wed, 8AM-4PM");
            doctorRepository.save(doctor2);
            
            Doctor doctor3 = new Doctor();
            doctor3.setName("Dr. Michael Chen");
            doctor3.setSpecialization("Neurology");
            doctor3.setContact("555-456-7890");
            doctor3.setSchedule("Tue-Fri, 10AM-6PM");
            doctorRepository.save(doctor3);
        }
        
        // Create sample patients
        if (patientRepository.count() == 0) {
            Patient patient1 = new Patient();
            patient1.setName("Robert Brown");
            patient1.setAge(45);
            patient1.setGender("Male");
            patient1.setContact("555-111-2222");
            patient1.setAddress("123 Main St, Anytown");
            patient1.setMedicalHistory("Hypertension, Diabetes");
            patientRepository.save(patient1);
            
            Patient patient2 = new Patient();
            patient2.setName("Emily Davis");
            patient2.setAge(32);
            patient2.setGender("Female");
            patient2.setContact("555-333-4444");
            patient2.setAddress("456 Oak Ave, Somewhere");
            patient2.setMedicalHistory("Asthma");
            patientRepository.save(patient2);
            
            Patient patient3 = new Patient();
            patient3.setName("James Wilson");
            patient3.setAge(28);
            patient3.setGender("Male");
            patient3.setContact("555-555-6666");
            patient3.setAddress("789 Pine Rd, Nowhere");
            patient3.setMedicalHistory("None");
            patientRepository.save(patient3);
            
            Patient patient4 = new Patient();
            patient4.setName("Sophia Martinez");
            patient4.setAge(65);
            patient4.setGender("Female");
            patient4.setContact("555-777-8888");
            patient4.setAddress("101 Elm St, Anywhere");
            patient4.setMedicalHistory("Arthritis, High Cholesterol");
            patientRepository.save(patient4);
        }
        
        // Create sample appointments
        if (appointmentRepository.count() == 0) {
            Doctor doctor1 = doctorRepository.findAll().get(0);
            Doctor doctor2 = doctorRepository.findAll().get(1);
            
            Patient patient1 = patientRepository.findAll().get(0);
            Patient patient2 = patientRepository.findAll().get(1);
            Patient patient3 = patientRepository.findAll().get(2);
            
            Appointment appointment1 = new Appointment();
            appointment1.setPatient(patient1);
            appointment1.setDoctor(doctor1);
            appointment1.setDateTime(LocalDateTime.now().plusDays(1));
            appointment1.setStatus(Appointment.AppointmentStatus.SCHEDULED);
            appointmentRepository.save(appointment1);
            
            Appointment appointment2 = new Appointment();
            appointment2.setPatient(patient2);
            appointment2.setDoctor(doctor2);
            appointment2.setDateTime(LocalDateTime.now().plusDays(2));
            appointment2.setStatus(Appointment.AppointmentStatus.SCHEDULED);
            appointmentRepository.save(appointment2);
            
            Appointment appointment3 = new Appointment();
            appointment3.setPatient(patient3);
            appointment3.setDoctor(doctor1);
            appointment3.setDateTime(LocalDateTime.now().minusDays(1));
            appointment3.setStatus(Appointment.AppointmentStatus.COMPLETED);
            appointmentRepository.save(appointment3);
        }
    }
}