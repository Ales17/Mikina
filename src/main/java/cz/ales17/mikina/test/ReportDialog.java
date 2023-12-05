package cz.ales17.mikina.test;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
@Route("dialog")
public class ReportDialog extends Dialog {
    public Anchor pdfExportBtn = new Anchor("#");
    public Anchor unlExportBtn = new Anchor("#");
    public Button closeDialogBtn = new Button("Storno", e -> close());

    public ReportDialog() {
        setHeaderTitle("Exportovat");
        prepareExportBtns();
        HorizontalLayout dialogToolbar = new HorizontalLayout();
        dialogToolbar.add(pdfExportBtn, unlExportBtn, closeDialogBtn);
        add(dialogToolbar);
        open();
    }

    private void prepareExportBtns() {
        pdfExportBtn.add(new Button("PDF", new Icon(VaadinIcon.DOWNLOAD_ALT)));
        pdfExportBtn.setEnabled(false);
        unlExportBtn.add(new Button("UNL (Ubyport)", new Icon(VaadinIcon.DOWNLOAD_ALT)));
        unlExportBtn.setEnabled(false);
    }
}
