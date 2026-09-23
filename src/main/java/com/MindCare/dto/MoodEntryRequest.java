package com.MindCare.dto;

import java.time.LocalDate;
import java.util.List;

public class MoodEntryRequest {
    private LocalDate date;
    private int mood;
    private List<String> factors;
    private String note;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public List<String> getFactors() {
        return factors;
    }

    public void setFactors(List<String> factors) {
        this.factors = factors;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
// Getters and Setters
}
