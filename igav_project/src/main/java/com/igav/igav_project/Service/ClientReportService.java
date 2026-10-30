package com.igav.igav_project.Service;

import com.igav.igav_project.Model.Entity.Client.Client;
import com.igav.igav_project.Model.Shared.ValueObjects.Address;
import com.igav.igav_project.Model.Shared.ValueObjects.DocumentoIdentidad;
import com.igav.igav_project.Model.Shared.ValueObjects.NombrePersona;
import com.igav.igav_project.Repository.ClientRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class ClientReportService {

    private static final List<String> HEADERS = List.of(
            "ID",
            "Nombre completo",
            "Tipo de documento",
            "Número de documento",
            "Correo electrónico",
            "Teléfono",
            "Estado",
            "Dirección",
            "Distrito",
            "Departamento",
            "País",
            "Fecha de nacimiento");

    private final ClientRepository clientRepository;

    public ClientReportService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public byte[] generateExcelReport() throws IOException {
        List<Client> clients = clientRepository.findAll();
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Clientes");
            writeHeader(sheet.createRow(0));
            CellStyle dateStyle = createDateStyle(workbook);

            for (int index = 0; index < clients.size(); index++) {
                writeClient(sheet.createRow(index + 1), clients.get(index), dateStyle);
            }

            for (int column = 0; column < HEADERS.size(); column++) {
                sheet.autoSizeColumn(column);
            }

            workbook.write(output);
            return output.toByteArray();
        }
    }

    private void writeHeader(Row row) {
        for (int column = 0; column < HEADERS.size(); column++) {
            row.createCell(column).setCellValue(HEADERS.get(column));
        }
    }

    private void writeClient(Row row, Client client, CellStyle dateStyle) {
        setLong(row, 0, client.getId());

        NombrePersona person = client.getNombrePersona();
        setText(row, 1, person != null ? person.nombreCompleto() : null);

        DocumentoIdentidad document = client.getDocumentoIdentidad();
        setText(row, 2, document != null && document.tipo() != null ? document.tipo().name() : null);
        setText(row, 3, document != null ? document.numero() : null);
        setText(row, 4, client.getEmail());
        setText(row, 5, client.getTelefono());
        setText(row, 6, client.getClientStatus() != null ? client.getClientStatus().name() : null);

        Address address = client.getAddress();
        setText(row, 7, address != null ? address.calle() : null);
        setText(row, 8, address != null ? address.distrito() : null);
        setText(row, 9, address != null ? address.departamento() : null);
        setText(row, 10, address != null ? address.pais() : null);

        Date birthDate = client.getFechaNacimiento();
        if (birthDate != null) {
            Cell cell = row.createCell(11);
            cell.setCellValue(birthDate);
            cell.setCellStyle(dateStyle);
        }
    }

    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        CreationHelper creationHelper = workbook.getCreationHelper();
        style.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-mm-dd"));
        return style;
    }

    private void setText(Row row, int column, String value) {
        if (value != null) {
            row.createCell(column).setCellValue(value);
        }
    }

    private void setLong(Row row, int column, Long value) {
        if (value != null) {
            row.createCell(column).setCellValue(value);
        }
    }
}
