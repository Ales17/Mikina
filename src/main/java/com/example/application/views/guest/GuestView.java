package com.example.application.views.guest;

import com.example.application.data.entity.Guest;
import com.example.application.data.service.Service;
import com.example.application.views.MainLayout;
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
@Route(value = "guest", layout = MainLayout.class)
@PageTitle("Seznam hostů | Ubytovací systém")
public class GuestView extends VerticalLayout {
    Grid<Guest> grid = new Grid<>(Guest.class);
    TextField filterText = new TextField();
    GuestForm form;
    Service service;

    public GuestView(Service service) {
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
        addGuestButton.addClickListener(click -> addGuest());
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
        grid.setColumns("firstName", "lastName", "email", "birthDate", "dateArrived", "dateLeft" );
        grid.getColumnByKey("firstName").setHeader("Jméno");
        grid.getColumnByKey("lastName").setHeader("Příjmení");
        grid.getColumnByKey("email").setHeader("Email");
        grid.getColumnByKey("birthDate").setHeader("Datum narození");
        grid.getColumnByKey("dateArrived").setHeader("Datum příchodu");
        grid.getColumnByKey("dateLeft").setHeader("Datum odchodu");
        grid.addColumn(Guest -> Guest.getStatus().getName()).setHeader("Status");
        grid.addColumn(Guest -> Guest.getCountry().getCountryName()).setHeader("Země");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event ->
                editGuest(event.getValue()));
    }
    private void configureForm() {
        form = new GuestForm(service.findAllCountries(), service.findAllStatuses());
        form.setWidth("25em");
        form.addSaveListener(this::saveGuest);
        form.addDeleteListener(this::deleteGuest);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveGuest(GuestForm.SaveEvent event) {
        service.saveGuest(event.getGuest());
        updateList();
        closeEditor();
    }

    private void deleteGuest(GuestForm.DeleteEvent event) {
        service.deleteGuest(event.getGuest());
        updateList();
        closeEditor();
    }

    private void editGuest(Guest guest) {
        if (guest == null) {
            closeEditor();
        } else {
            form.setGuest(guest);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setGuest(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addGuest() {
        grid.asSingleSelect().clear();
        editGuest(new Guest());
    }


    private void updateList() {
        grid.setItems(service.findAllGuests(filterText.getValue()));
    }
}