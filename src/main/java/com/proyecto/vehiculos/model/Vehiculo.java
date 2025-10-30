package com.proyecto.vehiculos.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "vehiculo")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marca;
    private String modelo;
    private int anio;
    private double precio;

    // Relación con VehiculoPersona (un vehículo puede estar asociado a muchas personas)
    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehiculoPersona> vehiculoPersonas;

    // Constructor vacío (necesario para JPA)
    public Vehiculo() {}

    // Constructor con parámetros (opcional)
    public Vehiculo(String marca, String modelo, int anio, double precio) {
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.precio = precio;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public List<VehiculoPersona> getVehiculoPersonas() { return vehiculoPersonas; }
    public void setVehiculoPersonas(List<VehiculoPersona> vehiculoPersonas) { this.vehiculoPersonas = vehiculoPersonas; }
}
