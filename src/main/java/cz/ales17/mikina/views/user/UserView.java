package cz.ales17.mikina.views.user;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import cz.ales17.mikina.data.entity.User;
import cz.ales17.mikina.data.service.UserService;
import cz.ales17.mikina.security.AuthenticatedUser;
import cz.ales17.mikina.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;

import java.util.Optional;

@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
@Route(value = "user", layout = MainLayout.class)
public class UserView extends VerticalLayout {
    private final UserService userService;
    PasswordDialog dialog;
    private AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;
    private UserForm form;

    public UserView(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker, UserService userService) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;
        this.userService = userService;


        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            /*StreamResource resource = new StreamResource("profile-pic",
                    () -> new ByteArrayInputStream(user.getProfilePicture()));*/
            H3 userButtons = new H3("Nastavení uživatele");
            add(userButtons, getUserButtons());
            configureDialog(user);
            H3 userInfo = new H3("Informace o uživateli");
            add(userInfo);
            configureForm(user);
        }
    }

    private void configureDialog(User user) {
        dialog = new PasswordDialog(userService, user);
        add(dialog);
    }

    private HorizontalLayout getUserButtons() {
        Button changePasswdBtn = new Button("Změnit heslo", e -> dialog.open());
        return new HorizontalLayout(changePasswdBtn);
    }

    private void configureForm(User user) {
        form = new UserForm(userService, user);
        form.setVisible(true);
        form.setWidth("30em");
        form.setUser(user);
        form.addSaveListener(this::saveUser);
        add(form);
    }

    private void saveUser(UserForm.SaveEvent event) {
        userService.update(event.getUser());
    }

}
