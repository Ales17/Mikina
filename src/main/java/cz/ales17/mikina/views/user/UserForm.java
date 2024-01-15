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
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.shared.Registration;
import cz.ales17.mikina.data.entity.User;
import cz.ales17.mikina.data.service.UserService;

import java.util.Objects;

public class UserForm extends FormLayout {

    private TextField username = new TextField("Uživatelské jméno");
    private TextField name = new TextField("Jméno a příjmení");
    private PasswordField oldPassword = new PasswordField("Staré heslo");
    private PasswordField password = new PasswordField("Nové heslo");
    private PasswordField passwordConfirmation = new PasswordField("Nové heslo znovu");

    private Binder<User> binder = new BeanValidationBinder<>(User.class);
    private Button save = new Button("Uložit");

    private UserService userService;
    private User user;

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
                oldPassword,
                password,
                passwordConfirmation,
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

    private void changePassword() {
        String pwdInput1 = password.getValue();
        String pwdInput2 = passwordConfirmation.getValue();


        if (userService.getEncoder().matches(oldPassword.getValue(), user.getHashedPassword())) {
            if (Objects.equals(pwdInput1, pwdInput2)) {
                //user.setHashedPassword(userService.getEncoder().encode(pwdInput1));
                userService.updatePassword(user, pwdInput2);
                Notification.show("Heslo změněno");
            } else {
                Notification.show("Hesla se neshodují");
            }
        } else {
            Notification.show("Původní heslo není správné");
        }
    }

    private void validateAndSave() {
        changePassword();
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }


    public static abstract class UserFormEvent
            extends ComponentEvent<UserForm> {
        private final User user;

        protected UserFormEvent(UserForm source, User user) {
            super(source, false);
            this.user = user;
        }

        public User getUser() {
            return user;
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
