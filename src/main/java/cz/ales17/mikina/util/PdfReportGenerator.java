package cz.ales17.mikina.util;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import cz.ales17.mikina.data.model.Company;
import cz.ales17.mikina.data.model.Guest;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service using iText 8 to generate PDF
 */
@Service
public class PdfReportGenerator implements ReportGenerator {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final DateTimeFormatter printFormatter = DateTimeFormatter.ofPattern("dd. MM. yyyy HH.mm");
    @Setter
    private LocalDateTime time;

    private final float[] columnWidths = {
            1f,
            1f,
            1f,
            1f,
            1f,
            1f,
            1f,
            1.5f
    };
    @Override
    public byte[] getReportBytes(Company c, List<Guest> guests) throws Exception {
        String FONT = "arial.ttf";
        PdfFont f1 = PdfFontFactory.createFont(FONT, PdfEncodings.CP1250, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter pdfWriter = new PdfWriter(outputStream);

        PdfDocument pdf = new PdfDocument(pdfWriter);
        pdf.setDefaultPageSize(PageSize.A4.rotate());
        String buildingNumber = c.getHouseNumber();
        if(c.getRegistrationNumber() != null) {buildingNumber = String.format("%s/%s", c.getHouseNumber(), c.getRegistrationNumber());}
        String reportHeading = String.format("Evidenční kniha - %s, %s %s, %s (čas generování %s)", c.getName(), c.getStreet(), buildingNumber, c.getMunicipality(), printFormatter.format(time));

        Document doc = new Document(pdf);
        doc.setMargins(10,10,10,10);
        doc.setFont(f1);
        doc.add(new Paragraph(reportHeading));
        Table table = new Table(columnWidths).useAllAvailableWidth();

        addHeaderCells(table, "Jméno", "Příjmení", "ID", "Narození", "Příchod", "Odchod", "Národnost", "Adresa");
        addDataCells(table, guests);

        doc.add(table);
        doc.close();
        return outputStream.toByteArray();
    }
    private void addHeaderCells(Table t, String... strings) {
        for (String s : strings) {
            t.addHeaderCell(s).setHorizontalAlignment(HorizontalAlignment.CENTER);
        }
    }

    private void addDataCells(Table table, List<Guest> guests) {
        for (Guest g : guests) {
            if(g.getDateArrived() == null || g.getDateLeft() == null) {
                return;
            }
            table.addCell(g.getFirstName());
            table.addCell(g.getLastName());
            table.addCell(g.getIdNumber());
            table.addCell(formatter.format(g.getBirthDate()));
            table.addCell(formatter.format(g.getDateArrived()));
            table.addCell(formatter.format(g.getDateLeft()));
            table.addCell(g.getNationality().getTitle());
            table.addCell(g.getAddress());
        }
    }


}
