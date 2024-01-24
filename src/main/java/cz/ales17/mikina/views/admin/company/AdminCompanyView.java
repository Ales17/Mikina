package cz.ales17.mikina.views.admin.company;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import cz.ales17.mikina.data.entity.Company;
import cz.ales17.mikina.data.service.AccommodationService;
import cz.ales17.mikina.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed({"ROLE_ADMIN"})
@Route(value = "admin/companies", layout = MainLayout.class)
@PageTitle("Správa firem | Ubytovací systém")
public class AdminCompanyView extends VerticalLayout {
    private final AccommodationService service;
    private final Grid<Company> companyGrid = new Grid<>(Company.class);
    private AdminCompanyForm form;

    public AdminCompanyView(AccommodationService service) {
        this.service = service;
        setSizeFull();
        configureCompanyGrid();
        configureCompanyForm();

        add(getContent());
        updateCompanyGrid();
        closeEditor();
    }

    private void configureCompanyGrid() {
        companyGrid.setSizeFull();
        companyGrid.setColumns("id", "name", "municipality");
        companyGrid.asSingleSelect().addValueChangeListener(e-> editCompany(e.getValue()));
    }

    private void configureCompanyForm() {
        form = new AdminCompanyForm(service);
        form.setWidth("30em");
        form.addDeleteListener(this::deleteCompany);
        form.addSaveListener(this::saveCompany);
        form.addCloseListener(e->closeEditor());
    }

    private void saveCompany(AdminCompanyForm.SaveEvent e) {
        service.saveCompany(e.getCompany());
        updateCompanyGrid();
        closeEditor();
    }

    private void deleteCompany(AdminCompanyForm.DeleteEvent e)
    {
        service.saveCompany(e.getCompany());
        updateCompanyGrid();
        closeEditor();
    }

    private void editCompany(Company c) {
        if(c == null) {
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

    private void updateCompanyGrid(){
        companyGrid.setItems(service.findAllCompanies());
        System.out.println(service.findAllCompanies());
    }
}
