package com.proyecto.vehiculos.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehiculo")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 6)
    private String placa;

    @Column(nullable = false)
    private String tipoVehiculo; // Automóvil o Motocicleta

    @Column(nullable = false)
    private String tipoServicio; // Público o Privado

    @Column(nullable = false)
    private String tipoCombustible; // Gasolina, Gas, Diésel

    @Column(nullable = false)
    private Integer capacidadPasajeros;

    @Column(nullable = false)
    private String color; // Color del vehículo

    @Column(nullable = false)
    private Integer modelo;

    @Column(nullable = false)
    private String marca;

    private String linea;

    // === Relaciones ===
    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Documento> documentos = new ArrayList<>();

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehiculoPersona> vehiculoPersonas = new ArrayList<>();

    // === Getters y Setters ===
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getTipoVehiculo() { return tipoVehiculo; }
    public void setTipoVehiculo(String tipoVehiculo) { this.tipoVehiculo = tipoVehiculo; }

    public String getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(String tipoServicio) { this.tipoServicio = tipoServicio; }

    public String getTipoCombustible() { return tipoCombustible; }
    public void setTipoCombustible(String tipoCombustible) { this.tipoCombustible = tipoCombustible; }

    public Integer getCapacidadPasajeros() { return capacidadPasajeros; }
    public void setCapacidadPasajeros(Integer capacidadPasajeros) { this.capacidadPasajeros = capacidadPasajeros; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public Integer getModelo() { return modelo; }
    public void setModelo(Integer modelo) { this.modelo = modelo; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getLinea() { return linea; }
    public void setLinea(String linea) { this.linea = linea; }

    public List<Documento> getDocumentos() { return documentos; }
    public void setDocumentos(List<Documento> documentos) { this.documentos = documentos; }

    public List<VehiculoPersona> getVehiculoPersonas() { return vehiculoPersonas; }
    public void setVehiculoPersonas(List<VehiculoPersona> vehiculoPersonas) { this.vehiculoPersonas = vehiculoPersonas; }

    @PrePersist
    private void validarPlaca() {
        if ("Automóvil".equalsIgnoreCase(tipoVehiculo) && !placa.matches("^[A-Z]{3}\\d{3}$")) {
            throw new IllegalArgumentException("Placa inválida para automóvil. Ejemplo: ABC123");
        } else if ("Motocicleta".equalsIgnoreCase(tipoVehiculo) && !placa.matches("^[A-Z]{3}\\d{2}[A-Z]{1}$")) {
            throw new IllegalArgumentException("Placa inválida para motocicleta. Ejemplo: ABC12D");
        }
    }

}
