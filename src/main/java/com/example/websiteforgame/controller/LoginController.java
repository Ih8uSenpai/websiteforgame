package com.example.websiteforgame.controller;

import com.example.websiteforgame.models.SiteUser;
import com.example.websiteforgame.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @Autowired
    private final FirebaseService firebaseService;

    public LoginController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }


}
