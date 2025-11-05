package com.proyecto.vehiculos.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "documento")
@JsonIgnoreProperties({"vehiculo"}) //  evita el bucle JSON y limpia el Swagger
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String tipoDocumento;

    private String tipoVehiculo;
    private String requerido;
    private String descripcion;

    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;

    @Lob
    private byte[] archivo;

    @Column(nullable = false)
    private String estadoDocumento = "EN VERIFICACION";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    @JsonIgnoreProperties({"documentos", "vehiculoPersonas"}) //  solo muestra datos básicos del vehículo
    private Vehiculo vehiculo;
}


