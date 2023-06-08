package com.example.application.views;

import com.example.application.data.entity.Country;
import com.example.application.data.service.Service;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route(value = "country", layout = MainLayout.class)
@PageTitle("Seznam zemí | Ubytovací systém")
public class CountryView extends VerticalLayout {
    private final Service service;
    Grid<Country> grid = new Grid<>(Country.class);
    TextField filterText = new TextField();

    public CountryView(Service service) {
        this.service = service;
        add(getToolbar(), getCompanyList());
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filtrovat podle názvu...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(event -> filterCompanies(event.getValue()));


        Button addCompanyButton = new Button("Přidat zemi");
        var toolbar = new HorizontalLayout(filterText, addCompanyButton);
        return toolbar;
    }

    private void filterCompanies(String filter) {
        grid.setItems(service.findCompaniesByName(filter));
    }

    private Grid<Country> getCompanyList() {
        grid.setItems(service.findAllCompanies());
        grid.setColumns("countryName");
        return grid;
    }


}