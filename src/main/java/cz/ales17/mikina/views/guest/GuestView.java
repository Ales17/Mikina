package cz.ales17.mikina.views.guest;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import cz.ales17.mikina.data.entity.Guest;
import cz.ales17.mikina.data.service.AccommodationService;
import cz.ales17.mikina.data.service.PdfReportService;
import cz.ales17.mikina.data.service.UbyportReportService;
import cz.ales17.mikina.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Set;

/**
 * GuestView shows a list of guests.
 */
@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
@Route(value = "guest", layout = MainLayout.class)
@PageTitle("Seznam hostů | Ubytovací systém")
public class GuestView extends VerticalLayout {
    // Services
    private final UbyportReportService ubyportReportService;
    private final PdfReportService pdfReportService;
    private final AccommodationService accommodationService;
    // Export dialog
    private final ExportDialog exportDialog = new ExportDialog();
    // Filtering for guests and utils
    private final TextField filterText = new TextField("Vyhledávání");
    private final DatePicker filterArrived = new DatePicker("Den příchodu");
    private final DatePicker filterLeft = new DatePicker("Den odchodu");
    private final Button openDialogBtn = new Button("Export dat", e -> exportDialog.open());
    private final Button duplicateGuestBtn = new Button("Duplikovat hosta");
    private final LocalDate currentMonthFirstDay = LocalDate.now().withDayOfMonth(1);
    private final LocalDate currentMonthLastDay = YearMonth.now().atEndOfMonth();
    private final Button addGuestButton = new Button("Přidat hosta");
    private final Button filterReset = new Button("Vymazat filtr");
    // Data rid
    private final Grid<Guest> guestGrid = new Grid<>(Guest.class);
    // Date formatting (for display and reports)
    private final FormatStyle formatStyle = FormatStyle.MEDIUM;
    private final DateTimeFormatter gridDateFormatter = DateTimeFormatter.ofLocalizedDate(formatStyle);
    private final DateTimeFormatter pdfDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
    private final DateTimeFormatter unlDateFormatter = DateTimeFormatter.ofPattern("ddMMyyHHmm");

    // Form for adding guests
    private GuestForm form;

    public GuestView(AccommodationService accommodationService, UbyportReportService ubyportReportService, PdfReportService pdfReportService) {
        this.accommodationService = accommodationService;
        this.ubyportReportService = ubyportReportService;
        this.pdfReportService = pdfReportService;
        addClassName("list-view");
        setSizeFull();
        // Configuring components
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());
        updateList();
        // Editor will be closed at the start
        closeEditor();
    }

    private void addExportButtons() {
        preparePdfExport(accommodationService.searchForGuests(filterText.getValue(), filterArrived.getValue(), filterLeft.getValue(), false));
        prepareUbyportExport(accommodationService.searchForGuests(filterText.getValue(), filterArrived.getValue(), filterLeft.getValue(), true));
    }

    private void preparePdfExport(List<Guest> guests) {
        LocalDateTime now = LocalDateTime.now();
        String formatDateTime = now.format(pdfDateFormatter);

        try {
            // Generating PDF using the service
            byte[] pdfBytes = pdfReportService.getReportBytes("Apartmány u Mikiny", guests);
            // Downloading the PDF
            StreamResource resource = new StreamResource("ubytovaci-kniha_" + formatDateTime + ".pdf", () -> new ByteArrayInputStream(pdfBytes));
            //anchor.getElement().setAttribute("download", true);
            exportDialog.pdfBtn.setTarget("_blank");
            exportDialog.pdfBtn.setHref(resource);
            exportDialog.pdfBtn.setEnabled(true);
        } catch (Exception e) {
            exportDialog.pdfBtn.setText("Chyba při generování PDF");
            exportDialog.pdfBtn.setEnabled(false);
            throw new RuntimeException();
        }
    }

    private void prepareUbyportExport(List<Guest> foreignGuestList) {
        LocalDateTime now = LocalDateTime.now();
        String formatDateTime = now.format(unlDateFormatter);
        try {
            byte[] unlReportBytes = ubyportReportService.getReportBytes("Apartmány", foreignGuestList);
            StreamResource resource = new StreamResource("123456789012_" + formatDateTime + ".unl", () -> new ByteArrayInputStream(unlReportBytes));
            exportDialog.unlBtn.setHref(resource);
            exportDialog.unlBtn.getElement().setAttribute("download", true);
            exportDialog.unlBtn.setEnabled(true);
        } catch (Exception e) {
            exportDialog.unlBtn.setText("Chyba při generování UNL");
            exportDialog.unlBtn.setEnabled(false);
            throw new RuntimeException(e);
        }
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Jméno, příjmení...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        filterArrived.setValue(currentMonthFirstDay);
        filterArrived.addValueChangeListener(e -> updateList());

        filterLeft.setValue(currentMonthLastDay);
        filterLeft.addValueChangeListener(e -> updateList());

        // Date validation
        filterArrived.addValueChangeListener(e -> filterLeft.setMin(e.getValue()));
        filterLeft.addValueChangeListener(e -> filterArrived.setMax(e.getValue()));
        // Listeners
        addGuestButton.addClickListener(click -> addGuest());
        filterReset.addClickListener(click -> resetFilters());
        filterReset.addThemeVariants(ButtonVariant.LUMO_ERROR);

        duplicateGuestBtn.addClickListener(click -> duplicateGuest());
        duplicateGuestBtn.setEnabled(false);
        openDialogBtn.getElement().setProperty("label", "XXX");
        var toolbar = new HorizontalLayout(openDialogBtn, filterText, filterArrived, filterLeft, filterReset, addGuestButton, duplicateGuestBtn);
        toolbar.setAlignItems(Alignment.END);
        toolbar.addClassName("toolbar");
        return toolbar;
    }


    private void resetFilters() {
        filterArrived.setValue(null);
        filterLeft.setValue(null);
        filterText.setValue("");
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(guestGrid, form);
        content.setFlexGrow(2, guestGrid);
        content.setFlexGrow(4, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }


    private void configureGrid() {
        guestGrid.addClassNames("guest-grid");
        guestGrid.setSizeFull();
        // When setting columns update this list
        guestGrid.setColumns("firstName", "lastName");
        guestGrid.addColumn(new LocalDateRenderer<>(Guest::getBirthDate, () -> gridDateFormatter)).setHeader("Datum narození");
        guestGrid.addColumn(Guest::getIdNumber).setHeader("Číslo dokladu");
        guestGrid.addColumn(Guest::getNationality).setHeader("Stát");
        guestGrid.addColumn(new LocalDateRenderer<>(Guest::getDateArrived, () -> gridDateFormatter)).setHeader("Datum příchodu");
        guestGrid.addColumn(new LocalDateRenderer<>(Guest::getDateLeft, () -> gridDateFormatter)).setHeader("Datum odchodu");
        guestGrid.getColumnByKey("firstName").setHeader("Jméno");
        guestGrid.getColumnByKey("lastName").setHeader("Příjmení");
        guestGrid.getColumns().forEach(col -> col.setAutoWidth(true));
        guestGrid.asSingleSelect().addValueChangeListener(event -> editGuest(event.getValue()));
    }

    private void configureForm() {
        form = new GuestForm(accommodationService, ubyportReportService.getCountryList());
        form.setWidth("30em");
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
            duplicateGuestBtn.setEnabled(false);
            closeEditor();
        } else {
            duplicateGuestBtn.setEnabled(true);
            form.setGuest(guest);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void duplicateGuest() {
        Guest selectedGuest = null;
        Set<Guest> selected = guestGrid.getSelectedItems();
        int i = 1;
        for (Guest g : selected) {
            if (i == 1) selectedGuest = g;
            i++;
        }
        form.setGuest(null);
        if (selectedGuest != null) {
            accommodationService.duplicateGuest(selectedGuest);
            System.out.println("selectedGuest != null -> duplicated");
            guestGrid.setItems(accommodationService.searchForGuests(filterText.getValue(), filterArrived.getValue(), filterLeft.getValue(), false));
            addExportButtons();
        } else {
            System.out.println("selectedGuest == null");
        }
    }

    private void closeEditor() {
        form.setGuest(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addGuest() {
        guestGrid.asSingleSelect().clear();
        editGuest(new Guest());
    }


    private void updateList() {
        guestGrid.setItems(accommodationService.searchForGuests(filterText.getValue(), filterArrived.getValue(), filterLeft.getValue(), false));
        addExportButtons();
    }
}