package com.example.My_Pet.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            SecurityContextRepository securityContextRepository) throws Exception {

        return http

                // ========================================================
                // CONTEXTO DE SEGURIDAD
                // ========================================================

                .securityContext(context -> context
                        .securityContextRepository(
                                securityContextRepository
                        )
                        .requireExplicitSave(true)
                )

                // ========================================================
                // CSRF
                // ========================================================

                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")
                        .csrfTokenRepository(
                                CookieCsrfTokenRepository.withHttpOnlyFalse()
                        )
                        .csrfTokenRequestHandler(
                                new CsrfTokenRequestAttributeHandler()
                        )
                )

                // ========================================================
                // AUTORIZACIÓN
                // ========================================================

                .authorizeHttpRequests(authorize -> authorize

                        .requestMatchers(
                                "/api/usuario/registro",
                                "/api/usuario/login",
                                "/api/usuario/cambiar-contrasena",
                                "/api/usuario/csrf",
                                "/error"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/servicios/**"
                        ).permitAll()

                        .requestMatchers(
                                "/api/publicaciones-foro/**"
                        ).permitAll()

                        .requestMatchers(
                                "/api/administradores/**"
                        ).hasRole("ADMINISTRADOR")

                        .requestMatchers(
                                "/api/comentarios-foro/**"
                        ).permitAll()

                        .requestMatchers(
                                "/api/**"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/**"
                        ).permitAll()

                        .anyRequest().authenticated()
                )

                // ========================================================
                // DESACTIVAR LOGIN NATIVO
                // ========================================================

                .formLogin(
                        AbstractHttpConfigurer::disable
                )

                .httpBasic(
                        AbstractHttpConfigurer::disable
                )

                .logout(
                        AbstractHttpConfigurer::disable
                )

                .build();
    }
}