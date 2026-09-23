package com.MindCare.service;

import com.MindCare.entity.GroqResponse;
import com.MindCare.entity.User;
import com.MindCare.repository.GroqResponseRepository;
import com.MindCare.repository.userRepo;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
public class GroqChatService {
    @Autowired
    private userRepo userRepository;

    public Long getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email))
                .getId();
    }

    private final ChatModel chatModel;
    private final GroqResponseRepository groqResponseRepository;


    private static final Map<String, String> LANGUAGE_NAMES = new HashMap<>();

    static {
        LANGUAGE_NAMES.put("en", "English");
        LANGUAGE_NAMES.put("es", "Spanish");
        LANGUAGE_NAMES.put("fr", "French");
        LANGUAGE_NAMES.put("de", "German");
        LANGUAGE_NAMES.put("it", "Italian");
        LANGUAGE_NAMES.put("pt", "Portuguese");
        LANGUAGE_NAMES.put("hi", "Hindi");
        LANGUAGE_NAMES.put("ja", "Japanese");
        LANGUAGE_NAMES.put("zh", "Chinese");


        LANGUAGE_NAMES.put("mr", "Marathi");
        LANGUAGE_NAMES.put("ta", "Tamil");  // Changed from "tm" to "ta"
        LANGUAGE_NAMES.put("kn", "Kannada"); // Changed from "kd" to "kn"
        LANGUAGE_NAMES.put("pa", "Punjabi"); // Changed from "pj" to "pa"


        LANGUAGE_NAMES.put("bn", "Bengali");
        LANGUAGE_NAMES.put("te", "Telugu");
        LANGUAGE_NAMES.put("gu", "Gujarati");
        LANGUAGE_NAMES.put("ml", "Malayalam");
        LANGUAGE_NAMES.put("or", "Odia");
        LANGUAGE_NAMES.put("ar", "Arabic");
    }

    public GroqChatService(ChatModel chatModel, GroqResponseRepository groqResponseRepository, userRepo userRepository) {
        this.chatModel = chatModel;
        this.groqResponseRepository = groqResponseRepository;
        this.userRepository = userRepository;
    }

    public String getResponse(String prompt, Long userId, String language) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("User not found with ID: " + userId));

        // Base system message for AI role
        String systemMessageContent =
                "You are a certified and empathetic mental health doctor. " +
                        "Provide and friendly advice on mental health topics such as depression, anxiety, stress, emotional regulation, self-care, and therapy. " +
                        "Avoid answering unrelated or inappropriate questions. " +
                        "Use a calm, professional tone and ensure your responses are medically grounded but easy to understand. Keep answers under 50-60 words.";

        // Add language instruction if not English
        if (!"en".equals(language)) {
            String languageName = LANGUAGE_NAMES.getOrDefault(language, "the requested language");
            systemMessageContent += " Please respond in " + languageName + ".";
        }

        SystemMessage systemMessage = new SystemMessage(systemMessageContent);

        // Create prompt
        UserMessage userMessage = new UserMessage(prompt);
        Prompt aiPrompt = new Prompt(List.of(systemMessage, userMessage));

        Message responseMessage = chatModel.call(aiPrompt).getResult().getOutput();
        String content = responseMessage.getText();

        // Create response and set user
        GroqResponse groqResponse = new GroqResponse();
        groqResponse.setPrompt(prompt);
        groqResponse.setResponse(content);
        groqResponse.setTimestamp(LocalDateTime.now());
        groqResponse.setUser(user);


        // Save response
        groqResponseRepository.save(groqResponse);

        return content;
    }
}