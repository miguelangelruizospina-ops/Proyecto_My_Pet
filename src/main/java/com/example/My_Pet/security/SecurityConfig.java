// Configuración de seguridad y autorización de la aplicación.

// Paquete donde se encuentra la configuración de seguridad.

package com.example.My_Pet.security;

// Librerías necesarias para configurar Spring Security.

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

// Define esta clase como configuración de Spring Security.

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // Configura el almacenamiento de la información de seguridad en la sesión.
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

                // Configura dónde se almacena la información del usuario autenticado.
                .securityContext(context -> context
                        .securityContextRepository(securityContextRepository))

                // Configura la protección CSRF para las solicitudes.
                .csrf(csrf -> csrf
                        .csrfTokenRepository(
                                CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(
                                new CsrfTokenRequestAttributeHandler()))

                // Define qué rutas pueden utilizarse sin autenticación
                // y cuáles requieren permisos.
                .authorizeHttpRequests(authorize -> authorize

                        // Permite el acceso sin iniciar sesión a estas rutas.
                        .requestMatchers(
                                "/api/usuario/registro",
                                "/api/usuario/login",
                                "/api/usuario/cambiar-contrasena",
                                "/api/usuario/csrf",
                                "/error"
                        ).permitAll()

                        // Permite consultar los servicios sin autenticación.
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/servicios/**"
                        ).permitAll()

                        // Solo permite a los administradores acceder a estas rutas.
                        .requestMatchers(
                                "/api/administradores/**"
                        ).hasRole("ADMINISTRADOR")

                        // Las demás rutas de la API requieren autenticación.
                        .requestMatchers("/api/**").authenticated()

                        // Permite las solicitudes GET generales.
                        .requestMatchers(
                                HttpMethod.GET,
                                "/**"
                        ).permitAll()

                        // Cualquier otra solicitud requiere autenticación.
                        .anyRequest().authenticated())

                // Desactiva el formulario de inicio de sesión de Spring Security.
                .formLogin(AbstractHttpConfigurer::disable)

                // Desactiva la autenticación HTTP básica.
                .httpBasic(AbstractHttpConfigurer::disable)

                // Desactiva el cierre de sesión predeterminado de Spring Security.
                .logout(AbstractHttpConfigurer::disable)

                .build();
    }
}