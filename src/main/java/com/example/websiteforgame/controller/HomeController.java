package com.example.websiteforgame.controller;

import com.example.websiteforgame.models.SiteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



import com.example.websiteforgame.service.FirebaseService;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import java.util.concurrent.atomic.AtomicBoolean;
@Controller
public class HomeController {

    @Autowired
    private final FirebaseService firebaseService;

    public HomeController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @GetMapping("/home")
    public String home(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String uid = (String) authentication.getPrincipal();
            String userEmail = firebaseService.getUserEmail(uid);
            model.addAttribute("userEmail", userEmail);
            model.addAttribute("uid", uid);

            firebaseService.getUser(uid, firebaseService.getSiteUsersRef().child(uid)).thenAccept(user -> {
                firebaseService.setUser(user);
                if (firebaseService.getUser() == null) {
                    SiteUser newUser = new SiteUser();
                    newUser.setEmail(userEmail);
                    newUser.setUid(uid);
                    firebaseService.setUser(newUser);
                    firebaseService.addUser(newUser);
                    System.out.println("USER IS NULL\n\n\n\n\n\n");
                }
                else if (firebaseService.getUser().getUsername() == null) {
                    model.addAttribute("promptForUsername", true);
                    System.out.println("USERNAME IS NULL\n\n\n\n\n\n");
                }
            }).join();
            model.addAttribute("hasUsername", firebaseService.getUser().getUsername() != null);
            return "home";
        }

        return "home";
    }
}
