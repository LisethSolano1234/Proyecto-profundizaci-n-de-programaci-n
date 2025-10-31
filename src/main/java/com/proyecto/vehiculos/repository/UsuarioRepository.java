package com.proyecto.vehiculos.repository;

import com.proyecto.vehiculos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Usuario.
 * Permite realizar operaciones CRUD sobre la tabla 'usuario'.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
}
