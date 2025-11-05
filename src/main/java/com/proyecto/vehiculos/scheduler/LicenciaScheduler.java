package com.proyecto.vehiculos.scheduler;

import com.proyecto.vehiculos.model.Persona;
import com.proyecto.vehiculos.model.VehiculoPersona;
import com.proyecto.vehiculos.repository.PersonaRepository;
import com.proyecto.vehiculos.repository.VehiculoPersonaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 *  Tarea programada: Verificar licencias de conductores cada 2 minutos.
 * Si la licencia está vencida, se cambia el estado del conductor a "RO" (Restringido para Operar)
 * en la tabla vehiculo_persona.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LicenciaScheduler {

    private final PersonaRepository personaRepository;
    private final VehiculoPersonaRepository vehiculoPersonaRepository;

    @Transactional
    @Scheduled(fixedRate = 120000) // Cada 2 minutos (120000 ms)
    public void verificarLicenciasVencidas() {
        log.info(" Ejecutando tarea: Verificación de licencias de conductores...");

        // Buscar todos los conductores (tipo_persona = 'C')
        List<Persona> conductores = personaRepository.findByTipoPersona("C");

        for (Persona conductor : conductores) {
            if (conductor.getVigenciaLicencia() == null) continue;

            if (conductor.getVigenciaLicencia().isBefore(LocalDate.now())) {
                log.warn("⚠ Licencia vencida para conductor: {} {}", conductor.getNombre(), conductor.getApellido());

                // Cambiar estado a "RO" en la relación VehiculoPersona
                List<VehiculoPersona> relaciones = vehiculoPersonaRepository.findByPersonaId(conductor.getId());
                for (VehiculoPersona vp : relaciones) {
                    if (!vp.getEstado().equals("RO")) {
                        vp.setEstado("RO");
                        vehiculoPersonaRepository.save(vp);
                        log.info(" Estado actualizado a 'RO' para vehículo asociado ID: {}", vp.getVehiculo().getId());
                    }
                }
            }
        }
        log.info(" Verificación de licencias completada.");
    }
}
