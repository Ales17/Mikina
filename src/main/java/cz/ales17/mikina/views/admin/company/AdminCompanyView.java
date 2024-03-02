package cz.ales17.mikina.views.admin.company;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import cz.ales17.mikina.data.model.Company;
import cz.ales17.mikina.data.service.impl.AccommodationServiceImpl;
import cz.ales17.mikina.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed({"ROLE_ADMIN"})
@Route(value = "companies", layout = MainLayout.class)
@PageTitle("Správa firem")
public class AdminCompanyView extends VerticalLayout {
    private final AccommodationServiceImpl service;
    private final Grid<Company> companyGrid = new Grid<>(Company.class);
    private final Button addCompanyBtn = new Button("Přidat společnost");
    private AdminCompanyForm form;

    public AdminCompanyView(AccommodationServiceImpl service) {
        this.service = service;
        setSizeFull();
        configureCompanyGrid();
        configureCompanyForm();

        add(getToolbar(), getContent());
        updateCompanyGrid();
        closeEditor();
    }

    private Component getToolbar() {
        addCompanyBtn.addClickListener(click -> addCompany());
        return new HorizontalLayout(addCompanyBtn);
    }

    private void configureCompanyGrid() {
        companyGrid.setSizeFull();
        companyGrid.setColumns("id", "name", "municipality");
        companyGrid.asSingleSelect().addValueChangeListener(e -> editCompany(e.getValue()));
    }

    private void configureCompanyForm() {
        form = new AdminCompanyForm();
        form.setWidth("30em");
        form.addDeleteListener(this::deleteCompany);
        form.addSaveListener(this::saveCompany);
        form.addCloseListener(e -> closeEditor());
    }

    private void addCompany() {
        companyGrid.asSingleSelect().clear();
        editCompany(new Company());
    }

    private void saveCompany(AdminCompanyForm.SaveEvent e) {
        service.saveCompany(e.getCompany());
        updateCompanyGrid();
        closeEditor();
    }

    private void deleteCompany(AdminCompanyForm.DeleteEvent e) {
        service.saveCompany(e.getCompany());
        updateCompanyGrid();
        closeEditor();
    }

    private void editCompany(Company c) {
        if (c == null) {
            closeEditor();
        } else {
            form.setCompany(c);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setCompany(null);
        form.setVisible(false);
        removeClassName("editing");
    }


    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(companyGrid, form);
        content.setSizeFull();
        return content;
    }

    private void updateCompanyGrid() {
        companyGrid.setItems(service.findAllCompanies());
    }
}
