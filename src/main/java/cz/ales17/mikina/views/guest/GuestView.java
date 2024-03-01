package cz.ales17.mikina.views.guest;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import cz.ales17.mikina.data.Role;
import cz.ales17.mikina.data.entity.Company;
import cz.ales17.mikina.data.entity.Guest;
import cz.ales17.mikina.data.entity.UserEntity;
import cz.ales17.mikina.data.service.impl.AccommodationServiceImpl;
import cz.ales17.mikina.data.service.impl.PdfReportService;
import cz.ales17.mikina.data.service.impl.UbyportReportService;
import cz.ales17.mikina.security.AuthenticatedUser;
import cz.ales17.mikina.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * GuestView shows a list of guests.
 */
@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
@Route(value = "guest", layout = MainLayout.class)
@PageTitle("Evidenční kniha")
public class GuestView extends VerticalLayout {
    private Guest selectedGuestInGrid;
    private final UbyportReportService ubyportReportService;
    private final PdfReportService pdfReportService;
    private final AccommodationServiceImpl accommodationService;
    private final AuthenticatedUser authenticatedUser;
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
    private final Grid<Guest> guestGrid = new Grid<>(Guest.class);
    private final DateTimeFormatter gridDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final DateTimeFormatter pdfDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
    private final DateTimeFormatter unlDateFormatter = DateTimeFormatter.ofPattern("ddMMyyHHmm");
    private final DateTimeFormatter printDateFormatter = DateTimeFormatter.ofPattern("dd. MM. yyyy HH.mm");

    private GuestForm form;
    private Company currentUserCompany;
    private List<Guest> currentGuestList, currentForeignGuestList;

    public GuestView(AccommodationServiceImpl accommodationService, UbyportReportService ubyportReportService, PdfReportService pdfReportService, AuthenticatedUser authenticatedUser) {
        this.accommodationService = accommodationService;
        this.ubyportReportService = ubyportReportService;
        this.pdfReportService = pdfReportService;
        this.authenticatedUser = authenticatedUser;
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

    private void prepareReports() {
        LocalDateTime now = LocalDateTime.now();

        preparePdfExport(currentGuestList, now);
        prepareUbyportExport(currentForeignGuestList, now);
    }

    private void preparePdfExport(List<Guest> guests, LocalDateTime now) {
        String formatDateTime = now.format(pdfDateFormatter);

        try {
            pdfReportService.setTime(now);

            byte[] pdfBytes = pdfReportService.getReportBytes(currentUserCompany, guests);

            StreamResource resource = new StreamResource("ubytovaci-kniha_" + formatDateTime + ".pdf", () -> new ByteArrayInputStream(pdfBytes));

            exportDialog.pdfBtn.setTarget("_blank");
            exportDialog.pdfBtn.setHref(resource);
            exportDialog.pdfBtn.setEnabled(true);
        } catch (Exception e) {
            Notification.show("Chyba při generování PDF", 5000, Notification.Position.MIDDLE);
            exportDialog.pdfBtn.setEnabled(false);
            throw new RuntimeException(e);
        }
    }

    private void prepareUbyportExport(List<Guest> foreignGuestList, LocalDateTime now) {
        String formatDateTime = now.format(unlDateFormatter);
        try {
            byte[] unlReportBytes = ubyportReportService.getReportBytes(currentUserCompany, foreignGuestList);
            StreamResource resource = new StreamResource("123456789012_" + formatDateTime + ".unl", () -> new ByteArrayInputStream(unlReportBytes));
            exportDialog.unlBtn.setHref(resource);
            exportDialog.unlBtn.getElement().setAttribute("download", true);
            exportDialog.unlBtn.setEnabled(true);
        } catch (Exception e) {
            Notification.show("Chyba při generování UNL", 5000, Notification.Position.MIDDLE);
            exportDialog.unlBtn.setEnabled(false);
            throw new RuntimeException(e);
        }
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Jméno / příjmení");
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

        duplicateGuestBtn.addClickListener(click -> handleGuestDuplication());
        duplicateGuestBtn.setEnabled(false);

        var toolbar = new HorizontalLayout(openDialogBtn, filterText, filterArrived, filterLeft, filterReset, addGuestButton, duplicateGuestBtn);
        toolbar.setAlignItems(Alignment.END);
        toolbar.addClassName("toolbar");
        toolbar.setPadding(false);
        toolbar.getStyle().set("display", "inline-flex");

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
        guestGrid.getColumnByKey("firstName").setHeader("Jméno");
        guestGrid.getColumnByKey("lastName").setHeader("Příjmení");
        guestGrid.addColumn(new LocalDateRenderer<>(Guest::getBirthDate, () -> gridDateFormatter)).setHeader("Datum narození");
        guestGrid.addColumn(Guest::getIdNumber).setHeader("Číslo dokladu");
        guestGrid.addColumn(Guest::getNationality).setHeader("Stát");
        guestGrid.addColumn(new LocalDateRenderer<>(Guest::getDateArrived, () -> gridDateFormatter)).setHeader("Datum příchodu");
        guestGrid.addColumn(new LocalDateRenderer<>(Guest::getDateLeft, () -> gridDateFormatter)).setHeader("Datum odchodu");
        guestGrid.getColumns().forEach(col -> col.setAutoWidth(true));
        guestGrid.asSingleSelect().addValueChangeListener(event -> handleGuestSelection(event.getValue()));
    }

    private void handleGuestSelection(Guest g) {
        selectedGuestInGrid = g;
        editGuest(g);
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

    private void handleGuestDuplication() {
        if (selectedGuestInGrid == null) {
            return;
        } else {
            form.setGuest(null);
            accommodationService.duplicateGuest(selectedGuestInGrid);
            updateList();
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
        // Adding new guest, set the company to the user's company
        form.company.setValue(currentUserCompany);
    }


    private void updateList() {
        Optional<UserEntity> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            UserEntity currentUser = maybeUser.get();
            currentUserCompany = currentUser.getCompany();

            // User is admin, get all guests
            if (currentUser.getRoles().contains(Role.ADMIN)) {
                currentGuestList = accommodationService.searchForAllGuests(filterText.getValue(), filterArrived.getValue(), filterLeft.getValue());
                currentForeignGuestList = accommodationService.searchForForeignGuests(filterText.getValue(), filterArrived.getValue(), filterLeft.getValue());
            } else { // Get only the guests that belong to user's company
                currentGuestList = accommodationService.searchGuests(filterText.getValue(), filterArrived.getValue(), filterLeft.getValue(), false, currentUserCompany);
                currentForeignGuestList = accommodationService.searchGuests(filterText.getValue(), filterArrived.getValue(), filterLeft.getValue(), true, currentUserCompany);
                form.company.setReadOnly(true); // No admin, do not allow changing company
            }
            guestGrid.setItems(currentGuestList);
            prepareReports();
        }
    }
}