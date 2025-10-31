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

    // Código único generado automáticamente
    @Column(unique = true)
    private String codigo;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String tipoDocumento;

    private String tipoVehiculo;  // A, M, AM
    private String requerido;     // RA, RM, RR
    private String descripcion;

    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] archivo;

    @Column(name = "estado_documento", nullable = false)
    private String estadoDocumento; // Habilitado, En verificación, etc.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    // ⚡ Genera automáticamente código y estado antes de guardar
    @PrePersist
    private void prePersist() {
        if (codigo == null || codigo.isEmpty()) {
            String prefijo = (tipoDocumento != null && tipoDocumento.length() >= 3)
                    ? tipoDocumento.substring(0, 3).toUpperCase()
                    : "DOC";
            codigo = prefijo + "-" + System.currentTimeMillis();
        }

        if (estadoDocumento == null || estadoDocumento.isEmpty()) {
            estadoDocumento = "EN VERIFICACION";
        }
    }
}





