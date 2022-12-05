package com.example.userservice.security;

import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeHttpRequests(authorize ->
                authorize
                        .requestMatchers("/**").permitAll()
                        .requestMatchers(new IpAddressMatcher("192.168.68.104")).permitAll()
        );
        http.authorizeHttpRequests().requestMatchers(new IpAddressMatcher("192.168.68.104"));

        http.authorizeHttpRequests().requestMatchers("/user-service/users/**").permitAll();
        http.authorizeHttpRequests().requestMatchers("/user-service/health_check/**").permitAll();
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
