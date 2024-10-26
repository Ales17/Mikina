package cz.ales17.mikina.views.guest;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ExportDialog extends Dialog {
    final Anchor pdfAnchor = new Anchor("#");
    final Anchor unlAnchor = new Anchor("#");
    final Button close = new Button("Storno", e -> close());


    public ExportDialog() {
        setHeaderTitle("Exportovat");
        HorizontalLayout dialogText = new HorizontalLayout();
        Paragraph text = new Paragraph("Reporty byly uloženy do sekce Historie reportů");
        dialogText.add(text);
        Button pdfBtn = new Button("PDF", new Icon(VaadinIcon.DOWNLOAD_ALT));
        pdfBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button unlBtn = new Button("UNL (Ubyport)", new Icon(VaadinIcon.DOWNLOAD_ALT));
        unlBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        pdfAnchor.add(pdfBtn);
        pdfAnchor.setEnabled(false);

        unlAnchor.add(unlBtn);
        unlAnchor.setEnabled(false);

        HorizontalLayout dialogToolbar = new HorizontalLayout();
        dialogToolbar.add(pdfAnchor, unlAnchor, close);

        VerticalLayout verticalLayout = new VerticalLayout(dialogText, dialogToolbar);
        add(verticalLayout);
    }
}
