package com.proyecto.vehiculos.controller;

import com.proyecto.vehiculos.model.Persona;
import com.proyecto.vehiculos.model.Usuario;
import com.proyecto.vehiculos.repository.PersonaRepository;
import com.proyecto.vehiculos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        try {
            if (usuario.getPersona() == null || usuario.getPersona().getId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Debe asociar una persona existente al usuario.");
            }

            Persona persona = personaRepository.findById(usuario.getPersona().getId())
                    .orElseThrow(() -> new RuntimeException("Persona no encontrada."));

            // Verificar si ya existe un usuario con esa persona
            boolean personaYaAsociada = usuarioRepository.findAll().stream()
                    .anyMatch(u -> u.getPersona().getId().equals(persona.getId()));

            if (personaYaAsociada) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("La persona ya tiene un usuario asociado.");
            }

            // Generar API key única
            usuario.setPersona(persona);
            usuario.setApiKey(UUID.randomUUID().toString());

            Usuario usuarioGuardado = usuarioRepository.save(usuario);
            return new ResponseEntity<>(usuarioGuardado, HttpStatus.CREATED);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el usuario: " + e.getMessage());
        }
    }


    //  Listar todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return ResponseEntity.ok(usuarios);
    }

    //  Obtener usuario por login
    @GetMapping("/{login}")
    public ResponseEntity<Usuario> obtenerUsuarioPorLogin(@PathVariable String login) {
        Usuario usuario = usuarioRepository.findById(login).orElse(null);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }

    //  Actualizar usuario
    @PutMapping("/{login}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable String login, @RequestBody Usuario usuarioActualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(login)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuarioActualizado.getPassword() != null) {
            usuarioExistente.setPassword(usuarioActualizado.getPassword());
        }
        if (usuarioActualizado.getRol() != null) {
            usuarioExistente.setRol(usuarioActualizado.getRol());
        }
        if (usuarioActualizado.getPersona() != null && usuarioActualizado.getPersona().getId() != null) {
            Persona persona = personaRepository.findById(usuarioActualizado.getPersona().getId())
                    .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
            usuarioExistente.setPersona(persona);
        }

        Usuario usuarioGuardado = usuarioRepository.save(usuarioExistente);
        return ResponseEntity.ok(usuarioGuardado);
    }

    //  Eliminar usuario
    @DeleteMapping("/{login}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable String login) {
        Usuario usuario = usuarioRepository.findById(login)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuarioRepository.delete(usuario);
        return ResponseEntity.noContent().build();
    }
    // 🔒 Endpoint protegido por API Key
    @GetMapping("/protegido")
    public ResponseEntity<?> ejemplo(@RequestHeader("x-api-key") String apiKey) {
        Usuario user = usuarioRepository.findAll().stream()
                .filter(u -> apiKey.equals(u.getApiKey()))
                .findFirst()
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(401).body(" API Key inválida o no registrada");
        }

        return ResponseEntity.ok(" Acceso permitido. Usuario: " + user.getLogin());
    }

}
