package cz.ales17.mikina.views.guest;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;

public class DeleteDialog extends ConfirmDialog {
    public DeleteDialog() {
        setHeader("Odstranit hosta");
        setText("Opravdu chcete odstranit hosta?");
        setConfirmText("Odstranit");
        setConfirmButtonTheme("error primary");

        setCancelable(true);
        setCancelText("Zrušit");

    }
}
