package com.example.application.views.country;

import com.example.application.data.entity.Country;
import com.example.application.data.service.Service;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route(value = "country", layout = MainLayout.class)
/*@PageTitle("Seznam zemí | Ubytovací systém")
public class CountryView extends VerticalLayout {
    Service service;
    Grid<Country> grid = new Grid<>(Country.class);
    TextField filterText = new TextField();
   // CountryForm form;

    public CountryView(Service service) {
        this.service = service;
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());
        //updateList();
        //closeEditor();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid*//*, form*//*);
        content.setFlexGrow(2, grid);
       // content.setFlexGrow(1, form);
        content.setSizeFull();
        return content;
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

    private void configureGrid() {
        grid.setItems(service.findAllCountries());
        grid.getColumnByKey("countryName").setHeader("Název země");
        //grid.getColumnByKey("countryCode").setHeader("Kód země (ISO)");
    }

    private void configureForm() {
        *//*form = new CountryForm();
        form.setWidth("25em");
        form.addSaveListener(this::saveCountry);
        form.addDeleteListener(this::deleteCountry);
        form.addCloseListener(e -> closeEditor());*//*
        System.out.println("CONF FORM");
    }

    private void deleteCountry(CountryForm.DeleteEvent saveEvent) {
        service.deleteCountry(saveEvent.getCountry());
        updateList();
        closeEditor();
    }

    private void saveCountry(CountryForm.SaveEvent event) {
        service.saveCountry(event.getCountry());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
   *//*     form.setCountry(null);
        form.setVisible(false);*//*
    }


    private void updateList() {
        grid.setItems(service.findAllCountries());
    }
}*/public class CountryView extends VerticalLayout {
    Grid<Country> grid = new Grid<>(Country.class);
    TextField filterText = new TextField();
    CountryForm form;
    Service service;

    public CountryView(Service service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }
    private Component getToolbar() {
        filterText.setPlaceholder("Filtrovat dle jména...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        Button addGuestButton = new Button("Přidat kontakt");
        //addGuestButton.addClickListener(click -> addGuest());
        var toolbar = new HorizontalLayout(filterText, addGuestButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }
    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }
    private void configureGrid() {
        grid.addClassNames("Guest-grid");
        grid.setSizeFull();
        grid.setColumns("countryName" , "countryCode");
        grid.getColumnByKey("countryName").setHeader("Název země");
        grid.getColumnByKey("countryCode").setHeader("Kód země (ISO)");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
       grid.asSingleSelect().addValueChangeListener(event -> editCountry(event.getValue()));
         //      editGuest(event.getValue()));
    }
    private void configureForm() {
     form = new CountryForm();
        form.addClassNames("Guest-form");
        form.setWidth("25em");

       form.setWidth("25em");
       form.addSaveListener(this::saveCountry);
        form.addDeleteListener(this::deleteCountry);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveCountry(CountryForm.SaveEvent event) {
        service.saveCountry(event.getCountry());
        updateList();
        closeEditor();
    }

    private void deleteCountry(CountryForm.DeleteEvent event) {
        service.deleteCountry(event.getCountry());
        updateList();
        closeEditor();
    }

    private void editCountry(Country guest) {
        if (guest == null) {
            closeEditor();
        } else {
            form.setCountry(guest);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setCountry(null);
        form.setVisible(false);
        removeClassName("editing");
    }

   private void addCountry() {
        grid.asSingleSelect().clear();
        editCountry(new Country());
    }


    private void updateList() {
        grid.setItems(service.fincCountriesByName(filterText.getValue()));
    }
}