package com.proyecto.vehiculos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "documento")
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipoDocumento; // Soat, Tecnomecánica, etc.
    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] archivo; // Aquí se guardará el PDF en Base64

    @ManyToOne
    @JoinColumn(name = "vehiculo_id")
    private Vehiculo vehiculo;
}
