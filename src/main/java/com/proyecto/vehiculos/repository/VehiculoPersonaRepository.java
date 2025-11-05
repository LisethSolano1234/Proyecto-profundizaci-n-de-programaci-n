package com.proyecto.vehiculos.repository;

import com.proyecto.vehiculos.model.VehiculoPersona;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VehiculoPersonaRepository extends JpaRepository<VehiculoPersona, Long> {
    List<VehiculoPersona> findByPersonaId(Long personaId);
}
