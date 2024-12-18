package cz.ales17.mikina.views.guest;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import cz.ales17.mikina.data.model.Company;
import cz.ales17.mikina.data.model.Guest;
import cz.ales17.mikina.data.service.impl.AccommodationServiceImpl;
import cz.geek.ubyport.StatniPrislusnost;

import java.time.LocalDate;
import java.util.List;

/**
 * GuestForm is a form for editing Guest entities.
 */
public class GuestForm extends FormLayout {
    ComboBox<Company> company = new ComboBox<>("Ubytovatel");
    TextField firstName = new TextField("Jméno");
    TextField lastName = new TextField("Příjmení");
    TextField address = new TextField("Adresa");
    ComboBox<StatniPrislusnost> nationality = new ComboBox<>("Stát pobytu");
    DatePicker birthDate = new DatePicker("Datum narození");
    DatePicker dateArrived = new DatePicker("Datum příjezdu");
    DatePicker dateLeft = new DatePicker("Datum odjezdu");
    TextField idNumber = new TextField("Číslo dokladu");
    Button save = new Button("Uložit");
    Button delete = new Button("Smazat");
    Button close = new Button("Storno");
    Binder<Guest> binder = new BeanValidationBinder<>(Guest.class);
    AccommodationServiceImpl service;

    public GuestForm(AccommodationServiceImpl service, List<StatniPrislusnost> ubyportNationality) {
        this.service = service;

        addClassName("guest-form");

        binder.bindInstanceFields(this);
        company.setItems(service.findAllCompanies())    ;
        company.setItemLabelGenerator(Company::getName);

        nationality.setItems(ubyportNationality);
        nationality.setItemLabelGenerator(StatniPrislusnost::getTitle);
        add(
                company,
                firstName,
                lastName,
                address,
                nationality,
                birthDate,
                dateArrived,
                dateLeft,
                idNumber,
                createButtonsLayout());
        // Validation
        // Range between dateArrived and dateLeft
        dateArrived.addValueChangeListener(e -> handleDateArrived(e.getValue()));
        dateLeft.addValueChangeListener(e -> handleDateLeft(e.getValue()));
        // Birthdate can not be later than today
        birthDate.setMax(LocalDate.now());
        setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", 2)
        );
        setColspan(address, 2);

        // Validation
        firstName.setMaxLength(50);
        lastName.setMaxLength(50);
        idNumber.setMaxLength(50);
        address.setMaxLength(150);
    }

    public void setGuest(Guest guest) {
        binder.setBean(guest);
    }
    // ToDo calculate stay fee
    private void handleDateArrived(LocalDate d) {
        dateLeft.setMin(d);
        //handleFeeCalculation();
    }

    private void handleDateLeft(LocalDate d) {
        dateArrived.setMax(d);
        //handleFeeCalculation();
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);
        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }

    // Events
    public static abstract class GuestFormEvent
            extends ComponentEvent<GuestForm> {
        private final Guest guest;

        protected GuestFormEvent(GuestForm source, Guest guest) {
            super(source, false);
            this.guest = guest;
        }

        public Guest getGuest() {
            return guest;
        }
    }
    // Events

    public static class SaveEvent extends GuestFormEvent {
        public SaveEvent(GuestForm source, Guest guest) {
            super(source, guest);
        }
    }

    public static class DeleteEvent extends GuestFormEvent {
        DeleteEvent(GuestForm source, Guest guest) {
            super(source, guest);
        }

    }

    public static class CloseEvent extends GuestFormEvent {
        public CloseEvent(GuestForm source) {
            super(source, null);
        }
    }
}