// Paquete principal de la aplicación.
package com.example.My_Pet;

// Librerías necesarias para iniciar la aplicación.
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Clase principal que inicia la aplicación MyPet.
@SpringBootApplication
public class MyPetApplication {

    // Inicia la aplicación Spring Boot.
    public static void main(String[] args) {
        SpringApplication.run(MyPetApplication.class, args);
    }

}