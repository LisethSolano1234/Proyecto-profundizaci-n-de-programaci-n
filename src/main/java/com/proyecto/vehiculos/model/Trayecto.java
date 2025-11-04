package com.proyecto.vehiculos.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "trayecto")
@JsonIgnoreProperties({"vehiculo", "persona", "ruta", "hibernateLazyInitializer", "handler"})
public class Trayecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ruta", nullable = false)
    private Ruta ruta;  //  Relación con la entidad Ruta

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vehiculo", nullable = false)
    private Vehiculo vehiculo;

    @Column(nullable = false)
    private String ubicacion;

    @Column(name = "orden_parada", nullable = false)
    private int ordenParada;

    private Double latitud;
    private Double longitud;

    @Column(name = "login_registro", nullable = false)
    private String loginRegistro;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;
}
