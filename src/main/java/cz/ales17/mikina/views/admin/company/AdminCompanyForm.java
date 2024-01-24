package cz.ales17.mikina.views.admin.company;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import cz.ales17.mikina.data.entity.Company;
import cz.ales17.mikina.data.service.AccommodationService;
import cz.ales17.mikina.views.admin.user.AdminUserForm;
import lombok.Getter;

public class AdminCompanyForm extends FormLayout {
    public final Button delete = new Button("Smazat");
    public final Button close = new Button("Storno");
    private final AccommodationService service;
    private final Binder<Company> binder = new BeanValidationBinder<>(Company.class);
    private final Button save = new Button("Uložit");
    private Company company;
    private TextField name = new TextField("Název zařízení");
    private TextField district = new TextField("Okres");
    private TextField houseNumber = new TextField("Číslo domovní");
    private TextField registrationNumber = new TextField("Číslo orientační");
    private TextField municipality = new TextField("Obec");
    private TextField municipalityQuarter = new TextField("Část obce");
    private TextField street = new TextField("Ulice");
    private TextField ubyportAbbr = new TextField("Ubyport zkratka");
    private TextField ubyportContact = new TextField("Ubyport kontakt");
    private TextField ubyportId = new TextField("Ubyport ID");
    private TextField zipCode = new TextField("PSČ");

    public AdminCompanyForm(AccommodationService service) {
        this.service = service;
        binder.bindInstanceFields(this);

        add(name,
                street,
                houseNumber,
                registrationNumber,
                municipalityQuarter,
                municipality,
                zipCode,
                ubyportAbbr,
                ubyportContact,
                ubyportId,
                createButtonsLayout()
        );

    }

    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }
    private Component createButtonsLayout() {

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);
        save.addClickListener(event -> validateAndSave());
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        close.addClickShortcut(Key.ESCAPE);
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));

        return new HorizontalLayout(save, delete, close);


    }
    public void addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        addListener(DeleteEvent.class, listener);
    }

    public void addSaveListener(ComponentEventListener<SaveEvent> listener) {
        addListener(SaveEvent.class, listener);
    }

    public void addCloseListener(ComponentEventListener<CloseEvent> listener) {
        addListener(CloseEvent.class, listener);
    }



    public void setCompany(Company c) {
        binder.setBean(c);
    }

    @Getter
    public static abstract class AdminCompanyFormEvent extends ComponentEvent<AdminCompanyForm> {
        private final Company company;

        protected AdminCompanyFormEvent(AdminCompanyForm source, Company company) {
            super(source, false);
            this.company = company;
        }
    }

    public static class SaveEvent extends AdminCompanyFormEvent {
        public SaveEvent(AdminCompanyForm source, Company c) {
            super(source, c);
        }

    }

    public static class DeleteEvent extends AdminCompanyFormEvent {
        public DeleteEvent(AdminCompanyForm source, Company c) {
            super(source, c);
        }
    }

    public static class CloseEvent extends AdminCompanyFormEvent {
        public CloseEvent(AdminCompanyForm source) {
            super(source, null);
        }
    }
}