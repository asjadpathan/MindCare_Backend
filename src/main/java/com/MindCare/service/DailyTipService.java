package com.MindCare.service;

import com.MindCare.entity.GroqResponse;
import com.MindCare.entity.User;
import com.MindCare.repository.GroqResponseRepository;
import com.MindCare.repository.MoodEntryRepository;
import com.MindCare.repository.userRepo;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DailyTipService {

    private final ChatModel chatModel;
    private final GroqResponseRepository groqResponseRepository;
    private final userRepo userRepository;
    private final MoodEntryRepository moodEntryRepository;

    public DailyTipService(ChatModel chatModel,
                                 GroqResponseRepository groqResponseRepository,
                                 userRepo userRepository,
                                 MoodEntryRepository moodEntryRepository) {
        this.chatModel = chatModel;
        this.groqResponseRepository = groqResponseRepository;
        this.userRepository = userRepository;
        this.moodEntryRepository = moodEntryRepository;
    }

    // Used for general chat: Save prompt & response
    public String getResponse(String prompt, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        SystemMessage systemMessage = new SystemMessage(
                "You are a certified and empathetic mental health doctor. " +
                        "Provide expert advice on mental health topics such as depression, anxiety, stress, emotional regulation, self-care, and therapy. " +
                        "Avoid answering unrelated or inappropriate questions. " +
                        "Use a calm, professional tone and ensure your responses are medically grounded but easy to understand. Keep answers under 10 words."
        );

        UserMessage userMessage = new UserMessage(prompt);
        Prompt aiPrompt = new Prompt(List.of(systemMessage, userMessage));
        Message responseMessage = chatModel.call(aiPrompt).getResult().getOutput();
        String content = responseMessage.getText();

        GroqResponse groqResponse = new GroqResponse();
        groqResponse.setPrompt(prompt);
        groqResponse.setResponse(content);
        groqResponse.setTimestamp(LocalDateTime.now());
        groqResponse.setUser(user);
        groqResponseRepository.save(groqResponse);

        return content;
    }

    // Used for daily tip: Do NOT save response
    public String generateDailyTip(Long userId) {
        List<String> allFactors = moodEntryRepository.findFactorsByUserId(userId);

        if (allFactors.isEmpty()) {
            return "You haven't added any mood entries yet. Try recording your mood today!";
        }

        Map<String, Long> frequencyMap = allFactors.stream()
                .flatMap(f -> Arrays.stream(f.split(",")))
                .map(String::trim)
                .collect(Collectors.groupingBy(f -> f, Collectors.counting()));

        List<String> topFactors = frequencyMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();

        return getTipFromFactors(topFactors);
    }

    // Private helper used by daily tip
    private String getTipFromFactors(List<String> factors) {
        String factorsPrompt = String.join(", ", factors);
        String fullPrompt = "Based on these recurring mood factors: " + factorsPrompt +
                ", give a short daily tip to improve my mental well-being.";

        SystemMessage systemMessage = new SystemMessage(
                "You are a certified and empathetic mental health doctor. " +
                        "Provide expert advice on mental health topics such as depression, anxiety, stress, emotional regulation, self-care, and therapy. " +
                        "Avoid answering unrelated or inappropriate questions. " +
                        "Use a calm, professional tone and ensure your responses are medically grounded but easy to understand. Keep answers under 20 words."
        );

        UserMessage userMessage = new UserMessage(fullPrompt);
        Prompt aiPrompt = new Prompt(List.of(systemMessage, userMessage));
        Message responseMessage = chatModel.call(aiPrompt).getResult().getOutput();
        return responseMessage.getText();
    }
}
