package com.MindCare.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String dosage;
    private String frequency;
    private LocalTime dosageTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private String instructions;
    private String prescribedBy;

    @Transient
    private boolean missed;

    private boolean active;
    private boolean taken;
    private LocalDate lastTakenDate;
    private boolean emailNotification;
    private int escalateAfterHours;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Medication() {}

    public Medication(Long id, String name, String dosage, String frequency, LocalTime dosageTime,
                      LocalDate startDate, LocalDate endDate, String instructions, String prescribedBy,
                      boolean missed, boolean active, boolean taken, LocalDate lastTakenDate,
                      boolean emailNotification, int escalateAfterHours, User user) {
        this.id = id;
        this.name = name;
        this.dosage = dosage;
        this.frequency = frequency;
        this.dosageTime = dosageTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.instructions = instructions;
        this.prescribedBy = prescribedBy;
        this.missed = missed;
        this.active = active;
        this.taken = taken;
        this.lastTakenDate = lastTakenDate;
        this.emailNotification = emailNotification;
        this.escalateAfterHours = escalateAfterHours;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }

    public LocalTime getDosageTime() { return dosageTime; }
    public void setDosageTime(LocalTime dosageTime) { this.dosageTime = dosageTime; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public String getPrescribedBy() { return prescribedBy; }
    public void setPrescribedBy(String prescribedBy) { this.prescribedBy = prescribedBy; }

    public boolean isMissed() { return missed; }
    public void setMissed(boolean missed) { this.missed = missed; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public boolean isTaken() { return taken; }
    public void setTaken(boolean taken) { this.taken = taken; }

    public LocalDate getLastTakenDate() { return lastTakenDate; }
    public void setLastTakenDate(LocalDate lastTakenDate) { this.lastTakenDate = lastTakenDate; }

    public boolean isEmailNotification() { return emailNotification; }
    public void setEmailNotification(boolean emailNotification) { this.emailNotification = emailNotification; }

    public int getEscalateAfterHours() { return escalateAfterHours; }
    public void setEscalateAfterHours(int escalateAfterHours) { this.escalateAfterHours = escalateAfterHours; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}

