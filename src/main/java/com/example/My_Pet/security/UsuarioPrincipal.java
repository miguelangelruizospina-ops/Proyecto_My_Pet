// Representa la información básica del usuario autenticado.

// Paquete donde se encuentra la clase de seguridad.

package com.example.My_Pet.security;

// Librería necesaria para permitir la serialización del objeto.

import java.io.Serializable;

// Define los datos del usuario autenticado y permite guardar el objeto como datos serializables.
public record UsuarioPrincipal(int idUsuario, String rol) implements Serializable {

}