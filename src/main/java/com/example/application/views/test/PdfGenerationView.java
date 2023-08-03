package com.example.application.views.test;

import com.example.application.data.service.AccommodationService;
import com.example.application.views.MainLayout;
import com.example.application.data.service.PdfService;
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

@Route(value = "pdf", layout = MainLayout.class)
@AnonymousAllowed
public class PdfGenerationView extends VerticalLayout {

    private final PdfService pdfService;
    private AccommodationService accommodationService;


    @Autowired
    public PdfGenerationView(PdfService pdfService, AccommodationService accommodationService) {
        this.pdfService = pdfService;

        Button generatePdfButton = new Button("Generovat PDF", event -> {
            String content = "Apartmány u Mikiny, Nové Splavy, Záhlučí 67, 36174.";
            try {
                byte[] pdfBytes = pdfService.generatePdf(content, accommodationService.findAllGuests2());
                LocalDateTime datetime1 = LocalDateTime.now();
                DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
                String formatDateTime = datetime1.format(format);
                // Stažení PDF souboru
                StreamResource resource = new StreamResource("report_" + formatDateTime +".pdf", () -> new ByteArrayInputStream(pdfBytes));
                Anchor anchor = new Anchor(resource, "Stáhnout PDF");
                anchor.getElement().setAttribute("download", true);
                anchor.setTarget("_blank");
                add(anchor);
            } catch (DocumentException e) {
                e.printStackTrace();
                Notification.show("Chyba při generování PDF", 5000, Notification.Position.MIDDLE);
            }
        });

        add(generatePdfButton);
    }
}
