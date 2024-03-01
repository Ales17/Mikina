package cz.ales17.mikina.test;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import cz.ales17.mikina.data.entity.Company;
import cz.ales17.mikina.data.service.AccommodationService;
import cz.ales17.mikina.data.service.PdfReportService;
import cz.ales17.mikina.data.service.UbyportReportService;
import cz.ales17.mikina.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * View for testing purposes
 */
@Route(value = "test", layout = MainLayout.class)
@RolesAllowed({"ROLE_ADMIN"})
public class TestView extends VerticalLayout {

    @Autowired
    public TestView(AccommodationService accommodationService, UbyportReportService ubyportReportService, PdfReportService pdfReportService) {
        Company sampleCompany = new Company();

        Button generateUnlButton = new Button("Generovat UNL", event -> {
            try {
                byte[] unlBytes = ubyportReportService.getReportBytes(sampleCompany,
                        accommodationService.findAllForeigners());
                StreamResource resource = new StreamResource("123456789012_" + "datetime" + ".unl", () -> new ByteArrayInputStream(unlBytes));
                Anchor anchor = new Anchor(resource, "Stáhnout UNL");
                anchor.getElement().setAttribute("download", true);
                add(anchor);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        add(generateUnlButton);

        Button iText8 = new Button("Generování PDF (iText8)", event -> {

            try {
                DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
                LocalDateTime now = LocalDateTime.now();
                String formatDateTime = now.format(format);
                pdfReportService.setTime(now);
                byte[] pdf = pdfReportService.getReportBytes(sampleCompany, accommodationService.findAllGuests());
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
