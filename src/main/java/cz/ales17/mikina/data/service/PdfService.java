package cz.ales17.mikina.data.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import cz.ales17.mikina.data.entity.Guest;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfService {
    private final int FONT_SIZE = 10;
    private final String[] HEADER_CELLS = {"Jméno", "Příjmení", "Doklad", "Datum narození", "Datum příchodu", "Datum odchodu", "Země", "Adresa"};
    private final Font font;
    // Format date as wanted
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public PdfService() {
        FontFactory.register("arial.ttf");
        // iText does not support Czech by default
        font = FontFactory.getFont("arial", BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED, FONT_SIZE);
    }

    private PdfPHeaderCell getHeaderCell(String text) {
        PdfPHeaderCell hCell = new PdfPHeaderCell();
        hCell.setPhrase(new Phrase(text, font));
        hCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        hCell.setPadding(9);
        return hCell;
    }

    private PdfPCell getTableCell(String text) {
        PdfPCell tCell = new PdfPCell(new Paragraph(text, font));
        tCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tCell.setPadding(9);
        return tCell;
    }

    public byte[] generatePdf(String content, List<Guest> guests) throws DocumentException {
        // Init doc and rotate it (landscape)
        Document document = new Document(PageSize.A4.rotate());
        document.setMargins(10, 10, 10, 10);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);

        document.open();
        document.add(new Paragraph(content, font));
        // Init table with num of columns
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        float[] columnWidths = {
                1f,
                1f,
                1f,
                1f,
                1f,
                1f,
                1f,
                1.5f
        };
        table.setWidths(columnWidths);

        for (String c : HEADER_CELLS) {
            table.addCell(getHeaderCell(c));
        }
        for (Guest g : guests
        ) {
            table.addCell(getTableCell(g.getFirstName()));
            table.addCell(getTableCell(g.getLastName()));
            table.addCell(getTableCell(g.getIdNumber()));
            table.addCell(getTableCell(formatter.format(g.getBirthDate())));
            table.addCell(getTableCell(formatter.format(g.getDateArrived())));
            table.addCell(getTableCell(formatter.format(g.getDateLeft())));
            table.addCell(getTableCell(g.getNationality().getKod()));
            table.addCell(getTableCell(g.getAddress()));
        }
        document.add(table);
        document.close();
        return outputStream.toByteArray();
    }
}