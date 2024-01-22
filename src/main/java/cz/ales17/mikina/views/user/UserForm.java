package cz.ales17.mikina.views.user;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.shared.Registration;
import cz.ales17.mikina.data.entity.User;
import cz.ales17.mikina.data.service.UserService;
import lombok.Getter;

public class UserForm extends FormLayout {

    private final UserService userService;
    private final User user;
    private final Binder<User> binder = new BeanValidationBinder<>(User.class);
    private final TextField username = new TextField("Uživatelské jméno");
    private final TextField name = new TextField("Jméno a příjmení");
    private final EmailField emailAddress = new EmailField("E-mailová adresa");

    private final Button save = new Button("Uložit");


    public UserForm(UserService userService, User user) {
        this.userService = userService;
        this.user = user;
        binder.bindInstanceFields(this);
        binder.forField(name)
                .withValidator(new StringLengthValidator(
                        "Jméno musí mít délku mezi 3 a 50 znaky",
                        3, 50))
                .bind(User::getName, User::setName);
        add(
                username,
                name,
                emailAddress,
                createButtonsLayout()
        );

        username.setEnabled(false);
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
        private final User user;

        protected UserFormEvent(UserForm source, User user) {
            super(source, false);
            this.user = user;
        }

    }
    // Events

    public static class SaveEvent extends UserFormEvent {
        public SaveEvent(UserForm source, User user) {
            super(source, user);
        }
    }

    public void setUser(User user) {
        binder.setBean(user);
    }
}
