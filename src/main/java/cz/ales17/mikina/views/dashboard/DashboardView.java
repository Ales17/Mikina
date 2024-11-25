package cz.ales17.mikina.views.dashboard;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import cz.ales17.mikina.data.model.Company;
import cz.ales17.mikina.data.model.UserEntity;
import cz.ales17.mikina.data.service.AccommodationService;
import cz.ales17.mikina.security.AuthenticatedUser;
import cz.ales17.mikina.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Optional;

@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
@Route(value = "", layout = MainLayout.class)
@PageTitle("Hlavní panel")
public class DashboardView extends VerticalLayout {
    private final AuthenticatedUser authenticatedUser;
    private final AccommodationService acccommodationService;
    private String companyInformation = "";
    private Company currentCompany;

    public DashboardView(AuthenticatedUser authenticatedUser, AccommodationService acccommodationService) {
        this.authenticatedUser = authenticatedUser;
        this.acccommodationService = acccommodationService;
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        prepareUserCompanyInformation();
        //add(getWelcomeSection(), getStatistics());
        add(welcome());
        add(stats());
    }

    private String getFormattedCompanyInfo(Company c) {
        return String.format(
                "%s, %s",
                c.getName(),
                c.getMunicipality());
    }

    private void prepareUserCompanyInformation() {
        Optional<UserEntity> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            UserEntity user = maybeUser.get();
            currentCompany = user.getCompany();
            companyInformation = getFormattedCompanyInfo(currentCompany);
        }
    }

    private List<Component> welcome() {
        H2 welcomeText = new H2("Vítejte v ubytovacím systému");
        LocalDate currentDate = LocalDate.now();
        String formattedCurrentDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(currentDate);
        Paragraph dateParagraph = new Paragraph(String.format("Dnes je %s. Spravujete ubytovací zařízení %s.", formattedCurrentDate, companyInformation));
        return List.of(welcomeText, dateParagraph);
    }

    private List<Component> stats() {
        int totalGuests = acccommodationService.totalGuestCount(currentCompany);
        H2 statsHeading = new H2("Statistika");

        Integer averageDaysCount = 999;//acccommodationService.averageDaysOfStay(currentCompany);
        Paragraph daysAvg = new Paragraph(String.format("Průměrný počet dní: %s\nCelkem hostů: %s", averageDaysCount, totalGuests));
        daysAvg.getStyle().set("white-space", "pre-line");
        return List.of(statsHeading, daysAvg);
    }
}