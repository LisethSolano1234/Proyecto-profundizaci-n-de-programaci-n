package com.proyecto.vehiculos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "persona")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, length = 2)
    private String tipoIdentificacion; // CC, CE, etc.

    @Column(nullable = false, unique = true)
    private String numeroIdentificacion;

    private String correo;
    private String telefono;

    @Column(nullable = false, length = 1)
    private String tipoPersona; // A = Administrativo, C = Conductor

    // === Campos agregados en E3, pero se dejan listos ===
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] licenciaConduccion; // Documento licencia en Base64

    private LocalDate vigenciaLicencia;
}




