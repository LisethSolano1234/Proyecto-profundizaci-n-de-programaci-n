package com.proyecto.vehiculos.repository;

import com.proyecto.vehiculos.model.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Documento.
 * Permite realizar operaciones CRUD sobre la tabla 'documento'.
 */
@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {
}
