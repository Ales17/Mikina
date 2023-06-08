package com.example.application.views;

import com.example.application.data.service.Service;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

@PermitAll

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard | Ubytovací systém")
public class DashboardView extends VerticalLayout {
    private final Service service;

    public DashboardView(Service service) {

        this.service = service;
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(getContactStats(), getCountriesChart());
    }
    // Gets countries of guests and returns numbers - Czech language logic - 1/2,3,4/5+
    private Component getContactStats() {
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
        service.findAllCompanies().forEach(company ->
                dataSeries.add(new DataSeriesItem(company.getCountryName(), company.getGuestCount())));
        chart.getConfiguration().setSeries(dataSeries);
        return chart;
    }


}