
package com.proyecto.vehiculos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;
    private String password;
    private String apiKey;
    private String rol; // Ejemplo: ADMINISTRADOR, USUARIO

    @OneToOne
    @JoinColumn(name = "persona_id")
    private Persona persona;
}

