package com.MindCare.controller;

import com.MindCare.config.JwtUtil;
import com.MindCare.dto.LoginDto;
import com.MindCare.dto.SignupDto;
import com.MindCare.entity.User;
import com.MindCare.repository.userRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

    @Autowired
    private userRepo userRepository;

    @Autowired
    private JwtUtil jwtUtil; // Inject JwtUtil

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Email already registered")
            );
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "User created successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto login) {
        return userRepository.findByEmail(login.getEmail())
                .map(user -> {
                    if (user.getPassword().equals(login.getPassword())) {
                        String token = jwtUtil.generateToken(user.getEmail());
                        System.out.println("Generated JWT Token: " + token);
                        return ResponseEntity.ok(Map.of(
                                "success", true,
                                "token", token,
                                "user", Map.of(
                                        "id", user.getId(),
                                        "name", user.getName(),
                                        "email", user.getEmail()
                                )
                        ));
                    } else {
                        return ResponseEntity.status(401).body(Map.of(
                                "success", false,
                                "message", "Invalid password"
                        ));
                    }
                })
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of(
                        "success", false,
                        "message", "User not found"
                )));
    }
}
