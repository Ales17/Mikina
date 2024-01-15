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
import cz.ales17.mikina.data.service.Pdf8ReportService;
import cz.ales17.mikina.data.service.UbyportReportService;
import cz.ales17.mikina.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Route(value = "pdf", layout = MainLayout.class)
@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
public class PdfGenerationView extends VerticalLayout {
    private final PdfService pdfService;
    private final UbyportReportService ubyportReportService;
    private final AccommodationService accommodationService;

    Pdf8ReportService pdf8ReportService;

    @Autowired
    public PdfGenerationView(PdfService pdfService, AccommodationService accommodationService, UbyportReportService ubyportReportService, Pdf8ReportService pdf8ReportService) {
        this.pdfService = pdfService;
        this.accommodationService = accommodationService;
        this.ubyportReportService = ubyportReportService;
        this.pdf8ReportService = pdf8ReportService;
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
                byte[] unlBytes = ubyportReportService.getReportBytes("Apartmány",
                        accommodationService.findAllForeigners());
                StreamResource resource = new StreamResource("123456789012_" + "datetime" + ".unl", () -> new ByteArrayInputStream(unlBytes));
                Anchor anchor = new Anchor(resource, "Stáhnout UNL");
                anchor.getElement().setAttribute("download", true);
                add(anchor);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        add(generateUnlButton);

        Button iText8 = new Button("Generování PDF (iText8)", event -> {

            try {
                LocalDateTime datetime1 = LocalDateTime.now();
                DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
                String formatDateTime = datetime1.format(format);

                byte[] pdf = pdf8ReportService.getReportBytes("", accommodationService.findAllGuests());
                StreamResource resource = new StreamResource("itext8_" + formatDateTime + ".pdf", () -> new ByteArrayInputStream(pdf));
                Anchor anchor = new Anchor(resource, "Stáhnout PDF");
                //anchor.getElement().setAttribute("download", true);
                anchor.setTarget("_blank");
                add(anchor);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

        add(iText8);
    }
}
