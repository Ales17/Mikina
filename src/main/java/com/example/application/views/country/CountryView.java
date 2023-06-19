package com.example.application.views.country;

import com.example.application.data.entity.Country;
import com.example.application.data.service.AccommodationService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
@Route(value = "country", layout = MainLayout.class)
public class CountryView extends VerticalLayout {
    Grid<Country> grid = new Grid<>(Country.class);
    TextField filterText = new TextField();
    CountryForm form;
    AccommodationService accommodationService;

    public CountryView(AccommodationService accommodationService) {
        this.accommodationService = accommodationService;
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
        //Button addGuestButton = new Button("Přidat zemi");
        //addGuestButton.addClickListener(click -> addGuest());
        var toolbar = new HorizontalLayout(filterText/*, addGuestButton*/);
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
        accommodationService.saveCountry(event.getCountry());
        updateList();
        closeEditor();
    }

    private void deleteCountry(CountryForm.DeleteEvent event) {
        accommodationService.deleteCountry(event.getCountry());
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


    private void updateList() {
        grid.setItems(accommodationService.fincCountriesByName(filterText.getValue()));
    }
}