package com.proyecto.vehiculos.scheduler;

import com.proyecto.vehiculos.model.Documento;
import com.proyecto.vehiculos.model.Persona;
import com.proyecto.vehiculos.model.Trayecto;
import com.proyecto.vehiculos.model.VehiculoPersona;
import com.proyecto.vehiculos.repository.DocumentoRepository;
import com.proyecto.vehiculos.repository.PersonaRepository;
import com.proyecto.vehiculos.repository.TrayectoRepository;
import com.proyecto.vehiculos.repository.VehiculoPersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * Clase que contiene tareas automáticas (E3)
 * - Verifica licencias de conducción vencidas
 * - Actualiza documentos vencidos
 * - Genera coordenadas simuladas en trayectos
 */
@Component
public class TareasProgramadas {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private VehiculoPersonaRepository vehiculoPersonaRepository;

    @Autowired
    private TrayectoRepository trayectoRepository;

    @Autowired
    private DocumentoRepository documentoRepository;

    /**
     * Cada 2 minutos: verificar licencias de conducción vencidas.
     * Si un conductor tiene la licencia vencida → cambia a estado “RO” (Restringido)
     */
    @Scheduled(fixedRate = 10000) // cada 10 segundos
    public void verificarLicenciasVencidas() {
        List<Persona> conductores = personaRepository.findAll().stream()
                .filter(p -> "C".equalsIgnoreCase(p.getTipoPersona()))
                .toList();

        for (Persona conductor : conductores) {
            if (conductor.getVigenciaLicencia() != null &&
                    conductor.getVigenciaLicencia().isBefore(LocalDate.now())) {

                List<VehiculoPersona> relaciones = vehiculoPersonaRepository.findAll().stream()
                        .filter(vp -> vp.getPersona().getId().equals(conductor.getId()))
                        .toList();

                for (VehiculoPersona vp : relaciones) {
                    if (!"RO".equalsIgnoreCase(vp.getEstado())) {
                        vp.setEstado("RO");
                        vehiculoPersonaRepository.save(vp);
                        System.out.println(" Conductor restringido por licencia vencida: "
                                + conductor.getNombre() + " (" + conductor.getNumeroIdentificacion() + ")");
                    }
                }
            }
        }
    }

    /**
     * Cada 2 minutos: verificar documentos vencidos de los vehículos.
     * Si la fecha de vencimiento ya pasó → estado = “VENCIDO”
     */
    @Scheduled(fixedRate = 120000)
    public void verificarDocumentosVencidos() {
        List<Documento> documentos = documentoRepository.findAll();

        for (Documento d : documentos) {
            if (d.getFechaVencimiento() != null && d.getFechaVencimiento().isBefore(LocalDate.now())) {
                if (!"VENCIDO".equalsIgnoreCase(d.getEstadoDocumento())) {
                    d.setEstadoDocumento("VENCIDO");
                    documentoRepository.save(d);
                    System.out.println(" Documento vencido actualizado: " + d.getCodigo());
                }
            }
        }
    }

    /**
     * Cada 90 segundos: asignar coordenadas falsas (simuladas) a trayectos
     * Esto solo es demostrativo.
     */
    @Scheduled(fixedRate = 90000)
    public void actualizarCoordenadasTrayectos() {
        List<Trayecto> trayectos = trayectoRepository.findAll();

        for (Trayecto t : trayectos) {
            if (t.getLatitud() == null || t.getLongitud() == null) {
                t.setLatitud(Math.random() * 5 + 4); // Latitud simulada
                t.setLongitud(Math.random() * -76 - 73); // Longitud simulada
                trayectoRepository.save(t);
                System.out.println(" Coordenadas simuladas agregadas a trayecto ID " + t.getId());
            }
        }
    }
    /**
     * Cada día a la medianoche: elimina trayectos con más de 30 días de antigüedad.
     * Esta tarea limpia la base de datos automáticamente.
     */
    @Scheduled(cron = "0 0 0 * * ?") // Ejecuta todos los días a las 00:00
    public void eliminarTrayectosAntiguos() {
        List<Trayecto> trayectos = trayectoRepository.findAll();
        LocalDate hoy = LocalDate.now();

        for (Trayecto t : trayectos) {
            if (t.getFechaInicio() != null && t.getFechaInicio().isBefore(hoy.minusDays(30))) {
                trayectoRepository.delete(t);
                System.out.println(" Trayecto eliminado por antigüedad: ID " + t.getId());
            }
        }
    }

}

