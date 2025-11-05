
package com.proyecto.vehiculos.repository;

import com.proyecto.vehiculos.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
    List<Persona> findByTipoPersona(String tipoPersona);
}


