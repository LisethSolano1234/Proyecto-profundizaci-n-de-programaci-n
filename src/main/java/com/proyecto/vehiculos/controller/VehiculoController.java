package com.proyecto.vehiculos.controller;
import com.proyecto.vehiculos.repository.VehiculoRepository;
import com.proyecto.vehiculos.model.Vehiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/vehiculos")
public class VehiculoController {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    // 1. Listar todos los vehículos
    @GetMapping
    public List<Vehiculo> listarVehiculos() {
        return vehiculoRepository.findAll();
    }

    // 2. Buscar vehículo por ID

    @GetMapping("/{id}")
    public Optional<Vehiculo> obtenerVehiculo(@PathVariable Long id) {
        return vehiculoRepository.findById(id);
    }

    // 3. Crear un nuevo vehículo
    @PostMapping
    public Vehiculo crearVehiculo(@RequestBody Vehiculo vehiculo) {
        return vehiculoRepository.save(vehiculo);
    }

    // 4. Actualizar un vehículo existente
    @PutMapping("/{id}")
    public Vehiculo actualizarVehiculo(@PathVariable Long id, @RequestBody Vehiculo vehiculoActualizado) {
        return vehiculoRepository.findById(id).map(v -> {
            v.setMarca(vehiculoActualizado.getMarca());
            v.setModelo(vehiculoActualizado.getModelo());
            v.setAnio(vehiculoActualizado.getAnio());
            v.setPrecio(vehiculoActualizado.getPrecio());
            return vehiculoRepository.save(v);
        }).orElseGet(() -> {
            vehiculoActualizado.setId(id);
            return vehiculoRepository.save(vehiculoActualizado);
        });
    }

    // 5. Eliminar un vehículo por ID
    @DeleteMapping("/{id}")
    public void eliminarVehiculo(@PathVariable Long id) {
        vehiculoRepository.deleteById(id);
    }
}
