package com.MindCare.service;



import com.MindCare.dto.MedicationRequest;
import com.MindCare.entity.Medication;
import com.MindCare.entity.User;
import com.MindCare.repository.MedicationRepository;
import com.MindCare.repository.userRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@CrossOrigin("http://localhost:5173/")
@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;

    private final userRepo userRepository;
    public MedicationService(MedicationRepository medicationRepository, userRepo userRepository) {
        this.medicationRepository = medicationRepository;
        this.userRepository = userRepository;
    }


    public List<Medication> getAllMedications(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        List<Medication> meds = medicationRepository.findByUser(user);

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        for (Medication med : meds) {
            boolean isMissed = med.isActive()
                    && (today.isAfter(med.getStartDate()) || today.isEqual(med.getStartDate()))
                    && (today.isBefore(med.getEndDate()) || today.isEqual(med.getEndDate()))
                    && now.isAfter(med.getDosageTime())
                    && (med.getLastTakenDate() == null || !med.getLastTakenDate().isEqual(today))
                    && !med.isTaken();

            med.setMissed(isMissed);
        }

        return meds;
    }


    public Medication addMedication(MedicationRequest req, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();

        Medication medication = new Medication(
                null, // id (will be auto-generated)
                req.getName(),
                req.getDosage(),
                req.getFrequency(),
                LocalTime.parse(req.getDosageTime()),
                LocalDate.parse(req.getStartDate()),
                LocalDate.parse(req.getEndDate()),
                req.getInstructions(),
                req.getPrescribedBy(),
                false, // missed
                req.isActive(),
                false, // taken
                null,  // lastTakenDate
                req.isEmailNotification(),
                req.getEscalateAfterHours(),
                user
        );

        return medicationRepository.save(medication);
    }


    public void markAsTaken(Long id, String userEmail) {
        Medication med = getUserMedicationById(id, userEmail);
        med.setTaken(true);
        med.setLastTakenDate(LocalDate.now());
        medicationRepository.save(med);
    }

    public void deactivate(Long id, String userEmail) {
        Medication med = getUserMedicationById(id, userEmail);
        med.setActive(false);
        medicationRepository.save(med);
    }
    private Medication getUserMedicationById(Long id, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        return medicationRepository.findById(id)
                .filter(m -> m.getUser().equals(user))
                .orElseThrow();
    }
}
