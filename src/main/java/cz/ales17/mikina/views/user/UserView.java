package cz.ales17.mikina.views.user;

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
    private AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;

    private UserService userService;
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
            configureForm(user);
          /*  configureDialog(user);*/
        }
    }

    private void configureForm(User user) {
        form = new UserForm(userService, user);
        form.setVisible(true);
        form.setWidth("30em");
        form.setUser(user);
        add(form);
        form.addSaveListener(this::saveUser);
    }



    private void saveUser(UserForm.SaveEvent event) {
        userService.update(event.getUser());
    }

}
