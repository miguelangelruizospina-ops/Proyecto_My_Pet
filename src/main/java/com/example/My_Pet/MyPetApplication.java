package com.example.My_Pet;

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