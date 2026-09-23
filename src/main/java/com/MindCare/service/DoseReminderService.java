package com.MindCare.service;

import com.MindCare.entity.Medication;
import com.MindCare.repository.MedicationRepository;
import com.MindCare.repository.userRepo;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service

public class DoseReminderService {

    private final MedicationRepository medicationRepository;
    private final userRepo userRepository;
    private final EmailService emailService;

    public DoseReminderService(MedicationRepository medicationRepository, userRepo userRepository, EmailService emailService) {
        this.medicationRepository = medicationRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void checkMissedDoses() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        List<Medication> medications = medicationRepository.findAll();

        for (Medication med : medications) {
            if (!med.isTaken() &&
                    med.getEndDate() != null &&
                    !today.isAfter(med.getEndDate()) &&
                    med.getDosageTime() != null &&
                    now.isAfter(med.getDosageTime()) &&
                    (med.getLastTakenDate() == null || !med.getLastTakenDate().equals(today))) {

                String email = med.getUser().getEmail();
                String name = med.getName();

                String body = """
                        MEDICATION ALERT

                        Our system detected that you might have missed your scheduled dose:

                        Medication: %s
                        Dosage: %s
                        Scheduled Time: %s

                        For your well-being and effective treatment, it's important not to miss your doses.

                        Please take your medication as soon as possible and mark it as taken in the app.

                        Stay healthy,
                        Team MindCare.
                        """.formatted(name, med.getDosage(), med.getDosageTime());

                System.out.println("Sending email to: " + email);
                emailService.sendEmail(email, "🚨 Missed Medication Detected", body);

                // Mark as notified to avoid repeat emails
                med.setLastTakenDate(today);
                medicationRepository.save(med);
            } else {
                System.out.println("Medication skipped for: " + med.getName());
            }
        }
    }
}
