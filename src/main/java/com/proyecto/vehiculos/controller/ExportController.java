package com.proyecto.vehiculos.controller;

import com.proyecto.vehiculos.model.Trayecto;
import com.proyecto.vehiculos.repository.TrayectoRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
@RequestMapping("/api/export")
public class ExportController {

    @Autowired
    private TrayectoRepository trayectoRepository;

    @GetMapping("/trayectos")
    public ResponseEntity<byte[]> exportarTrayectosExcel() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Trayectos");
            Row header = sheet.createRow(0);
            String[] columnas = {"ID", "Ruta", "Conductor", "Vehículo", "Ubicación", "Orden", "Latitud", "Longitud"};
            for (int i = 0; i < columnas.length; i++) {
                header.createCell(i).setCellValue(columnas[i]);
            }

            List<Trayecto> trayectos = trayectoRepository.findAll();
            int fila = 1;
            for (Trayecto t : trayectos) {
                Row row = sheet.createRow(fila++);
                row.createCell(0).setCellValue(t.getId());
                row.createCell(1).setCellValue(t.getRuta().getCodigo());
                row.createCell(2).setCellValue(t.getPersona().getNombre());
                row.createCell(3).setCellValue(t.getVehiculo().getPlaca());
                row.createCell(4).setCellValue(t.getUbicacion());
                row.createCell(5).setCellValue(t.getOrdenParada());
                row.createCell(6).setCellValue(t.getLatitud() != null ? t.getLatitud() : 0);
                row.createCell(7).setCellValue(t.getLongitud() != null ? t.getLongitud() : 0);
            }

            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=trayectos.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(outputStream.toByteArray());

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(("Error al exportar: " + e.getMessage()).getBytes());
        }
    }
}
