package com.MindCare.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class MedicationResponse {
    private Long id;
    private String name;
    private String dosage;
    private String frequency;
    private List<String> times;
    private LocalDate startDate;
    private LocalDate endDate;
    private String notes;
    private boolean reminderEnabled;
    private boolean push;
    private boolean sms;
    private boolean emailReminder;
    public MedicationResponse(Long id, String name, String dosage, String frequency, List<String> times,
                              LocalDate startDate, LocalDate endDate, String notes,
                              boolean reminderEnabled, boolean push, boolean sms, boolean emailReminder) {
        this.id = id;
        this.name = name;
        this.dosage = dosage;
        this.frequency = frequency;
        this.times = times;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
        this.reminderEnabled = reminderEnabled;
        this.push = push;
        this.sms = sms;
        this.emailReminder = emailReminder;
    }
}
