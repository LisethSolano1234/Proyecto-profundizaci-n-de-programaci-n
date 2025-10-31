package com.proyecto.vehiculos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "trayecto")
public class Trayecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String codigoRuta; // Agrupa los trayectos de una misma ruta

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona persona; // Solo tipo C (Conductor)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vehiculo", nullable = false)
    private Vehiculo vehiculo; // Solo si documentos están habilitados

    @Column(nullable = false)
    private String ubicacion; // Ejemplo: Conservatorio del Tolima, Ibagué, Tolima

    @Column(nullable = false)
    private Integer ordenParada; // 0 = inicial, >0 = intermedia, mayor = final

    private Double latitud;
    private Double longitud;

    @Column(nullable = false)
    private String loginRegistro; // Usuario que registra el trayecto

    // Fecha en que inicia el trayecto
    private LocalDate fechaInicio;

    // Fecha en que termina el trayecto
    private LocalDate fechaFin;

}
