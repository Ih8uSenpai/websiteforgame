package com.example.websiteforgame.filter;

import com.example.websiteforgame.service.FirebaseService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class FirebaseAuthenticationTokenFilter extends OncePerRequestFilter {

    private final FirebaseService firebaseService;

    public FirebaseAuthenticationTokenFilter(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String authToken = authHeader.substring(7);

            try {
                FirebaseToken firebaseToken = firebaseService.verifyToken(authToken);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        firebaseToken.getUid(), null, new ArrayList<>()); // Using UID for principal
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (RuntimeException e) {
                logger.error("Failed to authenticate user with token", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return; // Stop the request here with an Unauthorized status
            }
        }

        filterChain.doFilter(request, response);
    }
}
