package cz.ales17.mikina.views.guest;

import com.itextpdf.text.DocumentException;
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
import cz.ales17.mikina.data.service.PdfService;
import cz.ales17.mikina.data.service.UbyportService;
import cz.ales17.mikina.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

/**
 * GuestView shows a list of guests.
 */
@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
@Route(value = "guest", layout = MainLayout.class)
@PageTitle("Seznam hostů | Ubytovací systém")
public class GuestView extends VerticalLayout {
    private final UbyportService ubyportService;
    private final PdfService pdfService;
    private final AccommodationService accommodationService;
    private final LocalDate currentMonthStart = LocalDate.now().withDayOfMonth(1);
    private final LocalDate currentMonthEnd = YearMonth.now().atEndOfMonth();
    private final Grid<Guest> grid = new Grid<>(Guest.class);
    private final TextField filterText = new TextField("Vyhledávání");
    private final DatePicker filterArrived = new DatePicker("Den příchodu");
    private final DatePicker filterLeft = new DatePicker("Den odchodu");

    private ExportDialog dialog = new ExportDialog();

    private Button openDialogBtn = new Button("Export", e -> dialog.open());
    private GuestForm form;

    public GuestView(AccommodationService accommodationService, UbyportService ubyportService, PdfService pdfService) {
        this.accommodationService = accommodationService;
        this.ubyportService = ubyportService;
        this.pdfService = pdfService;
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

    private void addExportBtns() {
        preparePdfExport(accommodationService.searchForGuests(filterText.getValue(), filterArrived.getValue(), filterLeft.getValue(), false));
        prepareUbyportExport(accommodationService.searchForGuests(filterText.getValue(), filterArrived.getValue(), filterLeft.getValue(), true));
    }

    private void preparePdfExport(List<Guest> guests) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
        String formatDateTime = LocalDateTime.now().format(formatter);
        try {
            // Generating PDF using the service
            byte[] pdfBytes = pdfService.generatePdf("Apartmány u Mikiny", guests);
            // Downloading the PDF
            StreamResource resource = new StreamResource("ubytovaci-kniha_" + formatDateTime + ".pdf", () -> new ByteArrayInputStream(pdfBytes));
            //anchor.getElement().setAttribute("download", true);
            dialog.pdfBtn.setTarget("_blank");
            dialog.pdfBtn.setHref(resource);
            dialog.pdfBtn.setEnabled(true);
        } catch (DocumentException e) {
            e.printStackTrace();
            dialog.pdfBtn.setText("Chyba při generování PDF");
            dialog.pdfBtn.setEnabled(false);
        }
    }

    private void prepareUbyportExport(List<Guest> foreignGuestList) {
        try {
            ByteArrayOutputStream ubyportExport = ubyportService.getUbyportStream(foreignGuestList);
            byte[] unlBytes = ubyportExport.toByteArray();
            StreamResource resource = new StreamResource("ubyport.unl", () -> new ByteArrayInputStream(unlBytes));
            dialog.unlBtn.setHref(resource);
            dialog.unlBtn.getElement().setAttribute("download", true);
            dialog.unlBtn.setEnabled(true);
        } catch (IOException e) {
            e.printStackTrace();
            dialog.unlBtn.setText("Chyba při generování UNL");
            dialog.unlBtn.setEnabled(false);
        }
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Jméno, příjmení...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        filterArrived.setValue(currentMonthStart);
        filterLeft.setValue(currentMonthEnd);
        filterArrived.addValueChangeListener(e -> updateList());
        filterLeft.addValueChangeListener(e -> updateList());
        // Date alidation
        filterArrived.addValueChangeListener(e -> filterLeft.setMin(e.getValue()));
        filterLeft.addValueChangeListener(e -> filterArrived.setMax(e.getValue()));
        Button addGuestButton = new Button("Přidat hosta");
        Button filterReset = new Button("Vymazat filtr");
        addGuestButton.addClickListener(click -> addGuest());
        filterReset.addClickListener(click -> resetFilters());
        filterReset.addThemeVariants(ButtonVariant.LUMO_ERROR);
        var toolbar = new HorizontalLayout(filterText, filterArrived, filterLeft, filterReset, addGuestButton,  openDialogBtn);
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
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(4, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }


    private void configureGrid() {
        grid.addClassNames("guest-grid");
        grid.setSizeFull();
        // When setting columns update this list
        grid.setColumns("firstName", "lastName");
        grid.addColumn(new LocalDateRenderer<>(Guest::getBirthDate, () -> DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))).setHeader("Datum narození");
        grid.addColumn(Guest::getIdNumber).setHeader("Číslo dokladu");
        grid.addColumn(Guest::getNationality).setHeader("Stát");
        grid.addColumn(new LocalDateRenderer<>(Guest::getDateArrived, () -> DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))).setHeader("Datum příchodu");
        grid.addColumn(new LocalDateRenderer<>(Guest::getDateLeft, () -> DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))).setHeader("Datum odchodu");
        grid.getColumnByKey("firstName").setHeader("Jméno");
        grid.getColumnByKey("lastName").setHeader("Příjmení");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event ->
                editGuest(event.getValue()));
    }

    private void configureForm() {
        form = new GuestForm(ubyportService.getCountryList());
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
        grid.setItems(accommodationService.searchForGuests(filterText.getValue(), filterArrived.getValue(), filterLeft.getValue(), false));
        addExportBtns();
    }


}