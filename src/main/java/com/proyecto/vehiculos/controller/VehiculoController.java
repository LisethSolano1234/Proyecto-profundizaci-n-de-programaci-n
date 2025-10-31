package com.proyecto.vehiculos.controller;

import com.proyecto.vehiculos.model.Vehiculo;
import com.proyecto.vehiculos.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehiculos")
public class VehiculoController {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    //  Crear vehículo
    @PostMapping
    public ResponseEntity<?> crearVehiculo(@RequestBody Vehiculo vehiculo) {
        try {
            Vehiculo nuevo = vehiculoRepository.save(vehiculo);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ya existe un vehículo con la placa " + vehiculo.getPlaca());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocurrió un error al guardar el vehículo: " + e.getMessage());
        }
    }


    //  Listar todos los vehículos
    @GetMapping
    public ResponseEntity<List<Vehiculo>> listarVehiculos() {
        List<Vehiculo> vehiculos = vehiculoRepository.findAll();
        return ResponseEntity.ok(vehiculos);
    }

    //  Obtener un vehículo por ID
    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> obtenerVehiculoPorId(@PathVariable Long id) {
        return vehiculoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //  Actualizar un vehículo
    @PutMapping("/{id}")
    public ResponseEntity<Vehiculo> actualizarVehiculo(@PathVariable Long id, @RequestBody Vehiculo vehiculoActualizado) {
        return vehiculoRepository.findById(id)
                .map(v -> {
                    v.setPlaca(vehiculoActualizado.getPlaca());
                    v.setTipoVehiculo(vehiculoActualizado.getTipoVehiculo());
                    v.setTipoServicio(vehiculoActualizado.getTipoServicio());
                    v.setTipoCombustible(vehiculoActualizado.getTipoCombustible());
                    v.setCapacidadPasajeros(vehiculoActualizado.getCapacidadPasajeros());
                    v.setColor(vehiculoActualizado.getColor());
                    v.setModelo(vehiculoActualizado.getModelo());
                    v.setMarca(vehiculoActualizado.getMarca());
                    v.setLinea(vehiculoActualizado.getLinea());
                    Vehiculo actualizado = vehiculoRepository.save(v);
                    return ResponseEntity.ok(actualizado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    //  Eliminar un vehículo
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarVehiculo(@PathVariable Long id) {
        return vehiculoRepository.findById(id)
                .map(v -> {
                    vehiculoRepository.delete(v);
                    return ResponseEntity.noContent().build();
                } ).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

