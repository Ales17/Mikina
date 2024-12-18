package cz.ales17.mikina.views.guest;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;
import cz.ales17.mikina.data.model.*;
import cz.ales17.mikina.data.service.ReportService;
import cz.ales17.mikina.data.service.impl.AccommodationServiceImpl;
import cz.ales17.mikina.security.AuthenticatedUser;
import cz.ales17.mikina.util.PdfReportGenerator;
import cz.ales17.mikina.util.UbyportReportGenerator;
import cz.ales17.mikina.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static cz.ales17.mikina.util.DateTimeUtil.*;

/**
 * GuestView shows a list of guests.
 */
@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
@Route(value = "guest", layout = MainLayout.class)
@PageTitle("Evidenční kniha")
public class GuestView extends VerticalLayout {
    private final UbyportReportGenerator ubyportReportGenerator;
    private final PdfReportGenerator pdfReportGenerator;
    private final AccommodationServiceImpl accommodationService;
    private final ReportService recordService;

    private final AuthenticatedUser authenticatedUser;
    private final ExportDialog exportDialog = new ExportDialog();
    // Filtering for guests and utils
    private final Grid<Guest> guestGrid = new Grid<>(Guest.class);
    private final TextField filterText = new TextField("Vyhledávání");
    private final DatePicker filterArrived = new DatePicker("Den příchodu");
    private final DatePicker filterLeft = new DatePicker("Den odchodu");
    private final Button openDialogBtn = new Button("Export", e -> handleDialogOpening());
    private final LocalDate currentMonthFirstDay = LocalDate.now().withDayOfMonth(1);
    private final LocalDate currentMonthLastDay = YearMonth.now().atEndOfMonth();
    private final Button addGuestButton = new Button("Přidat hosta");
    private final Button filterReset = new Button(new Icon(VaadinIcon.CLOSE_SMALL));

    private GuestForm form;
    private Company currentUserCompany;

    private final DeleteDialog deleteDialog;

    private List<Guest> currentGuestList, currentForeignGuestList;

    public GuestView(
            AccommodationServiceImpl accommodationService,
            UbyportReportGenerator ubyportReportGenerator,
            PdfReportGenerator pdfReportGenerator,
            AuthenticatedUser authenticatedUser,
            ReportService recordService
    ) {
        this.accommodationService = accommodationService;
        this.ubyportReportGenerator = ubyportReportGenerator;
        this.pdfReportGenerator = pdfReportGenerator;
        this.authenticatedUser = authenticatedUser;
        this.recordService = recordService;
        addClassName("list-view");
        setSizeFull();
        // Configuring components
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());
        updateList();
        // Editor will be closed at the start
        closeEditor();

        deleteDialog = new DeleteDialog();

    }

    private void handleDialogOpening() {
        if (!currentGuestList.isEmpty()) {
            prepareReports();
        }
        exportDialog.open();
    }

    private void prepareReports() {
        LocalDateTime now = LocalDateTime.now();
        preparePdfExport(currentGuestList, now);
        prepareUbyportExport(currentForeignGuestList, now);
    }

    private void preparePdfExport(List<Guest> guests, LocalDateTime now) {
        String formatDateTime = now.format(pdfDateFormatter);
        try {
            pdfReportGenerator.setTime(now);
            byte[] pdfBytes = pdfReportGenerator.getReportBytes(currentUserCompany, guests);

            String filename = String.format("ubytovaci-kniha_%s.%s", formatDateTime, "pdf");
            recordService.saveReport(pdfBytes, ReportType.PDF, filename, currentUserCompany);

            StreamResource resource = new StreamResource(filename, () -> new ByteArrayInputStream(pdfBytes));
            Anchor anchor = exportDialog.pdfAnchor;
            anchor.setTarget("_blank");
            anchor.setHref(resource);
            anchor.setEnabled(true);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            Notification.show("Chyba při generování reportu", 5000, Notification.Position.MIDDLE);
            exportDialog.pdfAnchor.setEnabled(false);
        }
    }

    private void prepareUbyportExport(List<Guest> foreignGuestList, LocalDateTime now) {
        String formatDateTime = now.format(unlDateFormatter);
        try {
            byte[] reportBytes = ubyportReportGenerator.getReportBytes(currentUserCompany, foreignGuestList);

            String fileName = String.format("%s_%s.%s", currentUserCompany.getUbyportId(), formatDateTime, "unl");
            recordService.saveReport(reportBytes, ReportType.UNL, fileName, currentUserCompany);
            StreamResource resource = new StreamResource(fileName, () -> new ByteArrayInputStream(reportBytes));
            Anchor anchor = exportDialog.unlAnchor;
            anchor.setHref(resource);
            anchor.getElement().setAttribute("download", true);
            anchor.setEnabled(true);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            Notification.show("Chyba při generování reportu", 5000, Notification.Position.MIDDLE);
            exportDialog.unlAnchor.setEnabled(false);
        }
    }


    private Component getFilterBar() {
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

        filterReset.setAriaLabel("Zrušit filtr");
        filterReset.addClickListener(click -> resetFilters());
        var toolbar = new HorizontalLayout(filterArrived, filterLeft, filterReset);
        toolbar.setAlignItems(Alignment.END);
        toolbar.addClassName(LumoUtility.FlexWrap.WRAP);
        return toolbar;
    }

    private Component getToolbar() {

        // Listeners
        addGuestButton.addClickListener(click -> addGuest());
        addGuestButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        var toolbarGuestOperation = new HorizontalLayout(openDialogBtn, addGuestButton);
        toolbarGuestOperation.setAlignItems(Alignment.END);
        toolbarGuestOperation.addClassName("toolbar");
        toolbarGuestOperation.setPadding(false);
        toolbarGuestOperation.getStyle().set("display", "inline-flex");
        toolbarGuestOperation.setAlignItems(Alignment.END);

        var toolbar = new HorizontalLayout(getFilterBar(), toolbarGuestOperation);
        toolbar.setWidthFull();
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        toolbar.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN,
                LumoUtility.FlexDirection.Breakpoint.Medium.ROW, LumoUtility.Gap.MEDIUM);
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

        guestGrid.addColumn(
                new ComponentRenderer<>(person -> {
                    Button copy = new Button();
                    copy.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_PRIMARY,
                            ButtonVariant.LUMO_TERTIARY);
                    copy.addClickListener(e -> this.duplicateGuest(person));
                    copy.setIcon(new Icon(VaadinIcon.COPY));
                    copy.setTooltipText("Duplikovat hosta");

                    Button delete = new Button();
                    delete.addClickListener(e -> handleGuestDeleting(person));
                    delete.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_PRIMARY);

                    delete.setIcon(new Icon(VaadinIcon.TRASH));
                    delete.setTooltipText("Smazat hosta");

                    HorizontalLayout buttons =
                            new HorizontalLayout(copy, delete);
                    return buttons;
                })).setHeader("Akce");
        // Styling rows without date arrived OR left - adding CSS class to styles.css in frontend/themes/...
        guestGrid.setPartNameGenerator(person -> {
            if (person.getDateArrived() == null || person.getDateLeft() == null) {
                return "incomplete";
            }
            return null;
        });

        guestGrid.getColumns().forEach(col -> col.setAutoWidth(true));

        guestGrid.asSingleSelect().addValueChangeListener(event -> handleGuestSelection(event.getValue()));
    }

    private void handleGuestSelection(Guest g) {
        editGuest(g);
    }

    private void handleGuestDeleting(Guest g) {
        deleteDialog.addConfirmListener(e -> {
            accommodationService.deleteGuest(g);
            updateList();
        });
        deleteDialog.open();
    }

    private void configureForm() {
        form = new GuestForm(accommodationService, ubyportReportGenerator.getCountryList());
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


    private void duplicateGuest(Guest toDuplicate) {
        form.setGuest(null);
        accommodationService.duplicateGuest(toDuplicate);
        updateList();
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
        }
    }
}