package com.igav.igav_project.Service;

import com.igav.igav_project.Model.Entity.Client.Client;
import com.igav.igav_project.Model.Entity.Client.ClientStatus;
import com.igav.igav_project.Model.Shared.ValueObjects.DocumentoIdentidad;
import com.igav.igav_project.Model.Shared.ValueObjects.NombrePersona;
import com.igav.igav_project.Model.Shared.ValueObjects.TipoDocumento;
import com.igav.igav_project.Repository.ClientRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClientReportServiceTest {

    @Test
    void generatesExcelReportWithClientData() throws Exception {
        ClientRepository repository = mock(ClientRepository.class);
        Client client = Client.builder()
                .id(17L)
                .nombrePersona(NombrePersona.natural("Ana", "García"))
                .documentoIdentidad(new DocumentoIdentidad(TipoDocumento.DNI, "12345678"))
                .email("ana@example.com")
                .telefono("+51999888777")
                .clientStatus(ClientStatus.ACTIVE)
                .build();
        when(repository.findAll()).thenReturn(List.of(client));

        byte[] report = new ClientReportService(repository).generateExcelReport();

        assertNotNull(report);
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(report))) {
            assertEquals("Clientes", workbook.getSheetAt(0).getSheetName());
            Row header = workbook.getSheetAt(0).getRow(0);
            Row data = workbook.getSheetAt(0).getRow(1);
            assertEquals("Nombre completo", header.getCell(1).getStringCellValue());
            assertEquals("Número de documento", header.getCell(3).getStringCellValue());
            assertEquals(17, data.getCell(0).getNumericCellValue());
            assertEquals("Ana García", data.getCell(1).getStringCellValue());
            assertEquals("DNI", data.getCell(2).getStringCellValue());
            assertEquals("12345678", data.getCell(3).getStringCellValue());
            assertEquals("ana@example.com", data.getCell(4).getStringCellValue());
            assertEquals("ACTIVE", data.getCell(6).getStringCellValue());
        }
    }
}
