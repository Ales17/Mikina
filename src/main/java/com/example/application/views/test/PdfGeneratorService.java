package com.example.application.views.test;

import com.example.application.data.entity.Guest;
import com.example.application.data.service.AccommodationService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.lowagie.text.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PdfGeneratorService {

    public byte[] generatePdf(String content, List<Guest> guests) throws DocumentException {
        // Downloaded font because the iText does not support Czech  by default
        FontFactory.register("arial.ttf");
        Font font = FontFactory.getFont("arial", BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED, 10);
        // Init doc and rotate it (landscape)
        Document document = new Document(PageSize.A4.rotate());
        document.setMargins(10, 10, 10, 10);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);
        // Opening doc
        document.open();
        document.add(new Paragraph(content, font));
        // Init table with num of columns
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        //float[] columnWidths = {.8f, .8f, .4f, .3f, .3f, .3f};
        //table.setWidths(columnWidths);
// 1 = ALIGN_CENTER
        // 5 = ALIGN_MIDDLE
        // Header cells
        PdfPHeaderCell h1 = new PdfPHeaderCell();
        h1.setPhrase(new Phrase("Jméno", font));

        PdfPHeaderCell h2 = new PdfPHeaderCell();
        h2.setPhrase(new Phrase("Příjmení", font));

        PdfPHeaderCell h3 = new PdfPHeaderCell();
        h3.setPhrase(new Phrase("Doklad",font));

        PdfPHeaderCell h4 = new PdfPHeaderCell();
        h4.setPhrase(new Phrase("Datum narození", font));

        PdfPHeaderCell h5 = new PdfPHeaderCell();
        h5.setPhrase(new Phrase("Datum příchodu", font));

        PdfPHeaderCell h6 = new PdfPHeaderCell();
        h6.setPhrase(new Phrase("Datum příchodu", font));
        //
        h1.setVerticalAlignment(5);
        h2.setVerticalAlignment(5);
        h3.setVerticalAlignment(5);
        h4.setVerticalAlignment(5);
        h5.setVerticalAlignment(5);
        h6.setVerticalAlignment(5);

        //
        h1.setPadding(10);
        h2.setPadding(10);
        h3.setPadding(10);
        h4.setPadding(10);
        h5.setPadding(10);
        h6.setPadding(10);
        // Adding header cells
        table.addCell(h1);
        table.addCell(h2);
        table.addCell(h3);
        table.addCell(h4);
        table.addCell(h5);
        table.addCell(h6);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY");
        for (Guest g : guests
        ) {
            PdfPCell c1 = new PdfPCell(new Paragraph(g.getFirstName(), font));
            PdfPCell c2 = new PdfPCell(new Paragraph(g.getLastName(), font));
            PdfPCell c3 = new PdfPCell(new Paragraph(g.getIdNumber(),font));
            PdfPCell c4 = new PdfPCell(new Paragraph(formatter.format(g.getBirthDate()), font));
            PdfPCell c5 = new PdfPCell(new Paragraph(formatter.format(g.getDateArrived()), font));
            PdfPCell c6 = new PdfPCell(new Paragraph(formatter.format(g.getDateLeft()), font));


            c1.setVerticalAlignment(5);
            c2.setVerticalAlignment(5);
            c3.setVerticalAlignment(5);
            c4.setVerticalAlignment(5);
            c5.setVerticalAlignment(5);
            c6.setVerticalAlignment(5);

            c1.setPadding(10);
            c2.setPadding(10);
            c3.setPadding(10);
            c4.setPadding(10);
            c5.setPadding(10);
            c6.setPadding(10);

            table.addCell(c1);
            table.addCell(c2);
            table.addCell(c3);
            table.addCell(c4);
            table.addCell(c5);
            table.addCell(c6);
        }
        document.add(table);
        document.close();
        return outputStream.toByteArray();
    }
}