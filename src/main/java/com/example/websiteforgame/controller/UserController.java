package com.example.websiteforgame.controller;

import com.example.websiteforgame.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping()
public class UserController {

    @Autowired
    private FirebaseService firebaseService;

    @PostMapping("/change-username")
    public void changeUsername(@RequestBody Map<String, String> payload) {
        String uid = payload.get("uid");
        String username = payload.get("username");

        if (uid == null || username == null) {
            throw new IllegalArgumentException("UID and username must be provided");
        }

        firebaseService.changeUsername(uid, username, firebaseService.getSiteUsersRef().child(uid));
    }
}
