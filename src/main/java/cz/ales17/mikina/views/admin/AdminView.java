package cz.ales17.mikina.views.admin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import cz.ales17.mikina.views.MainLayout;
import cz.ales17.mikina.views.admin.company.AdminCompanyView;
import cz.ales17.mikina.views.admin.user.AdminUserView;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed({"ROLE_ADMIN"})
@Route(value = "admin", layout = MainLayout.class)
@PageTitle("Administrace systému")
public class AdminView extends VerticalLayout {
    public AdminView() {
        add(getBtns());
    }

    private HorizontalLayout getBtns() {
        Button usersBtn = new Button("Uživatelé");
        Button companiesBtn = new Button("Společnosti");

        usersBtn.addClickListener(e-> UI.getCurrent().navigate(AdminUserView.class));
        companiesBtn.addClickListener(e->UI.getCurrent().navigate(AdminCompanyView.class));

        return new HorizontalLayout(usersBtn, companiesBtn);
    }
}
