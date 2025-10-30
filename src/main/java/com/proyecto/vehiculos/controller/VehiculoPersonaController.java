package com.proyecto.vehiculos.controller;

import com.proyecto.vehiculos.model.VehiculoPersona;
import com.proyecto.vehiculos.repository.VehiculoPersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vehiculo-persona")
public class VehiculoPersonaController {

    @Autowired
    private VehiculoPersonaRepository vehiculoPersonaRepository;

    // Listar todas las relaciones
    @GetMapping
    public ResponseEntity<List<VehiculoPersona>> listarRelaciones() {
        List<VehiculoPersona> relaciones = vehiculoPersonaRepository.findAll();
        if (relaciones.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content si no hay registros
        }
        return ResponseEntity.ok(relaciones); // 200 OK con la lista
    }

    // Buscar una relación por ID
    @GetMapping("/{id}")
    public ResponseEntity<VehiculoPersona> obtenerRelacion(@PathVariable Long id) {
        Optional<VehiculoPersona> relacion = vehiculoPersonaRepository.findById(id);
        return relacion.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear una nueva relación vehículo-persona
    @PostMapping
    public ResponseEntity<VehiculoPersona> crearRelacion(@RequestBody VehiculoPersona relacion) {
        try {
            VehiculoPersona nuevaRelacion = vehiculoPersonaRepository.save(relacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaRelacion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Actualizar una relación existente
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

    // Eliminar una relación con validación
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarRelacion(@PathVariable Long id) {
        if (vehiculoPersonaRepository.existsById(id)) {
            vehiculoPersonaRepository.deleteById(id);
            return ResponseEntity.ok("Relación eliminada correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Relación no encontrada");
        }
    }
}
