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
public class MyDialog extends Dialog {
     public Anchor pdfBtn = new Anchor("#");
    public Anchor unlBtn = new Anchor("#");
    public Button close = new Button("Storno", e-> close());

    public Anchor getPdfBtn() {
        return pdfBtn;
    }

    public void setPdfBtn(Anchor pdfBtn) {
        this.pdfBtn = pdfBtn;
    }

    public Anchor getUnlBtn() {
        return unlBtn;
    }

    public void setUnlBtn(Anchor unlBtn) {
        this.unlBtn = unlBtn;
    }

    public Button getClose() {
        return close;
    }

    public void setClose(Button close) {
        this.close = close;
    }

    public MyDialog(   ) {
         setHeaderTitle("Exportovat");
        pdfBtn.add(new Button("PDF", new Icon(VaadinIcon.DOWNLOAD_ALT)));
        pdfBtn.setEnabled(false);
        unlBtn.add(new Button("UNL (Ubyport)", new Icon(VaadinIcon.DOWNLOAD_ALT)));
        unlBtn.setEnabled(false);
        HorizontalLayout dialogToolbar = new HorizontalLayout();
        dialogToolbar.add(pdfBtn, unlBtn, close);
        add(dialogToolbar);
        open();
    }



}
