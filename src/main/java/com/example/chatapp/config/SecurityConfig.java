package com.example.chatapp.config;

import com.example.chatapp.security.JwtTokenFilter;
import com.example.chatapp.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/api/auth/register", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/api/auth/username").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider, userDetailsService), UsernamePasswordAuthenticationFilter.class)
                .formLogin(formLoginConfigurer -> formLoginConfigurer
                        .loginPage("/login")
                        .defaultSuccessUrl("/chat", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logoutConfigurer -> logoutConfigurer
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
