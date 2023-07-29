package com.example.application.views.test;

import com.example.application.views.test.PdfGeneratorService;
import com.itextpdf.text.DocumentException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Route(value = "pdf")
@AnonymousAllowed
public class PdfGenerationView extends VerticalLayout {

    private final PdfGeneratorService pdfGeneratorService;

    @Autowired
    public PdfGenerationView(PdfGeneratorService pdfGeneratorService) {
        this.pdfGeneratorService = pdfGeneratorService;

        Button generatePdfButton = new Button("Generate PDF", event -> {
            String content = "Příliš žluťoučký kůň úpěl ďábelské ódy.";
            try {
                byte[] pdfBytes = pdfGeneratorService.generatePdf(content);

                LocalDateTime datetime1 = LocalDateTime.now();
                DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
                String formatDateTime = datetime1.format(format);
                // Stažení PDF souboru



                StreamResource resource = new StreamResource("report" + formatDateTime +".pdf", () -> new ByteArrayInputStream(pdfBytes));
                Anchor anchor = new Anchor(resource, "Click to download");
                anchor.getElement().setAttribute("download", true);
                anchor.setTarget("_blank"); // Volitelně: otevřít v nové záložce
                add(anchor);

            } catch (DocumentException e) {
                e.printStackTrace();
                Notification.show("Error generating PDF", 5000, Notification.Position.MIDDLE);
            }
        });

        add(generatePdfButton);
    }
}
