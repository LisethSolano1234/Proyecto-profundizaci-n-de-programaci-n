package com.proyecto.vehiculos.dto;

public class VehiculoDTO {
    private String placa;
    private String marca;
    private String tipoVehiculo;

    public VehiculoDTO() {}

    public VehiculoDTO(String placa, String marca, String tipoVehiculo) {
        this.placa = placa;
        this.marca = marca;
        this.tipoVehiculo = tipoVehiculo;
    }

    // Getters y setters
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getTipoVehiculo() { return tipoVehiculo; }
    public void setTipoVehiculo(String tipoVehiculo) { this.tipoVehiculo = tipoVehiculo; }
}
