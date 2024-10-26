package cz.ales17.mikina.views.report;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import cz.ales17.mikina.data.model.Company;
import cz.ales17.mikina.data.model.Report;
import cz.ales17.mikina.data.model.UserEntity;
import cz.ales17.mikina.data.service.ReportService;
import cz.ales17.mikina.security.AuthenticatedUser;
import cz.ales17.mikina.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;

import java.io.ByteArrayInputStream;
import java.util.List;

import static cz.ales17.mikina.util.DateTimeUtil.dateTimeFormatter;


@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
@Route(value = "report", layout = MainLayout.class)
@PageTitle("Seznam reportů")
public class ReportView extends VerticalLayout {

    private final Grid<Report> reportGrid = new Grid<>(Report.class, false);
    private final ReportService reportService;
    private final AuthenticatedUser authenticatedUser;

    public ReportView(AuthenticatedUser authenticatedUser, ReportService reportService) {
        this.authenticatedUser = authenticatedUser;
        this.reportService = reportService;
        setSizeFull();
        configureGrid();
        add(getContent());
        updateList();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(reportGrid);
        content.setSizeFull();
        return content;
    }

    private void configureGrid() {
        reportGrid.addColumn(new LocalDateTimeRenderer<>(Report::getCreatedOn, () -> dateTimeFormatter)).setHeader("Datum generování");
        reportGrid.addColumn(report -> report.getType().toString()).setHeader("Typ souboru");

        reportGrid.addColumn(new ComponentRenderer<>(report -> {
            // Array of report bytes
            byte[] bytes = report.getBytes();

            StreamResource resource = new StreamResource(report.getFileName(), () -> new ByteArrayInputStream(bytes));
            resource.setCacheTime(0);
            Anchor downloadLink = new Anchor(resource, "Stáhnout");
            downloadLink.setTarget("_blank");
            return new VerticalLayout(downloadLink);

        })).setHeader("Akce");

        reportGrid.setSizeFull();
    }

    private void updateList() {
        UserEntity user = authenticatedUser.get().orElseThrow(() -> new RuntimeException("User not found"));
        Company currentUserCompany = user.getCompany();
        List<Report> reports = reportService.getReportsByCompany(currentUserCompany);
        reportGrid.setItems(reports);
    }


}
