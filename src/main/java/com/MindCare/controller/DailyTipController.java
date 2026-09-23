package com.MindCare.controller;

import com.MindCare.service.DailyTipService;
import com.MindCare.config.JwtUtilStatic;
import com.MindCare.repository.userRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api")
public class DailyTipController {

    private final DailyTipService dailyTipService;
    private final userRepo userRepository;

    public DailyTipController(DailyTipService dailyTipService, userRepo userRepository) {
        this.dailyTipService = dailyTipService;
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/daily-tip", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getDailyTip(HttpServletRequest request) {
        SseEmitter emitter = new SseEmitter();

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                emitter.send(SseEmitter.event().data("[ERROR] Missing or invalid token"));
                emitter.complete();
                return emitter;
            }

            String token = authHeader.substring(7);
            String email = JwtUtilStatic.extractEmailFromToken(token);

            Long userId = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"))
                    .getId();

            String tip = dailyTipService.generateDailyTip(userId);

            emitter.send(tip);
            emitter.send("[DONE]");
        } catch (Exception e) {
            try {
                emitter.send("[ERROR] " + e.getMessage());
            } catch (Exception ignored) {}
        } finally {
            emitter.complete();
        }

        return emitter;
    }
}
