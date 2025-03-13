package org.example.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.authservice.model.entity.User;
import org.example.authservice.repository.UserRepository;
import org.example.authservice.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username) {
        User byUsername = userRepository.findByUsername(username);
        if (byUsername == null) {
            return ResponseEntity.notFound().build();
        }
        String token = authService.generateToken(byUsername.getUsername());
        return ResponseEntity.ok(token);
    }

}
