package cz.ales17.mikina.views.admin.user;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import cz.ales17.mikina.data.entity.User;
import cz.ales17.mikina.data.service.UserService;
import cz.ales17.mikina.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed({"ROLE_ADMIN"})
@Route(value = "admin/users", layout = MainLayout.class)
@PageTitle("Správa uživatelů | Ubytovací systém")
public class AdminUserView extends VerticalLayout {
    private final UserService userService;
    private final Grid<User> userGrid = new Grid<>(User.class);
    private AdminUserForm form;

    public AdminUserView(UserService userService) {
        this.userService = userService;
        setSizeFull();
        configureUserGrid();
        configureUserForm();

        add(getContent());
        updateUserGrid();
        closeEditor();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(userGrid, form);
        // flex grow for grid and form
        content.setSizeFull();
        return content;
    }


    private void configureUserGrid() {
        userGrid.setSizeFull();
        userGrid.setColumns("id", "username", "name", "emailAddress");
        userGrid.addColumn(User::getRolesAsString).setHeader("Role");
        userGrid.asSingleSelect().addValueChangeListener(e -> editUser(e.getValue()));

    }

    private void configureUserForm() {
        form = new AdminUserForm(userService);
        form.setWidth("30em");
        form.addDeleteListener(this::deleteUser);
        form.addSaveListener(this::saveUser);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveUser(AdminUserForm.SaveEvent event) {
        userService.saveUser(event.getUser());
        updateUserGrid();
        closeEditor();
    }

    private void deleteUser(AdminUserForm.DeleteEvent event) {
        userService.delete(event.getUser());
        updateUserGrid();
        closeEditor();
    }

    private void editUser(User user) {
        if (user == null) {
            closeEditor();
        } else {
            form.setUser(user);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setUser(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateUserGrid() {
        userGrid.setItems(userService.findAllUsers());
    }

}