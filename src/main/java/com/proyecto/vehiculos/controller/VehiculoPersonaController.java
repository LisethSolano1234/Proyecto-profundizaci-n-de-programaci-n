package com.proyecto.vehiculos.controller;

import com.proyecto.vehiculos.model.Persona;
import com.proyecto.vehiculos.model.VehiculoPersona;
import com.proyecto.vehiculos.repository.VehiculoPersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vehiculo-persona")
public class VehiculoPersonaController {

    @Autowired
    private VehiculoPersonaRepository vehiculoPersonaRepository;

    //  Listar todas las relaciones
    @GetMapping
    public ResponseEntity<List<VehiculoPersona>> listarRelaciones() {
        List<VehiculoPersona> relaciones = vehiculoPersonaRepository.findAll();
        if (relaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(relaciones);
    }

    //  Buscar relación por ID
    @GetMapping("/{id}")
    public ResponseEntity<VehiculoPersona> obtenerRelacion(@PathVariable Long id) {
        Optional<VehiculoPersona> relacion = vehiculoPersonaRepository.findById(id);
        return relacion.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //  Crear nueva relación (solo conductores)
    @PostMapping
    public ResponseEntity<?> crearRelacion(@RequestBody VehiculoPersona relacion) {
        Persona persona = relacion.getPersona();

        if (persona == null || !"C".equalsIgnoreCase(persona.getTipoPersona())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Solo se pueden asociar personas de tipo CONDUCTOR (C)");
        }

        relacion.setFechaAsociacion(LocalDate.now());
        relacion.setEstado("EA"); // Espera de aprobación por defecto

        VehiculoPersona nuevaRelacion = vehiculoPersonaRepository.save(relacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaRelacion);
    }

    //  Actualizar relación
    @PutMapping("/{id}")
    public ResponseEntity<VehiculoPersona> actualizarRelacion(
            @PathVariable Long id,
            @RequestBody VehiculoPersona relacionActualizada) {

        return vehiculoPersonaRepository.findById(id)
                .map(relacion -> {
                    relacion.setEstado(relacionActualizada.getEstado());
                    relacion.setVehiculo(relacionActualizada.getVehiculo());
                    relacion.setPersona(relacionActualizada.getPersona());
                    VehiculoPersona actualizada = vehiculoPersonaRepository.save(relacion);
                    return ResponseEntity.ok(actualizada);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //  Cambiar estado del conductor (PO, EA, RO)
    @PutMapping("/{id}/estado/{nuevoEstado}")
    public ResponseEntity<?> cambiarEstadoConductor(
            @PathVariable Long id,
            @PathVariable String nuevoEstado) {

        if (!List.of("PO", "EA", "RO").contains(nuevoEstado.toUpperCase())) {
            return ResponseEntity.badRequest().body("Estado inválido. Usa PO, EA o RO.");
        }

        return vehiculoPersonaRepository.findById(id)
                .map(relacion -> {
                    relacion.setEstado(nuevoEstado.toUpperCase());
                    vehiculoPersonaRepository.save(relacion);
                    return ResponseEntity.ok("Estado actualizado correctamente a " + nuevoEstado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //  Eliminar relación
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarRelacion(@PathVariable Long id) {
        if (vehiculoPersonaRepository.existsById(id)) {
            vehiculoPersonaRepository.deleteById(id);
            return ResponseEntity.ok("Relación eliminada correctamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Relación no encontrada");
    }
}
