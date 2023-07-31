package com.example.application.views.guest;

import com.example.application.data.entity.Country;
import com.example.application.data.entity.Guest;
import com.example.application.data.entity.Status;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.time.LocalDate;
import java.util.List;

import static org.aspectj.lang.reflect.DeclareAnnotation.Kind.Type;

/**
 * GuestForm is a form for editing Guest entities.
 */
public class GuestForm extends FormLayout {
    TextField firstName = new TextField("Jméno");
    TextField lastName = new TextField("Příjmení");
    //EmailField email = new EmailField("Email");
   // ComboBox<Status> status = new ComboBox<>("Status");
    TextField address = new TextField("Adresa");
    ComboBox<Country> country = new ComboBox<>("Země");
    DatePicker birthDate = new DatePicker("Datum narození");
    DatePicker dateArrived = new DatePicker("Datum příjezdu");
    DatePicker dateLeft = new DatePicker("Datum odjezdu");
    TextField idNumber = new TextField("Číslo dokladu");
    Button save = new Button("Uložit");
    Button delete = new Button("Smazat");
    Button close = new Button("Storno");
    Binder<Guest> binder = new BeanValidationBinder<>(Guest.class);
    public GuestForm(List<Country> countries) {


        addClassName("guest-form");
        binder.bindInstanceFields(this);
        country.setItems(countries);
        country.setItemLabelGenerator(Country::getCountryName);
        //status.setItems(statuses);
        //status.setItemLabelGenerator(Status::getName);
        add(firstName,
                lastName,
        //        email,
                address,
                country,
             //   status,
                birthDate, dateArrived, dateLeft, idNumber,
                createButtonsLayout());
        // Validation
        // Range between dateArrived and dateLeft
        dateArrived.addValueChangeListener(e->dateLeft.setMin(e.getValue()));
        dateLeft.addValueChangeListener(e->dateArrived.setMax(e.getValue()));
        // Birthdate can not be later than today
        birthDate.setMax(LocalDate.now());
        setResponsiveSteps(
                new ResponsiveStep("0",1),
                new ResponsiveStep("500px",2)
        );
        setColspan(address, 2);
    }
    public void setGuest(Guest guest) {
        binder.setBean(guest);
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
        return new HorizontalLayout(save, delete, close);
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