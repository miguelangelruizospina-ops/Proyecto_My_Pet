package com.example.My_Pet.model.Modulo_2_gestion_mascota;

// Librerías para relacionar las clases Java con las tablas de la base de datos.

import jakarta.persistence.*;

// Representa la tabla "Documento" de la base de datos.

@Entity
@Table(name = "historial_medico")
public class HistorialMedico {

    // Identificador único del historial médico.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Integer idHistorial;
   
    // Información del historial médico de la mascota.

    @Column(length = 100)
    private String diagnosticos;

    @Column(name = "antecedentes_medicos", length = 200)
    private String antecedentesMedicos;

    @Column(length = 100)
    private String alergias;

    @Column(length = 300)
    private String tratamientos;

    @Column(length = 50)
    private String peso;

    
    @Column(name = "cirugias_procedimientos", length = 500)
    private String cirugiasProcedimientos;

    
    @Column(length = 500)
    private String vacunacion;

    
    @Column(name = "consultas_atenciones", length = 1000)
    private String consultasAtenciones;

    @Column(name = "enfermedades_actuales", length = 1000)
    private String enfermedadesActuales;


    // Relación entre el historial médico y la mascota.

    @ManyToOne
    @JoinColumn(
        name = "id_mascota",
        referencedColumnName = "id_mascota",
        nullable = false
    )
    private Mascota mascota;

    // Constructor vacío requerido para crear la entidad historial medico.

    public HistorialMedico() {
    }

    // Constructor para crear un nuevo historial médico con sus datos.

    public HistorialMedico(
            Integer idHistorial,
            String diagnosticos,
            String antecedentesMedicos,
            String alergias,
            String tratamientos,
            String peso,
            String cirugiasProcedimientos,
            String vacunacion,
            String consultasAtenciones,
            String enfermedadesActuales,
            Mascota mascota) {

        this.idHistorial = idHistorial;
        this.diagnosticos = diagnosticos;
        this.antecedentesMedicos = antecedentesMedicos;
        this.alergias = alergias;
        this.tratamientos = tratamientos;
        this.peso = peso;
        this.cirugiasProcedimientos = cirugiasProcedimientos;
        this.vacunacion = vacunacion;
        this.consultasAtenciones = consultasAtenciones;
        this.enfermedadesActuales = enfermedadesActuales;
        this.mascota = mascota;
    }

    // Getters y setters de la información del historial médico de la mascota.

    public Integer getIdHistorial() {
        return idHistorial;
    }
    public void setIdHistorial(Integer idHistorial) {
        this.idHistorial = idHistorial;
    }
    public String getDiagnosticos() {
        return diagnosticos;
    }
    public void setDiagnosticos(String diagnosticos) {
        this.diagnosticos = diagnosticos;
    }
    public String getAntecedentesMedicos() {
        return antecedentesMedicos;
    }
    public void setAntecedentesMedicos(String antecedentesMedicos) {
        this.antecedentesMedicos = antecedentesMedicos;
    }
    public String getAlergias() {
        return alergias;
    }
    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }
    public String getTratamientos() {
        return tratamientos;
    }
    public void setTratamientos(String tratamientos) {
        this.tratamientos = tratamientos;
    }
    public String getPeso() {
        return peso;
    }
    public void setPeso(String peso) {
        this.peso = peso;
    }
    public String getCirugiasProcedimientos() {
        return cirugiasProcedimientos;
    }
    public void setCirugiasProcedimientos(String cirugiasProcedimientos) {
        this.cirugiasProcedimientos = cirugiasProcedimientos;
    }
    public String getVacunacion() {
        return vacunacion;
    }
    public void setVacunacion(String vacunacion) {
        this.vacunacion = vacunacion;
    }
    public String getConsultasAtenciones() {
        return consultasAtenciones;
    }
    public void setConsultasAtenciones(String consultasAtenciones) {
        this.consultasAtenciones = consultasAtenciones;
    }
    public String getEnfermedadesActuales() {
        return enfermedadesActuales;
    }
    public void setEnfermedadesActuales(String enfermedadesActuales) {
        this.enfermedadesActuales = enfermedadesActuales;
    }
    public Mascota getMascota() {
        return mascota;
    }
    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }
}
