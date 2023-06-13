package com.example.application.views;

import com.example.application.security.AuthenticatedUser;
import com.example.application.views.country.CountryView;
import com.example.application.views.guest.GuestView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout {
    private AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Ubytovací systém");
        logo.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM);

        //String u = securityService.getAuthenticatedUser().getUsername();
        //Button logout = new Button("Odhlášení (" + u + ")", e -> securityService.logout());
        Button logout = new Button("Odhlásit se (" + authenticatedUser.getUsername() + ")",  e -> {

            authenticatedUser.logout();
        });

        var header = new HorizontalLayout(new DrawerToggle(), logo, logout);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header);

    }

    private void createDrawer() {
        addToDrawer(new VerticalLayout(
                new RouterLink("Hlavní panel", DashboardView.class),
                new RouterLink("Seznam hostů", GuestView.class),
                new RouterLink("Seznam zemí", CountryView.class)
        ));
    }
}