package cz.ales17.mikina.views.dashboard;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import cz.ales17.mikina.data.model.Company;
import cz.ales17.mikina.data.model.UserEntity;
import cz.ales17.mikina.security.AuthenticatedUser;
import cz.ales17.mikina.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;

@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
@Route(value = "", layout = MainLayout.class)
@PageTitle("Hlavní panel")
public class DashboardView extends VerticalLayout {
    private final AuthenticatedUser authenticatedUser;
    private String companyInformation = "";

    public DashboardView(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        prepareUserCompanyInformation();
        add(getContent());
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
            Company currentCompany = user.getCompany();
            companyInformation = getFormattedCompanyInfo(currentCompany);
        }
    }

    private Component getContent() {
        H2 welcomeText = new H2("Vítejte v ubytovacím systému");
        LocalDate currentDate = LocalDate.now();
        String formattedCurrentDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(currentDate);
        Paragraph dateParagraph = new Paragraph(String.format("Dnes je %s. Spravujete ubytovací zařízení %s.", formattedCurrentDate, companyInformation));

        return new VerticalLayout(welcomeText, dateParagraph);
    }
}