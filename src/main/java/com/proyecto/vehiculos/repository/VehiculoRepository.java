package com.proyecto.vehiculos.repository;

import com.proyecto.vehiculos.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    // Aquí podrías agregar métodos personalizados si los necesitas,
    // pero por ahora con JpaRepository ya puedes:
    // - guardar vehículos
    // - buscar todos
    // - buscar por id
    // - eliminar
}

