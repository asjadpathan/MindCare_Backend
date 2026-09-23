package com.MindCare.controller;

import com.MindCare.service.GroqChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai-chat")
public class GroqController {

    private final GroqChatService groqChatService;

    public GroqController(GroqChatService groqChatService) {
        this.groqChatService = groqChatService;
    }

    @PostMapping
    public ResponseEntity<String> getResponse(@RequestBody GroqRequest request,
                                              @RequestAttribute("userId") Long userId) {
        String response = groqChatService.getResponse(
                request.getPrompt(),
                userId,
                request.getLanguage()
        );
        return ResponseEntity.ok(response);
    }

    // Updated DTO class to include language parameter
    public static class GroqRequest {
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
}