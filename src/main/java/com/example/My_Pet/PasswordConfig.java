// Paquete principal de la aplicación.
package com.example.My_Pet;

// Librerías necesarias para la configuración de seguridad.
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// Configuración para la encriptación de contraseñas.
@Configuration
public class PasswordConfig {

    // Define el método de codificación de contraseñas usando BCrypt.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}