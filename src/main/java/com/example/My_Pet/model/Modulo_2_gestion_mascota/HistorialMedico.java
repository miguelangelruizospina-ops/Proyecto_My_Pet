
// Modelo que representa el historial médico de una mascota.

package com.example.My_Pet.model.Modulo_2_gestion_mascota;

// Librería necesaria para el mapeo de la entidad y sus relaciones con la base de datos.
import jakarta.persistence.*;

// Define la clase como una entidad que será gestionada por JPA.
@Entity

// Especifica el nombre de la tabla correspondiente en la base de datos.
@Table(name = "historial_medico")
public class HistorialMedico {

    // Identificador único del historial médico.
    @Id

    // Genera automáticamente el identificador del historial.
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    // Relaciona el atributo con la columna id_historial.
    @Column(name = "id_historial")
    private Integer idHistorial;

    // Diagnósticos médicos registrados para la mascota.
    @Column(length = 100)
    private String diagnosticos;

    // Antecedentes personales de la mascota.
    @Column(name = "antecedentes_medicos", length = 200)
    private String antecedentesMedicos;

    // Alergias conocidas de la mascota.
    @Column(length = 100)
    private String alergias;

    // Medicamentos que toma actualmente la mascota.
    @Column(length = 300)
    private String tratamientos;

    // Peso registrado de la mascota.
    @Column(length = 50)
    private String peso;

    // Antecedentes quirúrgicos y procedimientos realizados.
    @Column(name = "cirugias_procedimientos", length = 500)
    private String cirugiasProcedimientos;

    // Información de vacunación de la mascota.
    @Column(length = 500)
    private String vacunacion;

    // Registro de consultas y atenciones veterinarias.
    @Column(name = "consultas_atenciones", length = 1000)
    private String consultasAtenciones;

    // Enfermedades actuales de la mascota.
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

    // Constructor vacío requerido por JPA.
    public HistorialMedico() {
    }

    // Constructor para crear un historial médico con sus datos.
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

    // Obtiene el identificador del historial médico.
    public Integer getIdHistorial() {
        return idHistorial;
    }

    // Modifica el identificador del historial médico.
    public void setIdHistorial(Integer idHistorial) {
        this.idHistorial = idHistorial;
    }

    // Obtiene los diagnósticos médicos.
    public String getDiagnosticos() {
        return diagnosticos;
    }

    // Modifica los diagnósticos médicos.
    public void setDiagnosticos(String diagnosticos) {
        this.diagnosticos = diagnosticos;
    }

    // Obtiene los antecedentes personales.
    public String getAntecedentesMedicos() {
        return antecedentesMedicos;
    }

    // Modifica los antecedentes personales.
    public void setAntecedentesMedicos(String antecedentesMedicos) {
        this.antecedentesMedicos = antecedentesMedicos;
    }

    // Obtiene las alergias de la mascota.
    public String getAlergias() {
        return alergias;
    }

    // Modifica las alergias de la mascota.
    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    // Obtiene los medicamentos o tratamientos de la mascota.
    public String getTratamientos() {
        return tratamientos;
    }

    // Modifica los medicamentos o tratamientos de la mascota.
    public void setTratamientos(String tratamientos) {
        this.tratamientos = tratamientos;
    }

    // Obtiene el peso de la mascota.
    public String getPeso() {
        return peso;
    }

    // Modifica el peso de la mascota.
    public void setPeso(String peso) {
        this.peso = peso;
    }

    // Obtiene los antecedentes quirúrgicos y procedimientos.
    public String getCirugiasProcedimientos() {
        return cirugiasProcedimientos;
    }

    // Modifica los antecedentes quirúrgicos y procedimientos.
    public void setCirugiasProcedimientos(String cirugiasProcedimientos) {
        this.cirugiasProcedimientos = cirugiasProcedimientos;
    }

    // Obtiene la información de vacunación.
    public String getVacunacion() {
        return vacunacion;
    }

    // Modifica la información de vacunación.
    public void setVacunacion(String vacunacion) {
        this.vacunacion = vacunacion;
    }

    // Obtiene las consultas y atenciones veterinarias.
    public String getConsultasAtenciones() {
        return consultasAtenciones;
    }

    // Modifica las consultas y atenciones veterinarias.
    public void setConsultasAtenciones(String consultasAtenciones) {
        this.consultasAtenciones = consultasAtenciones;
    }

    // Obtiene las enfermedades actuales de la mascota.
    public String getEnfermedadesActuales() {
        return enfermedadesActuales;
    }

    // Modifica las enfermedades actuales de la mascota.
    public void setEnfermedadesActuales(String enfermedadesActuales) {
        this.enfermedadesActuales = enfermedadesActuales;
    }

    // Obtiene la mascota asociada al historial médico.
    public Mascota getMascota() {
        return mascota;
    }

    // Modifica la mascota asociada al historial médico.
    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }
}
