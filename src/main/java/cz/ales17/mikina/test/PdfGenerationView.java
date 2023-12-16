package cz.ales17.mikina.test;

import com.itextpdf.text.DocumentException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import cz.ales17.mikina.data.service.AccommodationService;
import cz.ales17.mikina.data.service.PdfService;
import cz.ales17.mikina.data.service.PdfService8;
import cz.ales17.mikina.data.service.UbyportService;
import cz.ales17.mikina.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Route(value = "pdf", layout = MainLayout.class)
@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
public class PdfGenerationView extends VerticalLayout {
    private final PdfService pdfService;
    private final UbyportService ubyportService;
    private final AccommodationService accommodationService;

    PdfService8 pdfService8;

    @Autowired
    public PdfGenerationView(PdfService pdfService, AccommodationService accommodationService, UbyportService ubyportService, PdfService8 pdfService8) {
        this.pdfService = pdfService;
        this.accommodationService = accommodationService;
        this.ubyportService = ubyportService;
        this.pdfService8 = pdfService8;
        Button generatePdfButton = new Button("Generovat PDF", event -> {
            String content = "Apartmány u Mikiny, Nové Splavy, Záhlučí 67, 36174.";
            try {
                byte[] pdfBytes = pdfService.getPdfBytes(content, accommodationService.findAllGuests());
                LocalDateTime datetime1 = LocalDateTime.now();
                DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
                String formatDateTime = datetime1.format(format);
                // Stažení PDF souboru
                StreamResource resource = new StreamResource("ubytovaci_kniha_" + formatDateTime + ".pdf", () -> new ByteArrayInputStream(pdfBytes));
                Anchor anchor = new Anchor(resource, "Stáhnout PDF");
                //anchor.getElement().setAttribute("download", true);
                anchor.setTarget("_blank");
                add(anchor);
            } catch (DocumentException e) {
                e.printStackTrace();
                Notification.show("Chyba při generování PDF", 5000, Notification.Position.MIDDLE);
            }
        });

        add(generatePdfButton);

        Button generateUnlButton = new Button("Generovat UNL", event -> {
            try {
                byte[] unlBytes = ubyportService.getUbyportStream(accommodationService.findAllForeigners()).toByteArray();
                StreamResource resource = new StreamResource("ubyport.unl", () -> new ByteArrayInputStream(unlBytes));
                Anchor anchor = new Anchor(resource, "Stáhnout UNL");
                anchor.getElement().setAttribute("download", true);
                add(anchor);
            } catch (IOException e) {
                e.printStackTrace();

            }
        });
        add(generateUnlButton);

        Button iText8 = new Button("iText8", event -> {

            try {
                LocalDateTime datetime1 = LocalDateTime.now();
                DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
                String formatDateTime = datetime1.format(format);

                byte[] pdf = pdfService8.getPdfBytes("", accommodationService.findAllGuests());
                StreamResource resource = new StreamResource("itext8_" + formatDateTime + ".pdf", () -> new ByteArrayInputStream(pdf));
                Anchor anchor = new Anchor(resource, "Stáhnout PDF");
                //anchor.getElement().setAttribute("download", true);
                anchor.setTarget("_blank");
                add(anchor);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        add(iText8);
    }
}
