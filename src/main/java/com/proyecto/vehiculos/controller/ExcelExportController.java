package com.proyecto.vehiculos.controller;

import com.proyecto.vehiculos.model.Documento;
import com.proyecto.vehiculos.model.Vehiculo;
import com.proyecto.vehiculos.repository.DocumentoRepository;
import com.proyecto.vehiculos.repository.VehiculoRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/excel")
@CrossOrigin(origins = "*")
public class ExcelExportController {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private DocumentoRepository documentoRepository;

    // =====================================================
    //  EXPORTAR VEHÍCULOS A EXCEL
    // =====================================================
    @GetMapping("/vehiculos")
    public ResponseEntity<byte[]> exportarVehiculosExcel() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Vehículos");

            // Estilos de encabezado
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Encabezado
            Row header = sheet.createRow(0);
            String[] columnas = {"ID", "Placa", "Tipo Vehículo", "Servicio", "Combustible",
                    "Capacidad", "Color", "Modelo", "Marca", "Línea"};
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            // Contenido
            List<Vehiculo> vehiculos = vehiculoRepository.findAll();
            int rowNum = 1;
            for (Vehiculo v : vehiculos) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(v.getId());
                row.createCell(1).setCellValue(v.getPlaca());
                row.createCell(2).setCellValue(v.getTipoVehiculo());
                row.createCell(3).setCellValue(v.getTipoServicio());
                row.createCell(4).setCellValue(v.getTipoCombustible());
                row.createCell(5).setCellValue(v.getCapacidadPasajeros());
                row.createCell(6).setCellValue(v.getColor());
                row.createCell(7).setCellValue(v.getModelo());
                row.createCell(8).setCellValue(v.getMarca());
                row.createCell(9).setCellValue(v.getLinea());
            }

            // Ajustar ancho automático
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vehiculos.xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(out.toByteArray());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(("Error al generar Excel: " + e.getMessage()).getBytes());
        }
    }

    // =====================================================
    //  EXPORTAR DOCUMENTOS A EXCEL
    // =====================================================
    @GetMapping("/documentos")
    public ResponseEntity<byte[]> exportarDocumentosExcel() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Documentos");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row header = sheet.createRow(0);
            String[] columnas = {"Código", "Nombre", "Tipo Documento", "Vehículo ID",
                    "Fecha Emisión", "Fecha Vencimiento", "Estado"};
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            List<Documento> documentos = documentoRepository.findAll();
            int rowNum = 1;
            for (Documento d : documentos) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(d.getCodigo());
                row.createCell(1).setCellValue(d.getNombre());
                row.createCell(2).setCellValue(d.getTipoDocumento());
                row.createCell(3).setCellValue(
                        d.getVehiculo() != null ? d.getVehiculo().getId() : 0
                );
                row.createCell(4).setCellValue(
                        d.getFechaEmision() != null ? d.getFechaEmision().toString() : "—"
                );
                row.createCell(5).setCellValue(
                        d.getFechaVencimiento() != null ? d.getFechaVencimiento().toString() : "—"
                );
                row.createCell(6).setCellValue(d.getEstadoDocumento());
            }

            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=documentos.xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(out.toByteArray());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(("Error al generar Excel: " + e.getMessage()).getBytes());
        }
    }
}
