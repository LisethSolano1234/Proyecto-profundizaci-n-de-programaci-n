
package com.proyecto.vehiculos.repository;

import com.proyecto.vehiculos.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    // Aquí podrías agregar consultas personalizadas si las necesitas después
}
