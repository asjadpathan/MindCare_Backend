package com.MindCare.controller;

import com.MindCare.entity.Community;
import com.MindCare.entity.User;
import com.MindCare.repository.CommunityRepository;
import com.MindCare.repository.userRepo;
import com.MindCare.service.CommunitySuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt; // ✅ Correct Jwt
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/communities")
@CrossOrigin(origins = "http://localhost:5173") // Adjust for your frontend port
public class CommunityController {

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private CommunitySuggestionService suggestionService;

    @Autowired
    private userRepo userRepository;

    private final CommunitySuggestionService communityService;

    @GetMapping
    public List<Map<String, Object>> getAllCommunities(@AuthenticationPrincipal Jwt principal) {
        String email = principal.getSubject();
        User user = userRepository.findByEmail(email).orElseThrow();

        return communityRepository.findAll().stream().map(comm -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", comm.getId());
            map.put("name", comm.getName());
            map.put("members", comm.getMembers());
            map.put("category", comm.getCategory());
            map.put("description", comm.getDescription());
            map.put("lastActivity", comm.getLastActivity());
            map.put("color", comm.getColor());
            map.put("isJoined", comm.getUsers().contains(user));
            return map;
        }).collect(Collectors.toList());
    }

    @PutMapping("/{id}/join")
    public ResponseEntity<?> joinCommunity(@PathVariable Long id, @AuthenticationPrincipal Jwt principal) {
        String email = principal.getSubject();
        User user = userRepository.findByEmail(email).orElseThrow();
        Community community = communityRepository.findById(id).orElseThrow();

        if (!user.getJoinedCommunities().contains(community)) {
            user.getJoinedCommunities().add(community);
            community.getUsers().add(user);
            community.setMembers(community.getMembers() + 1);
            userRepository.save(user);
            communityRepository.save(community);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("id", community.getId());
        map.put("name", community.getName());
        map.put("members", community.getMembers());
        map.put("category", community.getCategory());
        map.put("description", community.getDescription());
        map.put("lastActivity", community.getLastActivity());
        map.put("color", community.getColor());
        map.put("isJoined", true);

        return ResponseEntity.ok(map);
    }
    @GetMapping("/joined")
    public List<Map<String, Object>> getJoinedCommunities(@AuthenticationPrincipal Jwt principal) {
        String email = principal.getSubject();
        User user = userRepository.findByEmail(email).orElseThrow();

        return user.getJoinedCommunities().stream().map(comm -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", comm.getId());
            map.put("name", comm.getName());
            map.put("members", comm.getMembers());
            map.put("category", comm.getCategory());
            map.put("description", comm.getDescription());
            map.put("lastActivity", comm.getLastActivity());
            map.put("color", comm.getColor());
            return map;
        }).collect(Collectors.toList());
    }
    @GetMapping("/suggested")
    public ResponseEntity<?> getSuggestedCommunities(@AuthenticationPrincipal Jwt principal) {
        String email = principal.getSubject();
        User user = userRepository.findByEmail(email).orElseThrow();

        List<Community> suggestions = suggestionService.suggestCommunities(user.getId());

        List<Map<String, Object>> result = suggestions.stream().map(comm -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", comm.getId());
            map.put("name", comm.getName());
            map.put("members", comm.getMembers());
            map.put("category", comm.getCategory());
            map.put("description", comm.getDescription());
            map.put("lastActivity", comm.getLastActivity());
            map.put("color", comm.getColor());
            map.put("isJoined", comm.getUsers().contains(user));
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }


    public CommunityController(CommunitySuggestionService communityService) {
        this.communityService = communityService;
    }
    @GetMapping("/{id}")
    public ResponseEntity<Community> getCommunityById(@PathVariable Long id) {
        Optional<Community> community = communityRepository.findById(id);
        return community.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



    @GetMapping("/search")
    public ResponseEntity<List<Community>> searchCommunities(@RequestParam String keyword) {
        List<Community> result = communityService.searchCommunities(keyword);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}/leave")
    public ResponseEntity<?> leaveCommunity(@PathVariable Long id, @AuthenticationPrincipal Jwt principal) {
        String email = principal.getSubject();
        User user = userRepository.findByEmail(email).orElseThrow();
        Community community = communityRepository.findById(id).orElseThrow();

        if (user.getJoinedCommunities().contains(community)) {
            user.getJoinedCommunities().remove(community);
            community.getUsers().remove(user);
            community.setMembers(Math.max(community.getMembers() - 1, 0));
            userRepository.save(user);
            communityRepository.save(community);
        }

        return ResponseEntity.ok().build();
    }

}
