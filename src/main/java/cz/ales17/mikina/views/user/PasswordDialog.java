package cz.ales17.mikina.views.user;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import cz.ales17.mikina.data.entity.User;
import cz.ales17.mikina.data.service.UserService;

import java.util.Objects;

public class PasswordDialog extends Dialog {
    private final UserService userService;
    private final User user;
    private final Button close = new Button("Storno", e -> close());
    private PasswordField passwd, passwdConfirmation, oldPassword;
    private final Button save = new Button("Uložit", e -> handlePasswordChange());

    public PasswordDialog(UserService userService, User user) {
        this.user = user;
        this.userService = userService;
        setHeaderTitle("Změnit heslo");
        add(getLayout());
        add(getButtons());
    }

    private VerticalLayout getLayout() {
        oldPassword = new PasswordField("Původní heslo");
        oldPassword.setRequired(true);
        passwd = new PasswordField("Nové heslo");
        passwd.setRequired(true);
        passwdConfirmation = new PasswordField("Nové heslo (pro kontrolu)");
        passwdConfirmation.setRequired(true);

        return new VerticalLayout(oldPassword, passwd, passwdConfirmation);
    }

    private HorizontalLayout getButtons() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_ERROR);
        return new HorizontalLayout(save, close);
    }

    private void handlePasswordChange() {
        if (userService.getEncoder().matches(oldPassword.getValue(), user.getHashedPassword())) {
            String newPwdInput1 = passwd.getValue();
            String newPwdInput2 = passwdConfirmation.getValue();
            if (Objects.equals(newPwdInput1, newPwdInput2)) {
                int length = 3;
                if (newPwdInput1.length() < length) {
                    Notification.show("Délka hesla musí být delší než " + length);

                } else {
                    userService.updatePassword(user, newPwdInput2);
                    // Must call update, otherwise it will not be persisted to the DB
                    //userService.update(user);
                    Notification.show("Heslo změněno");
                    close();
                }
            } else {
                Notification.show("Hesla se neshodují");
            }
        } else {
            Notification.show("Původní heslo není správné");
        }
    }


}
