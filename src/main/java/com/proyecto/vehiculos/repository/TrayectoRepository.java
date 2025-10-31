package com.proyecto.vehiculos.repository;

import com.proyecto.vehiculos.model.Trayecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrayectoRepository extends JpaRepository<Trayecto, Long> {

    // Buscar trayectos por código de ruta
    List<Trayecto> findByCodigoRutaOrderByOrdenParadaAsc(String codigoRuta);

    // Buscar rutas por número de identificación del conductor
    @Query("SELECT DISTINCT t.codigoRuta FROM Trayecto t WHERE t.persona.numeroIdentificacion = ?1")
    List<String> findRutasPorConductor(String numeroIdentificacion);

    // Buscar rutas por placa del vehículo
    @Query("SELECT DISTINCT t.codigoRuta FROM Trayecto t WHERE t.vehiculo.placa = ?1")
    List<String> findRutasPorVehiculo(String placa);

    // Buscar trayectos donde el vehículo o conductor estén restringidos o documentos no habilitados
    @Query("SELECT t FROM Trayecto t " +
            "JOIN t.vehiculo v " +
            "JOIN t.persona p " +
            "JOIN v.vehiculoPersonas vp " +
            "WHERE vp.estado = 'RO' OR v.id IN (" +
            "   SELECT d.vehiculo.id FROM Documento d WHERE d.estadoDocumento <> 'HABILITADO')")
    List<Trayecto> findTrayectosNoHabilitados();
}


