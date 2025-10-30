package com.proyecto.vehiculos.controller;

import com.proyecto.vehiculos.model.Persona;
import com.proyecto.vehiculos.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/personas")
public class PersonaController {

    @Autowired
    private PersonaRepository personaRepository;

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
        return personaRepository.save(persona);
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

