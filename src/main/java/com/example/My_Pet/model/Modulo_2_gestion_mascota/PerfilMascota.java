package com.example.My_Pet.model.Modulo_2_gestion_mascota;

// Librerías para relacionar las clases Java con las tablas de la base de datos.

import jakarta.persistence.*;

// Representa la tabla "Perfil Mascota" de la base de datos.

@Entity
@Table(name = "perfil_mascota")
public class PerfilMascota {

    // Identificador unico del perfil mascota creado.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perfil_mascota")
    private Integer idPerfilMascota;

    //Informacion del perfil de usuario.

    @Column(length = 300)
    private String caracteristicas;

    @Column(length = 300)
    private String comportamiento;

    @Column(length = 300)
    private String gustos;

    @Column(name = "cuidados_especiales", length = 300)
    private String cuidadosEspeciales;

    // Relación uno a uno entre el perfil y la mascota.

    @OneToOne
    @JoinColumn(
            name = "id_mascota",
            referencedColumnName = "id_mascota",
            nullable = false,
            unique = true
    )
    private Mascota mascota;

    // Constructor vacío requerido para crear la entidad perfil de mascota

    public PerfilMascota() {
    }

    // Constructor para crear un perfil de mascota con sus datos.

    public PerfilMascota(
            Integer idPerfilMascota,
            String caracteristicas,
            String comportamiento,
            String gustos,
            String cuidadosEspeciales,
            Mascota mascota) {
        this.idPerfilMascota = idPerfilMascota;
        this.caracteristicas = caracteristicas;
        this.comportamiento = comportamiento;
        this.gustos = gustos;
        this.cuidadosEspeciales = cuidadosEspeciales;
        this.mascota = mascota;
    }

    // Getters y setters- Métodos para obtener y modificar los datos.

    public Integer getIdPerfilMascota() {
        return idPerfilMascota;
    }
    public void setIdPerfilMascota(Integer idPerfilMascota) {
        this.idPerfilMascota = idPerfilMascota;
    }
    public String getCaracteristicas() {
        return caracteristicas;
    }
    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }
    public String getComportamiento() {
        return comportamiento;
    }
    public void setComportamiento(String comportamiento) {
        this.comportamiento = comportamiento;
    }
    public String getGustos() {
        return gustos;
    }
    public void setGustos(String gustos) {
        this.gustos = gustos;
    }
    public String getCuidadosEspeciales() {
        return cuidadosEspeciales;
    }
    public void setCuidadosEspeciales(String cuidadosEspeciales) {
        this.cuidadosEspeciales = cuidadosEspeciales;
    }
    public Mascota getMascota() {
        return mascota;
    }
    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }
}