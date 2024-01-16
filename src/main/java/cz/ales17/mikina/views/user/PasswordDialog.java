package cz.ales17.mikina.views.user;

import com.vaadin.flow.component.button.Button;
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
    private PasswordField passwd, passwdConfirmation, oldPassword;
    private final Button close = new Button("Storno", e -> close());
    private final Button save = new Button("Uložit", e -> changePassword());

    public PasswordDialog(UserService userService, User user) {
        this.user = user;
        this.userService = userService;
        setHeaderTitle("Změnit heslo");
        add(getLayout());
        add(getButtons());
    }

    private VerticalLayout getLayout() {
        oldPassword = new PasswordField("Staré heslo");
        passwd = new PasswordField("Nové heslo");
        passwdConfirmation = new PasswordField("Nové heslo znovu");

        return new VerticalLayout(oldPassword, passwd, passwdConfirmation);
    }

    private HorizontalLayout getButtons() {
        return new HorizontalLayout(save, close);
    }

    private void changePassword() {
        String pwdInput1 = passwd.getValue();
        String pwdInput2 = passwdConfirmation.getValue();
        if (userService.getEncoder().matches(oldPassword.getValue(), user.getHashedPassword())) {
            if (Objects.equals(pwdInput1, pwdInput2)) {
                userService.updatePassword(user, pwdInput2);
                // Must call update, otherwise it will not be persisted to the DB
                userService.update(user);
                Notification.show("Heslo změněno");
                close();
            } else {
                Notification.show("Hesla se neshodují");
            }
        } else {
            Notification.show("Původní heslo není správné");
        }
    }




}
