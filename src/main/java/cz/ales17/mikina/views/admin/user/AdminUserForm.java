package cz.ales17.mikina.views.admin.user;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.shared.Registration;
import cz.ales17.mikina.data.Role;
import cz.ales17.mikina.data.entity.Company;
import cz.ales17.mikina.data.entity.UserEntity;
import cz.ales17.mikina.data.service.impl.AccommodationService;
import cz.ales17.mikina.data.service.impl.UserServiceImpl;
import lombok.Getter;

import java.util.Objects;

public class AdminUserForm extends FormLayout {

    private final UserServiceImpl userService;
    private UserEntity user;
    private final Binder<UserEntity> binder = new BeanValidationBinder<>(UserEntity.class);
    @Getter
    private final TextField username = new TextField("Uživatelské jméno");
    private final TextField name = new TextField("Jméno a příjmení");
    private final EmailField emailAddress = new EmailField("E-mailová adresa");
    private final ComboBox<Company> company = new ComboBox<>("Ubytovací zařízení");
    private final Button save = new Button("Uložit");
    public final Button delete = new Button("Smazat");
    public final Button close = new Button("Storno");
    private final PasswordField passwd, passwdConfirmation;
    AccommodationService accommodationService;

    public AdminUserForm(UserServiceImpl userService, AccommodationService accommodationService) {
        this.userService = userService;
        this.accommodationService=accommodationService;
        MultiSelectComboBox<Role> roles = new MultiSelectComboBox<>("Uživatelské role");
        roles.setItems(Role.USER,Role.ADMIN);
        binder.forField(roles).bind(UserEntity::getRoles, UserEntity::setRoles);


        company.setItemLabelGenerator(Company::getName);
        company.setItems(accommodationService.findAllCompanies());

        binder.forField(name)
                .withValidator(new StringLengthValidator(
                        "Jméno musí mít délku mezi 3 a 50 znaky",
                        3, 50)).bind(UserEntity::getName, UserEntity::setName);

        binder.bindInstanceFields(this);

        passwd = new PasswordField("Nové heslo");
        passwdConfirmation = new PasswordField("Nové heslo pro kontrolu");

        add(
                username,
                name,
                emailAddress,
                company,
                passwd,
                passwdConfirmation,
                roles,
                createButtonsLayout()
        );

        //TODO disable username field (changing username) but allow when  creating new acc
        //username.setEnabled(false);
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

    private void validateAndSave() {
        System.out.println("Validate and save");
        System.out.println("Password change");
        if (passwd.getValue() != null && passwdConfirmation.getValue() != null) {
            if (Objects.equals(passwd.getValue(), passwdConfirmation.getValue())) {
                System.out.println("****");
                String rawPasswd = passwd.getValue();
                String encodedPasswd = userService.getEncoder().encode(rawPasswd);
                binder.getBean().setHashedPassword(encodedPasswd);
                System.out.println("----");
                passwd.setValue("");
                passwdConfirmation.setValue("");
            } else {
                Notification.show("Hesla se neshodují");
            }
        }
        System.out.println("Fire event (save info)");
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }

    }
    public void addDeleteListener(ComponentEventListener<DeleteEvent> listener)
    {
        addListener(DeleteEvent.class, listener);
    }
    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public void addCloseListener(ComponentEventListener<CloseEvent> listener) {
        addListener(CloseEvent.class, listener);
    }

    public void setUser(UserEntity user) {
        binder.setBean(user);
        this.user = user;
    }
    // Events

    @Getter
    public static abstract class AdminUserFormEvent
            extends ComponentEvent<AdminUserForm> {
        private final UserEntity user;

        protected AdminUserFormEvent(AdminUserForm source, UserEntity user) {
            super(source, false);
            this.user = user;
        }

    }

    public static class SaveEvent extends AdminUserFormEvent {
        public SaveEvent(AdminUserForm source, UserEntity user) {
            super(source, user);
        }
    }

    public static class DeleteEvent extends AdminUserFormEvent {
        public DeleteEvent(AdminUserForm source, UserEntity user) {
            super(source, user);
        }
    }

    public static class CloseEvent extends AdminUserFormEvent {
        public CloseEvent(AdminUserForm source) {
            super(source, null);
        }
    }
}
