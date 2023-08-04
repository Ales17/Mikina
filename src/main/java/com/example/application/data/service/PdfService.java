package com.example.application.data.service;

import com.example.application.data.entity.Guest;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
public class PdfService {
    private final int FONT_SIZE = 10;
    private final String[] headerLabelsArr = {"Jméno", "Příjmení", "Doklad", "Datum narození", "Datum příchodu", "Datum odchodu"};
    private final List<String> headerLabels = Arrays.asList(headerLabelsArr);
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
        PdfPHeaderCell headerCell = new PdfPHeaderCell();
        headerCell.setPhrase(new Phrase(text, font));
        headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        headerCell.setPadding(10);
        return headerCell;
    }

    private PdfPCell getTableCell(String text) {
        PdfPCell tableCell = new PdfPCell(new Paragraph(text, font));
        tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableCell.setPadding(10);
        return tableCell;
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
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        //float[] columnWidths = {.8f, .8f, .4f, .3f, .3f, .3f};
        //table.setWidths(columnWidths);

        for (String cell : headerLabels) {
            table.addCell(getHeaderCell(cell));
        }

        for (Guest g : guests
        ) {
            table.addCell(getTableCell(g.getFirstName()));
            table.addCell(getTableCell(g.getLastName()));
            table.addCell(getTableCell(g.getIdNumber()));
            table.addCell(getTableCell(formatter.format(g.getBirthDate())));
            table.addCell(getTableCell(formatter.format(g.getDateArrived())));
            table.addCell(getTableCell(formatter.format(g.getDateLeft())));
        }
        document.add(table);
        document.close();
        return outputStream.toByteArray();
    }
}