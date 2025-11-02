package com.proyecto.vehiculos.repository;

import com.proyecto.vehiculos.model.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Long> {

    // Buscar ruta por su código
    Optional<Ruta> findByCodigo(String codigo);
}
