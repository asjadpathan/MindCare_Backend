package com.MindCare.controller;


import com.MindCare.dto.MedicationRequest;
import com.MindCare.entity.Medication;
import com.MindCare.service.MedicationService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
@RestController
@RequestMapping("/api/medications")
@CrossOrigin(origins = "http://localhost:5173")
public class MedicationController {

    private final MedicationService medicationService;

    @Autowired
    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @GetMapping
    public List<Medication> getAll(Principal principal) {
        return medicationService.getAllMedications(principal.getName());
    }

    @PostMapping
    public Medication addMedication(@RequestBody MedicationRequest req, Principal principal) {
        return medicationService.addMedication(req, principal.getName());
    }

    @PostMapping("/{id}/mark-taken")
    public void markAsTaken(@PathVariable Long id, Principal principal) {
        medicationService.markAsTaken(id, principal.getName());
    }

    @PatchMapping("/{id}/deactivate")
    public void deactivate(@PathVariable Long id, Principal principal) {
        medicationService.deactivate(id, principal.getName());
    }
}
