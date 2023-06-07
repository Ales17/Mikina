package com.example.application.views;

import com.example.application.data.entity.Company;
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
import com.vaadin.flow.shared.Registration;

public class CompanyForm extends FormLayout {
    TextField companyName = new TextField("Company Name");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    Binder<Company> binder = new BeanValidationBinder<>(Company.class);

    public CompanyForm() {
        addClassName("company-form");
        binder.bindInstanceFields(this);

        add(companyName,
                createButtonsLayout());
    }

    public void setCompany(Company company) {
        binder.setBean(company);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(click -> fireEvent(new DeleteEvent(this, binder.getBean())));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));
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
    public static abstract class CompanyFormEvent extends ComponentEvent<CompanyForm> {
        private Company company;

        protected CompanyFormEvent(CompanyForm source, Company company) {
            super(source, false);
            this.company = company;
        }

        public Company getCompany() {
            return company;
        }
    }
    public static class SaveEvent extends CompanyFormEvent {
        SaveEvent(CompanyForm source, Company company) {
            super(source, company);
        }
    }
    public static class DeleteEvent extends CompanyFormEvent {
        DeleteEvent(CompanyForm source, Company company) {
            super(source, company);
        }
    }
    public static class CloseEvent extends CompanyFormEvent {
        CloseEvent(CompanyForm source) {
            super(source, null);
        }
    }
}
