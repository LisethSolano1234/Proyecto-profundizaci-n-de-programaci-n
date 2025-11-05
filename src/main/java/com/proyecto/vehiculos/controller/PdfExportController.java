package com.proyecto.vehiculos.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.proyecto.vehiculos.model.Documento;
import com.proyecto.vehiculos.model.Vehiculo;
import com.proyecto.vehiculos.repository.DocumentoRepository;
import com.proyecto.vehiculos.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/pdf")
@CrossOrigin(origins = "*")
public class PdfExportController {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private DocumentoRepository documentoRepository;

    // =====================================================
    //  EXPORTAR VEHÍCULOS A PDF
    // =====================================================
    @GetMapping("/vehiculos")
    public ResponseEntity<byte[]> exportarVehiculosPDF() {
        try {
            List<Vehiculo> vehiculos = vehiculoRepository.findAll();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, out);
            document.open();

            // Encabezado
            Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLUE);
            Paragraph titulo = new Paragraph("Listado de Vehículos", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            Paragraph fecha = new Paragraph("Generado el: " + LocalDate.now());
            fecha.setAlignment(Element.ALIGN_RIGHT);
            document.add(fecha);

            document.add(new Paragraph("\n"));

            // Crear tabla
            PdfPTable table = new PdfPTable(9);
            table.setWidthPercentage(100);
            String[] encabezados = {"ID", "Placa", "Tipo", "Servicio", "Combustible", "Capacidad", "Color", "Modelo", "Marca"};

            for (String encabezado : encabezados) {
                PdfPCell cell = new PdfPCell(new Phrase(encabezado, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            for (Vehiculo v : vehiculos) {
                table.addCell(String.valueOf(v.getId()));
                table.addCell(v.getPlaca());
                table.addCell(v.getTipoVehiculo());
                table.addCell(v.getTipoServicio());
                table.addCell(v.getTipoCombustible());
                table.addCell(String.valueOf(v.getCapacidadPasajeros()));
                table.addCell(v.getColor());
                table.addCell(String.valueOf(v.getModelo()));
                table.addCell(v.getMarca());
            }

            document.add(table);
            document.close();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vehiculos.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(out.toByteArray());

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(("Error al generar PDF: " + e.getMessage()).getBytes());
        }
    }

    // =====================================================
    //  EXPORTAR DOCUMENTOS A PDF
    // =====================================================
    @GetMapping("/documentos")
    public ResponseEntity<byte[]> exportarDocumentosPDF() {
        try {
            List<Documento> documentos = documentoRepository.findAll();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
            Paragraph titulo = new Paragraph("Listado de Documentos", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            Paragraph fecha = new Paragraph("Generado el: " + LocalDate.now());
            fecha.setAlignment(Element.ALIGN_RIGHT);
            document.add(fecha);

            document.add(new Paragraph("\n"));

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            String[] encabezados = {"Código", "Nombre", "Tipo", "Vehículo ID", "Emisión", "Vencimiento"};

            for (String encabezado : encabezados) {
                PdfPCell cell = new PdfPCell(new Phrase(encabezado, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            for (Documento d : documentos) {
                table.addCell(d.getCodigo());
                table.addCell(d.getNombre());
                table.addCell(d.getTipoDocumento());
                table.addCell(d.getVehiculo() != null ? String.valueOf(d.getVehiculo().getId()) : "—");
                table.addCell(d.getFechaEmision() != null ? d.getFechaEmision().toString() : "—");
                table.addCell(d.getFechaVencimiento() != null ? d.getFechaVencimiento().toString() : "—");
            }

            document.add(table);
            document.close();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=documentos.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(out.toByteArray());

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(("Error al generar PDF: " + e.getMessage()).getBytes());
        }
    }
}
