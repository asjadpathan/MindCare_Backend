package com.MindCare.dto;

public class GroqPrompt {
    private String prompt;
    private String language = "en"; // Default to English

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}

