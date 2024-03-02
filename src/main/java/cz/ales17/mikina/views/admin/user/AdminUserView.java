package cz.ales17.mikina.views.admin.user;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import cz.ales17.mikina.data.model.UserEntity;
import cz.ales17.mikina.data.service.impl.AccommodationServiceImpl;
import cz.ales17.mikina.data.service.impl.UserServiceImpl;
import cz.ales17.mikina.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed({"ROLE_ADMIN"})
@Route(value = "users", layout = MainLayout.class)
@PageTitle("Správa uživatelů")
public class AdminUserView extends VerticalLayout {
    private final UserServiceImpl userService;
    private final AccommodationServiceImpl accommodationService;
    private final Grid<UserEntity> userGrid = new Grid<>(UserEntity.class);
    private AdminUserForm form;

    private final Button addUserBtn = new Button("Přidat uživatele");

    public AdminUserView(UserServiceImpl userService, AccommodationServiceImpl accommodationService) {
        this.userService = userService;
        this.accommodationService = accommodationService;
        setSizeFull();
        configureUserGrid();
        configureUserForm();

        add(getToolbar(), getContent());
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
        userGrid.addColumn(UserEntity::getRolesAsString).setHeader("Role");
        userGrid.asSingleSelect().addValueChangeListener(e -> editUser(e.getValue()));

    }

    private Component getToolbar() {
        addUserBtn.addClickListener(click -> addUser());
        return new HorizontalLayout(addUserBtn);
    }

    private void configureUserForm() {
        form = new AdminUserForm(userService, accommodationService);
        form.setWidth("30em");
        form.addDeleteListener(this::deleteUser);
        form.addSaveListener(this::saveUser);
        form.addCloseListener(e -> closeEditor());
    }
    private void addUser() {
        userGrid.asSingleSelect().clear();
        editUser(new UserEntity());
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

    private void editUser(UserEntity user) {
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