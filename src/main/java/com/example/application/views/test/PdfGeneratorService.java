package com.example.application.views.test;

import com.example.application.data.entity.Guest;
import com.example.application.data.service.AccommodationService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.lowagie.text.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PdfGeneratorService {

    public byte[] generatePdf(String content, List<Guest> guests) throws DocumentException {
        // Downloaded font because the iText does not support Czech  by default
        FontFactory.register("arial.ttf");
        Font font = FontFactory.getFont("arial", BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED, 12);
        // Init doc and rotate it (landscape)
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);
        // Opening doc
        document.open();
        // Looping through the guestlist and adding information



        PdfPTable table = new PdfPTable(3); // 3 columns.
        table.setWidthPercentage(100); //Width 100%
        table.setSpacingBefore(10f); //Space before table
        table.setSpacingAfter(10f); //Space after table
        float[] columnWidths = {1f, 1f, .5f};
        table.setWidths(columnWidths);

        PdfPHeaderCell h1 = new PdfPHeaderCell();
        h1.setPhrase(new Phrase("Jméno", font));
        h1.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPHeaderCell h2 = new PdfPHeaderCell();
        h2.setHorizontalAlignment(Element.ALIGN_CENTER);
        h2.setPhrase(new Phrase("Příjmení", font));

        PdfPHeaderCell h3 = new PdfPHeaderCell();
        h3.setPhrase(new Phrase("Země", font));
        h3.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(h1);
        table.addCell(h2);
        table.addCell(h3);

        for (Guest g: guests
             ) {
            /*document.add(new Paragraph(g.getFirstName() + " " + g.getLastName(), font));*/
            PdfPCell c1 = new PdfPCell(new Paragraph(g.getFirstName(),font));
            //c1.setBorderColor(BaseColor.BLACK);
            c1.setPadding(10);
            //c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell c2 = new PdfPCell(new Paragraph(g.getLastName(),font));
            //c2.setBorderColor(BaseColor.BLACK);
            c2.setPadding(10);
            //c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell c3 = new PdfPCell(new Paragraph(g.getCountry().getCountryName(),font));
            //c3.setBorderColor(BaseColor.BLACK);
            c3.setPadding(10);
            //c3.setHorizontalAlignment(Element.ALIGN_CENTER);
            c3.setVerticalAlignment(Element.ALIGN_MIDDLE);

            table.addCell(c1);
            table.addCell(c2);
            table.addCell(c3);
        }

        //Set Column widths
        document.add(table);

        document.add(new Paragraph(content, font));

          /*  PdfPTable table = new PdfPTable(3); // 3 columns.
            table.setWidthPercentage(100); //Width 100%
            table.setSpacingBefore(10f); //Space before table
            table.setSpacingAfter(10f); //Space after table

            //Set Column widths
            float[] columnWidths = {1f, 1f, 1f};
            table.setWidths(columnWidths);

            PdfPCell cell1 = new PdfPCell(new Paragraph("Cell 1"));
            cell1.setBorderColor(BaseColor.BLUE);
            cell1.setPaddingLeft(10);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell2 = new PdfPCell(new Paragraph("Cell 2"));
            cell2.setBorderColor(BaseColor.GREEN);
            cell2.setPaddingLeft(10);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell3 = new PdfPCell(new Paragraph("Cell 3"));
            cell3.setBorderColor(BaseColor.RED);
            cell3.setPaddingLeft(10);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

            //To avoid having the cell border and the content overlap, if you are having thick cell borders
            //cell1.setUserBorderPadding(true);
            //cell2.setUserBorderPadding(true);
            //cell3.setUserBorderPadding(true);

            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);

            document.add(table);*/

            document.close();
        //System.out.println(Arrays.toString(outputStream.toByteArray()));
            return outputStream.toByteArray();
        }
    }

