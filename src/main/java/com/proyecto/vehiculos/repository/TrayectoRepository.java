package com.proyecto.vehiculos.repository;

import com.proyecto.vehiculos.model.Trayecto;
import com.proyecto.vehiculos.model.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrayectoRepository extends JpaRepository<Trayecto, Long> {
    List<Trayecto> findByRutaOrderByOrdenParadaAsc(Ruta ruta);

    List<Trayecto> findByVehiculoIdOrderByOrdenParadaAsc(Long idVehiculo);
}
