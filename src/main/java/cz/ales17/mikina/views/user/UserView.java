package cz.ales17.mikina.views.user;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import cz.ales17.mikina.data.model.UserEntity;
import cz.ales17.mikina.data.service.impl.UserServiceImpl;
import cz.ales17.mikina.security.AuthenticatedUser;
import cz.ales17.mikina.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;

import java.util.Optional;

@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
@PageTitle("Můj účet")
@Route(value = "user", layout = MainLayout.class)
public class UserView extends VerticalLayout {
    private final UserServiceImpl userService;
    private PasswordDialog dialog;

    public UserView(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker, UserServiceImpl userService) {
        this.userService = userService;


        Optional<UserEntity> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            UserEntity user = maybeUser.get();
            /*StreamResource resource = new StreamResource("profile-pic",
                    () -> new ByteArrayInputStream(user.getProfilePicture()));*/

            H3 userInfo = new H3("Informace o uživateli");
            add(userInfo);
            configureForm(user);

            H3 userButtons = new H3("Nastavení uživatele");
            add(userButtons, getUserButtons());
            configureDialog(user);
        }
    }

    private void configureDialog(UserEntity user) {
        dialog = new PasswordDialog(userService, user);
        add(dialog);
    }

    private HorizontalLayout getUserButtons() {
        Button changePasswdBtn = new Button("Změnit heslo", e -> dialog.open());
        return new HorizontalLayout(changePasswdBtn);
    }

    private void configureForm(UserEntity user) {
        UserForm form = new UserForm(userService, user);
        form.setVisible(true);
        form.setWidth("30em");
        form.setUser(user);
        form.addSaveListener(this::saveUser);
        add(form);
    }

    private void saveUser(UserForm.SaveEvent event) {
        userService.saveUser(event.getUser());
        /* After user changes info, page will be reloaded.
        * This prevents error when user tries to do another change without reload.
        * User display name in MainLayout is also updated thanks to refresh. . */
        Optional<UI> maybeUi = getUI();
        if(maybeUi.isPresent()) {
            Page p = maybeUi.get().getPage();
            p.reload();
        }
    }

}
