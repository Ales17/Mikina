package cz.ales17.mikina.views.guest;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class ExportDialog extends Dialog {
    final Anchor pdfBtn = new Anchor("#");
    final Anchor unlBtn = new Anchor("#");
    final Button close = new Button("Storno", e -> close());


    public ExportDialog() {
        setHeaderTitle("Exportovat");
        pdfBtn.add(new Button("PDF", new Icon(VaadinIcon.DOWNLOAD_ALT)));
        pdfBtn.setEnabled(false);
        unlBtn.add(new Button("UNL (Ubyport)", new Icon(VaadinIcon.DOWNLOAD_ALT)));
        unlBtn.setEnabled(false);
        HorizontalLayout dialogToolbar = new HorizontalLayout();
        dialogToolbar.add(pdfBtn, unlBtn, close);
        add(dialogToolbar);
    }
}
