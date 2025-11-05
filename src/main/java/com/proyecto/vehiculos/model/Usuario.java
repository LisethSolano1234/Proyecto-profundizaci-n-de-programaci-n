package com.proyecto.vehiculos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    private String login;

    private String password;
    private String apiKey;
    private String rol;

    @ManyToOne
    @JoinColumn(name = "persona_id", nullable = false)
    private Persona persona;

    public void generarCredenciales() {
        if (this.persona != null && this.persona.getNumeroIdentificacion() != null) {
            this.login = this.persona.getNombre().substring(0, 1).toLowerCase()
                    + this.persona.getNumeroIdentificacion();
        } else {
            this.login = "usr_" + UUID.randomUUID().toString().substring(0, 6);
        }

        this.password = "abcd1234";
        this.apiKey = UUID.randomUUID().toString();
    }


}



