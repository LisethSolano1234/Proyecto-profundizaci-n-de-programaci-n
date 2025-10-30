package com.proyecto.vehiculos.controller;

import com.proyecto.vehiculos.model.Documento;
import com.proyecto.vehiculos.repository.DocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar los documentos asociados a vehículos.
 * Permite listar, crear, actualizar y eliminar documentos.
 */
@RestController
@RequestMapping("/documentos")
public class DocumentoController {

    @Autowired
    private DocumentoRepository documentoRepository;

    /**
     * Listar todos los documentos existentes.
     */
    @GetMapping
    public ResponseEntity<List<Documento>> listarDocumentos() {
        List<Documento> documentos = documentoRepository.findAll();
        return ResponseEntity.ok(documentos);
    }

    /**
     * Buscar un documento por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Documento> obtenerDocumentoPorId(@PathVariable Long id) {
        Optional<Documento> documento = documentoRepository.findById(id);
        return documento.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Crear un nuevo documento.
     */
    @PostMapping
    public ResponseEntity<Documento> crearDocumento(@RequestBody Documento documento) {
        Documento nuevoDocumento = documentoRepository.save(documento);
        return ResponseEntity.ok(nuevoDocumento);
    }

    /**
     * Actualizar un documento existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Documento> actualizarDocumento(@PathVariable Long id, @RequestBody Documento documentoActualizado) {
        return documentoRepository.findById(id)
                .map(documento -> {
                    documento.setTipoDocumento(documentoActualizado.getTipoDocumento());
                    documento.setFechaEmision(documentoActualizado.getFechaEmision());
                    documento.setFechaVencimiento(documentoActualizado.getFechaVencimiento());
                    documento.setArchivo(documentoActualizado.getArchivo());
                    documento.setVehiculo(documentoActualizado.getVehiculo());
                    Documento actualizado = documentoRepository.save(documento);
                    return ResponseEntity.ok(actualizado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Eliminar un documento por su ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDocumento(@PathVariable Long id) {
        if (documentoRepository.existsById(id)) {
            documentoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
