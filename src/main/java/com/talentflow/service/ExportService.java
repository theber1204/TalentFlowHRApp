package com.talentflow.service;

import com.talentflow.model.Candidato;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.DefaultStreamedContent;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;

@ApplicationScoped
public class ExportService {

    public StreamedContent exportarCandidatosCSV(List<Candidato> candidatos) {
        try {
            // Crear un archivo temporal
            File tempFile = File.createTempFile("candidatos", ".csv");
            tempFile.deleteOnExit();

            // Escribir los datos al archivo CSV
            try (PrintWriter writer = new PrintWriter(
                    new OutputStreamWriter(new FileOutputStream(tempFile), "UTF-8"))) {
                // Escribir encabezados
                writer.println("Nombre,Apellidos,Email,Teléfono,Empresa,Estado,Fecha Creación,Fecha Actualización");

                // Escribir datos
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                for (Candidato candidato : candidatos) {
                    writer.println(String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                            escaparCSV(candidato.getNombre()),
                            escaparCSV(candidato.getApellidos()),
                            escaparCSV(candidato.getEmail()),
                            escaparCSV(candidato.getTelefono()),
                            escaparCSV(candidato.getEmpresa()),
                            escaparCSV(candidato.getEstado()),
                            candidato.getFechaCreacion() != null ? sdf.format(candidato.getFechaCreacion()) : "",
                            candidato.getFechaActualizacion() != null ? sdf.format(candidato.getFechaActualizacion())
                                    : ""));
                }
            }

            // Crear el StreamedContent para la descarga
            InputStream stream = new FileInputStream(tempFile);
            return DefaultStreamedContent.builder()
                    .name("candidatos.csv")
                    .contentType("text/csv")
                    .stream(() -> stream)
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public StreamedContent exportarCSVGenerico(List<String> headers, List<List<String>> rows, String fileName) {
        try {
            File tempFile = File.createTempFile(fileName, ".csv");
            tempFile.deleteOnExit();

            try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(tempFile), "UTF-8"))) {
                // Escribir encabezados
                writer.println(String.join(",", headers));
                // Escribir filas
                for (List<String> row : rows) {
                    writer.println(String.join(",", row.stream().map(this::escaparCSV).toArray(String[]::new)));
                }
            }
            InputStream stream = new FileInputStream(tempFile);
            return DefaultStreamedContent.builder()
                    .name(fileName + ".csv")
                    .contentType("text/csv")
                    .stream(() -> stream)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String escaparCSV(String valor) {
        if (valor == null) {
            return "";
        }
        // Escapar comas y comillas
        valor = valor.replace("\"", "\"\"");
        if (valor.contains(",") || valor.contains("\"") || valor.contains("\n")) {
            valor = "\"" + valor + "\"";
        }
        return valor;
    }
}