package com.proyecto.vehiculos.controller;

import com.proyecto.vehiculos.model.Trayecto;
import com.proyecto.vehiculos.model.Persona;
import com.proyecto.vehiculos.model.Vehiculo;
import com.proyecto.vehiculos.repository.PersonaRepository;
import com.proyecto.vehiculos.repository.TrayectoRepository;
import com.proyecto.vehiculos.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trayectos")
public class TrayectoController {

    @Autowired
    private TrayectoRepository trayectoRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    // 1️⃣ Registrar trayecto
    @PostMapping
    public ResponseEntity<?> crearTrayecto(@RequestBody Trayecto trayecto) {
        try {
            // Buscar conductor y vehículo
            Persona conductor = personaRepository.findById(trayecto.getPersona().getId())
                    .orElseThrow(() -> new RuntimeException("Conductor no encontrado"));

            Vehiculo vehiculo = vehiculoRepository.findById(trayecto.getVehiculo().getId())
                    .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

            // Validar tipo de persona
            if (!"C".equalsIgnoreCase(conductor.getTipoPersona())) {
                return ResponseEntity.badRequest().body(" La persona no es un conductor (tipo C).");
            }

            // Validar documentos habilitados
            boolean documentosHabilitados = vehiculo.getDocumentos().stream()
                    .allMatch(d -> "HABILITADO".equalsIgnoreCase(d.getEstadoDocumento()));

            if (!documentosHabilitados) {
                return ResponseEntity.badRequest().body(" El vehículo no tiene todos los documentos habilitados.");
            }

            trayecto.setPersona(conductor);
            trayecto.setVehiculo(vehiculo);

            trayectoRepository.save(trayecto);
            return ResponseEntity.ok(" Trayecto registrado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error al registrar trayecto: " + e.getMessage());
        }
    }

    // Consultar trayectos por código de ruta
    @GetMapping("/ruta/{codigoRuta}")
    public ResponseEntity<List<Trayecto>> obtenerPorCodigoRuta(@PathVariable String codigoRuta) {
        List<Trayecto> trayectos = trayectoRepository.findByCodigoRutaOrderByOrdenParadaAsc(codigoRuta);
        return ResponseEntity.ok(trayectos);
    }

    // Consultar rutas por número de identificación del conductor
    @GetMapping("/conductor/{numeroIdentificacion}")
    public ResponseEntity<List<String>> obtenerRutasPorConductor(@PathVariable String numeroIdentificacion) {
        List<String> rutas = trayectoRepository.findRutasPorConductor(numeroIdentificacion);
        return ResponseEntity.ok(rutas);
    }

    //  Consultar rutas por placa del vehículo
    @GetMapping("/vehiculo/{placa}")
    public ResponseEntity<List<String>> obtenerRutasPorVehiculo(@PathVariable String placa) {
        List<String> rutas = trayectoRepository.findRutasPorVehiculo(placa);
        return ResponseEntity.ok(rutas);
    }

    // Consultar trayectos con vehículo o conductor no habilitado
    @GetMapping("/no-habilitados")
    public ResponseEntity<List<Trayecto>> obtenerTrayectosNoHabilitados() {
        List<Trayecto> trayectos = trayectoRepository.findTrayectosNoHabilitados();
        return ResponseEntity.ok(trayectos);
    }
}
