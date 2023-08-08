package com.example.application.views;

import com.example.application.security.SecurityService;
import com.example.application.test.PdfGenerationView;
import com.example.application.views.country.CountryView;
import com.example.application.views.dashboard.DashboardView;
import com.example.application.views.guest.GuestView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * MainLayout
 * <p>
 * Layout of the application.
 * Contains header and drawer.
 * Header contains title, toggle button and logout button.
 * </p>
 */
public class MainLayout extends AppLayout {
    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }

    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.add(
                createTab(VaadinIcon.DASHBOARD, "Hlavní panel", DashboardView.class),
                createTab(VaadinIcon.BOOK, "Evidenční kniha", GuestView.class),
                createTab(VaadinIcon.GLOBE_WIRE, "Země", CountryView.class),
                createTab(VaadinIcon.ANCHOR, "Testovací", PdfGenerationView.class)

        );
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }

    private Tab createTab(VaadinIcon viewIcon, String viewName, Class<? extends Component> viewClass) {
        Icon icon = viewIcon.create();
        icon.getStyle().set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");
        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));
        link.setRoute(viewClass);
        link.setTabIndex(-1);
        return new Tab(link);
    }

    private void createHeader() {
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("Ubytovací systém");
        title.getStyle().set("font-size", "var(--lumo-font-size-1)")/*.set("margin", "0")*/;
        String loggedUser = securityService.getAuthenticatedUser().getUsername();
        Button logout = new Button("Odhlásit se (" + loggedUser + ")", e -> securityService.logout());
        logout.getStyle().set("margin-right", "16px");
        var header = new HorizontalLayout(toggle, title, logout);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(title);
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);
        addToNavbar(header);
    }

    private void createDrawer() {
        Tabs tabs = getTabs();
        addToDrawer(tabs);
        setPrimarySection(Section.DRAWER);
    }
}