package com.proyecto.vehiculos.controller;

import com.proyecto.vehiculos.model.Persona;
import com.proyecto.vehiculos.model.Usuario;
import com.proyecto.vehiculos.repository.PersonaRepository;
import com.proyecto.vehiculos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/personas")
public class PersonaController {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Listar todas las personas
    @GetMapping
    public List<Persona> listarPersonas() {
        return personaRepository.findAll();
    }

    // Buscar persona por ID
    @GetMapping("/{id}")
    public Optional<Persona> obtenerPersona(@PathVariable Long id) {
        return personaRepository.findById(id);
    }

    // Crear nueva persona
    @PostMapping
    public Persona crearPersona(@RequestBody Persona persona) {
        // Guardar primero la persona
        Persona nuevaPersona = personaRepository.save(persona);

        // Si la persona es tipo ADMINISTRATIVO (A), crear su usuario automáticamente
        if ("A".equalsIgnoreCase(persona.getTipoPersona())) {
            Usuario usuario = new Usuario();
            usuario.setPersona(nuevaPersona);
            usuario.setRol("ADMINISTRADOR"); // opcional, puedes usarlo luego para seguridad
            usuario.generarCredenciales();
            usuarioRepository.save(usuario);
        }

        return nuevaPersona;
    }

    // Actualizar persona existente
    @PutMapping("/{id}")
    public Persona actualizarPersona(@PathVariable Long id, @RequestBody Persona personaActualizada) {
        return personaRepository.findById(id).map(p -> {
            p.setNombre(personaActualizada.getNombre());
            p.setApellido(personaActualizada.getApellido());
            p.setCorreo(personaActualizada.getCorreo());
            p.setTelefono(personaActualizada.getTelefono());
            p.setNumeroIdentificacion(personaActualizada.getNumeroIdentificacion());
            p.setTipoIdentificacion(personaActualizada.getTipoIdentificacion());
            p.setTipoPersona(personaActualizada.getTipoPersona());
            return personaRepository.save(p);
        }).orElseGet(() -> {
            personaActualizada.setId(id);
            return personaRepository.save(personaActualizada);
        });
    }

    // Eliminar persona
    @DeleteMapping("/{id}")
    public void eliminarPersona(@PathVariable Long id) {
        personaRepository.deleteById(id);
    }
}
