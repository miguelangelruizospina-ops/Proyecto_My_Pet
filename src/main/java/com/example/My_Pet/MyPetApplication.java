package com.example.My_Pet;

// Librerias del spring.

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

// Clase principal de la aplicación My Pet.

@SpringBootApplication
@EnableScheduling
public class MyPetApplication {

    // Inicia la aplicación.
    public static void main(String[] args) {
        SpringApplication.run(MyPetApplication.class, args);
    }
}