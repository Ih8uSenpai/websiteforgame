package com.example.websiteforgame.controller;

import com.example.websiteforgame.models.SiteUser;
import com.example.websiteforgame.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private FirebaseService firebaseService;


    @GetMapping
    public String getProfile(Model model, Authentication authentication) {
        String uid = (String) authentication.getPrincipal();
        if (firebaseService.getUser() == null) {
            SiteUser default_user = new SiteUser();
            default_user.setEmail(firebaseService.getUserEmail(uid));
            default_user.setUid(uid);
            default_user.setUsername(null);
            model.addAttribute("user", default_user);
        }
        firebaseService.getUser(uid, firebaseService.getSiteUsersRef().child(uid)).thenAccept(user -> {
            if (firebaseService.getUser() != null) {
                System.out.println("USER IS NOT NULL\n\n\n\n\n\n");
            }
            model.addAttribute("user", user);
        }).join();

        return "account";
    }
/*
    @PostMapping
    public String updateProfile(@ModelAttribute SiteUser user, Authentication authentication) {
        String uid = (String) authentication.getPrincipal();
        user.setUid(uid);
        firebaseService.updateUserProfile(user);
        return "redirect:/profile";
    }*/
}
