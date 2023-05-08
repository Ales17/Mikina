package com.example.application.views;

import com.example.application.data.entity.Company;
import com.example.application.data.service.CrmService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "/company")
@PageTitle("Company | Vaadin CRM")
public class CompanyView {
    Grid<Company> grid = new Grid<>(Company.class);
    CrmService service;

    public CompanyView(CrmService service) {
        this.service = service;
        configureGrid();
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setColumns("name");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));



    }
}
