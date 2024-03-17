package cz.ales17.mikina.views.user;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.shared.Registration;
import cz.ales17.mikina.data.model.UserEntity;
import cz.ales17.mikina.data.service.impl.UserServiceImpl;
import lombok.Getter;

public class UserForm extends FormLayout {

    private final UserServiceImpl userService;
    private final UserEntity user;
    private final Binder<UserEntity> binder = new BeanValidationBinder<>(UserEntity.class);
    private final TextField username = new TextField("Uživatelské jméno");
    private final TextField name = new TextField("Jméno a příjmení");
    private final EmailField emailAddress = new EmailField("E-mailová adresa");

    private final Button save = new Button("Uložit");


    public UserForm(UserServiceImpl userService, UserEntity user) {
        this.userService = userService;
        this.user = user;
        binder.bindInstanceFields(this);
        binder.forField(name)
                .withValidator(new StringLengthValidator(
                        "Jméno musí mít délku mezi 3 a 50 znaky",
                        3, 50))
                .bind(UserEntity::getName, UserEntity::setName);
        add(
                username,
                name,
                emailAddress,
                createButtonsLayout()
        );
        // Validation
        name.setMaxLength(50);
        username.setEnabled(false);
        emailAddress.setMaxLength(255);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);
        save.addClickListener(event -> validateAndSave());
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }


    @Getter
    public static abstract class UserFormEvent
            extends ComponentEvent<UserForm> {
        private final UserEntity user;

        protected UserFormEvent(UserForm source, UserEntity user) {
            super(source, false);
            this.user = user;
        }

    }
    // Events

    public static class SaveEvent extends UserFormEvent {
        public SaveEvent(UserForm source, UserEntity user) {
            super(source, user);
        }
    }

    public void setUser(UserEntity user) {
        binder.setBean(user);
    }
}
