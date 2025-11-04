package com.proyecto.vehiculos.controller;

import com.proyecto.vehiculos.model.Ruta;
import com.proyecto.vehiculos.repository.RutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rutas")
@CrossOrigin(origins = "*") // permite conexión desde el front (mapa.html)
public class RutaController {

    @Autowired
    private RutaRepository rutaRepository;

    // Crear nueva ruta
    @PostMapping
    public ResponseEntity<?> crearRuta(@RequestBody Ruta ruta) {
        try {
            Optional<Ruta> existente = rutaRepository.findByCodigo(ruta.getCodigo());
            if (existente.isPresent()) {
                return ResponseEntity.badRequest().body("Ya existe una ruta con ese código.");
            }

            Ruta nuevaRuta = rutaRepository.save(ruta);
            return ResponseEntity.ok(nuevaRuta);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(" Error al crear la ruta: " + e.getMessage());
        }
    }

    // Consultar ruta por código
    @GetMapping("/{codigo}")
    public ResponseEntity<?> obtenerPorCodigo(@PathVariable String codigo) {
        Optional<Ruta> ruta = rutaRepository.findByCodigo(codigo);
        return ruta.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Consultar todas las rutas
    @GetMapping
    public ResponseEntity<List<Ruta>> listarRutas() {
        List<Ruta> rutas = rutaRepository.findAll();
        if (rutas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rutas);
    }

    // (Opcional) Eliminar ruta
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarRuta(@PathVariable Long id) {
        if (!rutaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        rutaRepository.deleteById(id);
        return ResponseEntity.ok("Ruta eliminada correctamente");
    }
}
