package com.example.websiteforgame.controller;

import com.example.websiteforgame.service.FirebaseService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthenticationController {

    private final FirebaseService firebaseService;

    public AuthenticationController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody Map<String, String> tokenPayload) {
        try {
            String idToken = tokenPayload.get("token");
            FirebaseToken decodedToken = firebaseService.verifyToken(idToken);

            // Установка аутентификационных данных в контекст безопасности
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    decodedToken.getUid(), null, Collections.emptyList()); // Using UID for principal
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return ResponseEntity.ok().build();

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Failed to authenticate token");
        }
    }
}

