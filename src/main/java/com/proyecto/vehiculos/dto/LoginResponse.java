package com.proyecto.vehiculos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String apiKey;
    private String rol;
}
