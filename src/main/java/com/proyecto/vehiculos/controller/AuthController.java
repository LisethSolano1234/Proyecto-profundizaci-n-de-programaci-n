package com.proyecto.vehiculos.controller;

import com.proyecto.vehiculos.dto.LoginRequest;
import com.proyecto.vehiculos.dto.LoginResponse;
import com.proyecto.vehiculos.model.Usuario;
import com.proyecto.vehiculos.repository.UsuarioRepository;
import com.proyecto.vehiculos.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(request.getUsername());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).body("{\"error\":\"Usuario no encontrado\"}");
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(401).body("{\"error\":\"Credenciales inválidas\"}");
        }

        // 🔁 Generar nueva API key dinámica
        String nuevaApiKey = UUID.randomUUID().toString();
        usuario.setApiKey(nuevaApiKey);
        usuarioRepository.save(usuario);

        // 🔐 Generar nuevo token JWT
        String token = jwtTokenUtil.generateToken(usuario.getLogin());

        return ResponseEntity.ok(new LoginResponse(token, nuevaApiKey, usuario.getRol()));
    }
}
