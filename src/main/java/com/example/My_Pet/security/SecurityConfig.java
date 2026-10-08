package com.example.My_Pet.security;

// Librerias del spring.

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

// Configuración de seguridad de la aplicación.

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

 // Guarda el contexto de seguridad dentro de la sesión HTTP.

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    // Configura las reglas de seguridad y acceso de la aplicación.

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            SecurityContextRepository securityContextRepository) throws Exception {
        return http

                 // Configura el almacenamiento del contexto de seguridad en la sesión.

                .securityContext(context -> context
                        .securityContextRepository(
                                securityContextRepository
                        )
                        .requireExplicitSave(true)
                )

                // Configura la protección CSRF y el almacenamiento del token.

                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")
                        .csrfTokenRepository(
                                CookieCsrfTokenRepository.withHttpOnlyFalse()
                        )
                        .csrfTokenRequestHandler(
                                new CsrfTokenRequestAttributeHandler()
                        )
                )

                 // Define las rutas públicas y las que requieren autenticación o permisos.

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

                  // Desactiva el formulario de inicio de sesión predeterminado de Spring Security.

                .formLogin(
                        AbstractHttpConfigurer::disable
                )

                // Desactiva la autenticación HTTP básica.

                .httpBasic(
                        AbstractHttpConfigurer::disable
                )

                 // Desactiva el cierre de sesión predeterminado de Spring Security.

                .logout(
                        AbstractHttpConfigurer::disable
                )
                .build();
    }
}