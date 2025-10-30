package com.proyecto.vehiculos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "persona")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String tipoIdentificacion;
    private String numeroIdentificacion;
    private String correo;
    private String telefono;
    private String tipoPersona; // Natural o Jurídica
}



