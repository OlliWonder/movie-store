package com.sber.java13.filmlibrary.config.jwt;

import com.sber.java13.filmlibrary.service.userdetails.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import java.util.Arrays;

import static com.sber.java13.filmlibrary.constants.SecurityConstants.*;
import static com.sber.java13.filmlibrary.constants.UserRoleConstants.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class JWTSecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final JWTTokenFilter jwtTokenFilter;

    public JWTSecurityConfig(CustomUserDetailsService customUserDetailsService,
                             JWTTokenFilter jwtTokenFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public HttpFirewall httpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
//        firewall.setAllowUrlEncodedPercent(true);
//        firewall.setAllowUrlEncodedSlash(true);
//        firewall.setAllowSemicolon(true);
        firewall.setAllowedHttpMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        return firewall;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                //Настройка http запросов - кому куда можно/нельзя
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(RESOURCES_WHITE_LIST.toArray(String[]::new)).permitAll()
                        .requestMatchers(USERS_REST_WHITE_LIST.toArray(String[]::new)).permitAll()
                        .requestMatchers("/authors/**").hasAnyRole(ADMIN, LIBRARIAN)
                        .anyRequest().authenticated()
                )
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                        authException.getMessage()))
                .and()
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //JWT Token Filter VALID or NOT
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(customUserDetailsService)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
