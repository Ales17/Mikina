package cz.ales17.mikina.views.login;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class PasswordResetDialog extends Dialog {
    public PasswordResetDialog() {
        setHeaderTitle("Zapomenuté heslo");
        add(getLayout());
    }

    private VerticalLayout getLayout() {
        Paragraph p = new Paragraph("Pro obnovu hesla kontaktujte, prosím, administrátora.");
        VerticalLayout layout = new VerticalLayout(p, getButtons());
        layout.setPadding(false);
        return layout;
    }

    private HorizontalLayout getButtons() {
        Button close = new Button("Zavřít", e -> close());
        return new HorizontalLayout(close);
    }
}
