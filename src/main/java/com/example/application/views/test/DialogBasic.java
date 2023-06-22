package com.example.application.views.test;

import com.example.application.data.entity.Guest;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
@Route("dialog-basic")
public class DialogBasic extends Div {
    Binder<Guest> binder = new BeanValidationBinder<>(Guest.class);

    public DialogBasic() {
        // tag::snippet[]
        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("New employee");

        VerticalLayout dialogLayout = createDialogLayout();
        dialog.add(dialogLayout);

        Button saveButton = createSaveButton(dialog);
        Button cancelButton = new Button("Storno", e -> dialog.close());
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);

        Button button = new Button("Show dialog", e -> dialog.open());

        add(dialog, button);
        // end::snippet[]

        dialog.open();

        // Center the button within the example
        getStyle().set("position", "fixed").set("top", "0").set("right", "0")
                .set("bottom", "0").set("left", "0").set("display", "flex")
                .set("align-items", "center").set("justify-content", "center");
    }
    private void setGuest(Guest guest) {
        binder.setBean(guest);
    }
    private static VerticalLayout createDialogLayout() {

        TextField firstNameField = new TextField("Jméno");
        TextField lastNameField = new TextField("Příjmení");
        DatePicker dateArrived = new DatePicker("Datum příchodu");
        DatePicker dateLeft = new DatePicker("Datum odchodu");
        VerticalLayout dialogLayout = new VerticalLayout(firstNameField,
                lastNameField, dateArrived, dateLeft);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        return dialogLayout;
    }

    private static Button createSaveButton(Dialog dialog) {
        Button saveButton = new Button("Add", e -> dialog.close());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        return saveButton;
    }

}
