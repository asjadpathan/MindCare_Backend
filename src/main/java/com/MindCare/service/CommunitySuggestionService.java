package com.MindCare.service;

import com.MindCare.entity.Community;
import com.MindCare.entity.MoodEntry;
import com.MindCare.entity.User;
import com.MindCare.repository.CommunityRepository;
import com.MindCare.repository.MoodEntryRepository;
import com.MindCare.repository.userRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommunitySuggestionService {

    @Autowired
    private MoodEntryRepository moodEntryRepo;

    @Autowired
    private CommunityRepository communityRepo;

    @Autowired
    private userRepo userRepository;

    private final CommunityRepository communityRepository;

    public CommunitySuggestionService(CommunityRepository communityRepository) {
        this.communityRepository = communityRepository;
    }

    public List<Community> searchCommunities(String keyword) {
        return communityRepository.searchCommunities(keyword);
    }

    public List<Community> suggestCommunities(Long userId) {
        // Fetch user and their joined communities
        User user = userRepository.findById(userId).orElseThrow();
        Set<Community> joinedCommunities = user.getJoinedCommunities();

        List<MoodEntry> entries = moodEntryRepo.findByUserId(userId);

        // Count factors from mood entries
        Map<String, Long> factorCount = entries.stream()
                .flatMap(entry -> Arrays.stream(entry.getFactors().split(",")))
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(f -> f, Collectors.counting()));

        // Get top 3 factors
        Set<String> topFactors = factorCount.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        // Suggest communities with matching category and not already joined
        List<Community> matched = communityRepo.findAll().stream()
                .filter(c -> c.getCategory() != null &&
                        topFactors.contains(c.getCategory().toLowerCase()) &&
                        !joinedCommunities.contains(c))
                .collect(Collectors.toList());

        // Fallback: suggest 3 random communities the user hasn’t joined
        if (matched.isEmpty()) {
            List<Community> all = communityRepo.findAll().stream()
                    .filter(c -> !joinedCommunities.contains(c))
                    .collect(Collectors.toList());
            Collections.shuffle(all);
            return all.stream().limit(3).collect(Collectors.toList());
        }

        return matched;
    }
}
