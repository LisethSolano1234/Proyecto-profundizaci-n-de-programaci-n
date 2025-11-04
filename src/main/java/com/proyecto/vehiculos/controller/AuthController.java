package com.proyecto.vehiculos.controller;

import com.proyecto.vehiculos.dto.LoginRequest;
import com.proyecto.vehiculos.dto.LoginResponse;
import com.proyecto.vehiculos.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        //  Validación temporal: puedes cambiar por la de tu BD
        if ("admin".equals(request.getUsername()) && "1234".equals(request.getPassword())) {
            String token = jwtTokenUtil.generateToken(request.getUsername());
            return ResponseEntity.ok(new LoginResponse(token));
        } else {
            return ResponseEntity.status(401).body("{\"error\":\"Credenciales inválidas\"}");
        }
    }
}
