package com.MindCare.controller;

import com.MindCare.dto.MoodEntryRequest;
import com.MindCare.entity.MoodEntry;
import com.MindCare.entity.User;
import com.MindCare.repository.MoodEntryRepository;
import com.MindCare.repository.userRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class MoodController {

    @Autowired
    private MoodEntryRepository moodRepo;

    @Autowired
    private userRepo userRepo;

    // Save mood entry
    @PostMapping("/mood")
    public String saveMood(@RequestBody MoodEntryRequest request, Principal principal) {
        User user = userRepo.findByEmail(principal.getName()).orElse(null);
        if (user == null) return "User not found";

        MoodEntry entry = new MoodEntry();
        entry.setDate(request.getDate() != null ? request.getDate() : LocalDate.now());
        entry.setMood(request.getMood());
        entry.setNote(request.getNote());
        entry.setFactors(String.join(",", request.getFactors())); // Convert list to string
        entry.setUser(user);

        moodRepo.save(entry);
        return "Mood saved successfully";
    }

    // Get all mood entries for the authenticated user
    @GetMapping("/mood")
    public List<Map<String, Object>> getUserMoodHistory(Principal principal) {
        User user = userRepo.findByEmail(principal.getName()).orElse(null);
        if (user == null) return Collections.emptyList();

        List<MoodEntry> entries = moodRepo.findByUser(user);
        return entries.stream().map(entry -> {
            Map<String, Object> data = new HashMap<>();
            data.put("id", entry.getId());
            data.put("date", entry.getDate());
            data.put("mood", entry.getMood());
            data.put("note", entry.getNote());
            data.put("factors", entry.getFactors() != null
                    ? Arrays.asList(entry.getFactors().split(","))
                    : new ArrayList<>());
            return data;
        }).toList();
    }

    // Optional: Get summary of mood counts
    @GetMapping("/mood-summary")
    public Map<String, Long> getMoodSummary(Principal principal) {
        User user = userRepo.findByEmail(principal.getName()).orElse(null);
        if (user == null) return Collections.emptyMap();

        List<MoodEntry> entries = moodRepo.findByUser(user);
        Map<String, Long> summary = new HashMap<>();
        for (MoodEntry entry : entries) {
            String mood = String.valueOf(entry.getMood());
            summary.put(mood, summary.getOrDefault(mood, 0L) + 1);
        }
        return summary;
    }
}
