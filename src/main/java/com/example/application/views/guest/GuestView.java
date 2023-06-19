package com.example.application.views.guest;

import com.example.application.data.entity.Guest;
import com.example.application.data.service.AccommodationService;
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
import jakarta.annotation.security.RolesAllowed;


@RolesAllowed("ROLE_ADMIN")
@Route(value = "guest", layout = MainLayout.class)
@PageTitle("Seznam hostů | Ubytovací systém")
public class GuestView extends VerticalLayout {
    Grid<Guest> grid = new Grid<>(Guest.class);
    TextField filterText = new TextField();
    GuestForm form;
    AccommodationService accommodationService;

    public GuestView(AccommodationService accommodationService) {
        this.accommodationService = accommodationService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());
        updateList();
        // Editor will be closed at the start
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
        // Set flex grow
        content.setFlexGrow(2, grid);
        content.setFlexGrow(3, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }
    private void configureGrid() {
        grid.addClassNames("Guest-grid");
        grid.setSizeFull();
        // When setting columns update this list
        grid.setColumns("firstName", "lastName", /*"email",*/ "birthDate", "dateArrived", "dateLeft", "idNumber" );
        // Then add a new column also here
        grid.getColumnByKey("firstName").setHeader("Jméno");
        grid.getColumnByKey("lastName").setHeader("Příjmení");
        //grid.getColumnByKey("email").setHeader("Email");
        grid.getColumnByKey("birthDate").setHeader("Datum narození");
        grid.getColumnByKey("dateArrived").setHeader("Datum příchodu");
        grid.getColumnByKey("dateLeft").setHeader("Datum odchodu");
        grid.getColumnByKey("idNumber").setHeader("Číslo dokladu");
        grid.addColumn(Guest -> Guest.getStatus().getName()).setHeader("Status");

        grid.addColumn(Guest -> Guest.getCountry().getCountryName()).setHeader("Země");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event ->
                editGuest(event.getValue()));
    }
    private void configureForm() {
        form = new GuestForm(accommodationService.findAllCountries(), accommodationService.findAllStatuses());
        form.setWidth("25em");
        form.addSaveListener(this::saveGuest);
        form.addDeleteListener(this::deleteGuest);
        form.addCloseListener(e -> closeEditor());
    }




    private void saveGuest(GuestForm.SaveEvent event) {
        accommodationService.saveGuest(event.getGuest());
        updateList();
        closeEditor();
    }

    private void deleteGuest(GuestForm.DeleteEvent event) {
        accommodationService.deleteGuest(event.getGuest());
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
        grid.setItems(accommodationService.findAllGuests(filterText.getValue()));
    }
}