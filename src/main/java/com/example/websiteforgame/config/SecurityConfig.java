package com.example.websiteforgame.config;

import com.example.websiteforgame.filter.FirebaseAuthenticationTokenFilter;
import com.example.websiteforgame.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final FirebaseService firebaseService;

    public SecurityConfig(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new FirebaseAuthenticationTokenFilter(firebaseService), UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/home")
                .and()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/download").permitAll()
                .antMatchers("/home").permitAll()
                .antMatchers("/authenticate").permitAll() // разрешаем доступ к setToken для всех
                .antMatchers("/profile").authenticated()
                .antMatchers("/css/**", "/js/**", "/images/**", "/**/*.mp4").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable();
    }

}

