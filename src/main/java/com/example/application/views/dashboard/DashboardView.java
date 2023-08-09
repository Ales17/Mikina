package com.example.application.views.dashboard;

import com.example.application.data.service.AccommodationService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

/**
 * DashboardView shows statistics about the guests.
 */
@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
@Route(value = "", layout = MainLayout.class)
@PageTitle("Hlavní panel | Ubytovací systém")
public class DashboardView extends VerticalLayout {
    private final AccommodationService accommodationService;

    public DashboardView(AccommodationService accommodationService) {

        this.accommodationService = accommodationService;
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        //      add(getStats(), getCountriesChart());
    }


 /*   private Component getStats() {
        String guestWord;
        switch (accommodationService.countGuests()) {
            case 1:
                guestWord = "host";
                break;
            case 2, 3, 4:
                guestWord = "hosté";
                break;
            default:
                guestWord = "hostů";
        }
        Span stats = new Span(accommodationService.countGuests() + " " + guestWord);
        stats.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.Margin.Top.MEDIUM);
        return stats;
    }

    private Chart getCountriesChart() {
        Chart chart = new Chart(ChartType.PIE);
        DataSeries dataSeries = new DataSeries();
        accommodationService.findGuestsCountries().forEach(country ->
                dataSeries.add(new DataSeriesItem(country.getCountryName(), country.getGuestCount()))
        );
        chart.getConfiguration().setSeries(dataSeries);
        return chart;
    }
*/

}