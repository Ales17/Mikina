package com.example.application.views;

import com.example.application.data.service.Service;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
@Route(value = "", layout = MainLayout.class)
@PageTitle("Hlavní panel | Ubytovací systém")
public class DashboardView extends VerticalLayout {
    private final Service service;

    public DashboardView(Service service) {

        this.service = service;
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(getStats(), getCountriesChart());
    }



    private Component getStats() {
        String guestWord;
        switch (service.countGuests()) {
            case 1: guestWord = "host";
            break;
            case 2, 3, 4: guestWord = "hosté";
            break;
            default: guestWord = "hostů";
        }
        Span stats = new Span(service.countGuests() + " " + guestWord);
        stats.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.Margin.Top.MEDIUM);
        return stats;
    }

    private Chart getCountriesChart() {
        Chart chart = new Chart(ChartType.PIE);
        DataSeries dataSeries = new DataSeries();
        service.findGuestsCountries().forEach(country ->
                dataSeries.add(new DataSeriesItem(country.getCountryName(), country.getGuestCount()))
        );
        chart.getConfiguration().setSeries(dataSeries);
        return chart;
    }


}