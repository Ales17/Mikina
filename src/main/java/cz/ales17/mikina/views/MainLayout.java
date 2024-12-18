package cz.ales17.mikina.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;
import cz.ales17.mikina.data.model.Role;
import cz.ales17.mikina.data.model.UserEntity;
import cz.ales17.mikina.security.AuthenticatedUser;
import cz.ales17.mikina.views.admin.company.AdminCompanyView;
import cz.ales17.mikina.views.admin.user.AdminUserView;
import cz.ales17.mikina.views.dashboard.DashboardView;
import cz.ales17.mikina.views.guest.GuestView;
import cz.ales17.mikina.views.report.ReportView;
import cz.ales17.mikina.views.user.UserView;

import java.io.ByteArrayInputStream;
import java.util.Optional;

/**
 * MainLayout
 * <p>
 * Layout of the application.
 * Contains header and drawer.
 * Header contains title, toggle button and logout button.
 * </p>
 */
public class MainLayout extends AppLayout {
    private final AuthenticatedUser authenticatedUser;

    public MainLayout(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        createHeader();
        createDrawer();
    }

    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.add(
                createTab(VaadinIcon.DASHBOARD, "Hlavní panel", DashboardView.class),
                createTab(VaadinIcon.BOOK, "Kniha hostů", GuestView.class),
                createTab(VaadinIcon.DOWNLOAD, "Seznam reportů", ReportView.class)
        );
        Optional<UserEntity> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            if (maybeUser.get().getRoles().contains(Role.ADMIN)) {
                tabs.add(
                        createTab(VaadinIcon.USER, "Uživatelé", AdminUserView.class),
                        createTab(VaadinIcon.HOME, "Ubytovatelé", AdminCompanyView.class)
                );
            }
        }
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


        var header = new HorizontalLayout(toggle, title, createUserInfo());
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

        setPrimarySection(Section.DRAWER);

        addToDrawer(tabs);
    }


    private Footer createUserInfo() {
        Footer layout = new Footer();

        Optional<UserEntity> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            UserEntity user = maybeUser.get();

            Avatar avatar = new Avatar(user.getName());
            avatar.setThemeName("xsmall");
            avatar.getElement().setAttribute("tabindex", "-1");

            // If user's picture is set in database
            if (user.getProfilePicture() != null) {
                StreamResource resource = new StreamResource("profile-pic",
                        () -> new ByteArrayInputStream(user.getProfilePicture()));
                avatar.setImageResource(resource);
            }

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);
            div.add(user.getName());
            div.add(new Icon("lumo", "dropdown"));
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
            userName.add(div);
            userName.getSubMenu().addItem("Odhlásit se", e -> authenticatedUser.logout());
            userName.getSubMenu().addItem("Moje údaje", e -> UI.getCurrent().navigate(UserView.class));

            layout.add(userMenu);
            layout.getStyle().set("margin-right", "var(--lumo-space-m)");
        }
        return layout;
    }
}
