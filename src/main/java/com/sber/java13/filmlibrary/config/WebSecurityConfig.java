package com.sber.java13.filmlibrary.config;

import com.sber.java13.filmlibrary.service.userdetails.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;
import java.util.List;

import static com.sber.java13.filmlibrary.constants.UserRoleConstants.ADMIN;
import static com.sber.java13.filmlibrary.constants.UserRoleConstants.LIBRARIAN;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    private final List<String> RESOURCES_WHITE_LIST = List.of("/resources/**", "/js/**", "/css/**", "/swagger-ui/**", "/");
    private final List<String> FILMS_WHITE_LIST = List.of("/films");
    private final List<String> FILMS_PERMISSION_LIST = List.of("/films/add", "/films/update", "/films/delete");
    private final List<String> DIRECTORS_WHITE_LIST = List.of("/directors");
    private final List<String> DIRECTORS_PERMISSION_LIST = List.of("/directors/add", "/directors/update", "/directors/delete");
    private final List<String> USERS_WHITE_LIST = List.of("/login", "/users/registration", "/users/remember-password", "/users/change-password");
    
    public WebSecurityConfig(BCryptPasswordEncoder bCryptPasswordEncoder, CustomUserDetailsService customUserDetailsService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.customUserDetailsService = customUserDetailsService;
    }
    
    @Bean
    public HttpFirewall httpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedPercent(true);
        firewall.setAllowUrlEncodedSlash(true);
        firewall.setAllowSemicolon(true);
        firewall.setAllowedHttpMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        return firewall;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(RESOURCES_WHITE_LIST.toArray(String[]::new)).permitAll()
                        .requestMatchers(FILMS_WHITE_LIST.toArray(String[]::new)).permitAll()
                        .requestMatchers(DIRECTORS_WHITE_LIST.toArray(String[]::new)).permitAll()
                        .requestMatchers(USERS_WHITE_LIST.toArray(String[]::new)).permitAll()
                        .requestMatchers(FILMS_PERMISSION_LIST.toArray(String[]::new)).hasAnyRole(ADMIN, LIBRARIAN)
                        .requestMatchers(DIRECTORS_PERMISSION_LIST.toArray(String[]::new)).hasAnyRole(ADMIN, LIBRARIAN)
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                );
        return httpSecurity.build();
    }
    
    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
}
