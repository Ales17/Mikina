package com.example.application.views;

import com.example.application.data.entity.Company;
import com.example.application.data.service.CrmService;
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
@Route(value = "company", layout = MainLayout.class)
@PageTitle("Company List | Vaadin CRM")
public class CompanyView extends VerticalLayout {
    Grid<Company> grid = new Grid<>(Company.class);

    private final CrmService service;
    TextField filterText = new TextField()  ;
    public CompanyView(CrmService service) {
        this.service = service;
        add(getToolbar(), getCompanyList());
    }
    private Component getToolbar(){
        filterText.setPlaceholder("Filter by company name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(event -> filterCompanies(event.getValue()));


        Button addCompanyButton = new Button("Add company");
        var toolbar = new HorizontalLayout(filterText, addCompanyButton);
        return toolbar;
    }

    private void filterCompanies(String filter) {
        grid.setItems(service.findCompaniesByName(filter));
    }
    private Grid<Company> getCompanyList() {
        grid.setItems(service.findAllCompanies());
        grid.setColumns("companyName");
        return grid;
    }


}