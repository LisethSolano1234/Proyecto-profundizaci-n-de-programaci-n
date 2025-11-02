package com.proyecto.vehiculos.controller;

import com.proyecto.vehiculos.model.Trayecto;
import com.proyecto.vehiculos.model.Ruta;
import com.proyecto.vehiculos.repository.TrayectoRepository;
import com.proyecto.vehiculos.repository.RutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/trayectos")
@CrossOrigin(origins = "*")
public class TrayectoController {

    @Autowired
    private TrayectoRepository trayectoRepository;

    @Autowired
    private RutaRepository rutaRepository;

    // 🔹 1. Obtener todos los trayectos
    @GetMapping
    public List<Trayecto> obtenerTodos() {
        return trayectoRepository.findAll();
    }

    // 🔹 2. Obtener trayectos por código de ruta
    @GetMapping("/ruta/{codigoRuta}")
    public ResponseEntity<List<Trayecto>> obtenerPorCodigoRuta(@PathVariable String codigoRuta) {
        Optional<Ruta> ruta = rutaRepository.findByCodigo(codigoRuta);
        if (ruta.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Trayecto> trayectos = trayectoRepository.findByRutaOrderByOrdenParadaAsc(ruta.get());
        if (trayectos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(trayectos);
    }

    // 🔹 3. Guardar trayectos desde el mapa
    @PostMapping("/guardar")
    public ResponseEntity<?> guardarTrayectos(@RequestBody List<Trayecto> trayectos) {
        try {
            trayectoRepository.saveAll(trayectos);
            return ResponseEntity.ok("Trayectos guardados correctamente.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al guardar los trayectos: " + e.getMessage());
        }
    }
}
