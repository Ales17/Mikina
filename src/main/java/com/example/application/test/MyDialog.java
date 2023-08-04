package com.example.application.test;

import com.example.application.data.service.AccommodationService;
import com.example.application.views.guest.GuestForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
@Route("dialog")
public class MyDialog extends Div {
    AccommodationService accommodationService;

    GuestForm form;
    public MyDialog(AccommodationService accommodationService) {
        this.accommodationService = accommodationService;

        // tag::snippet[]
        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("Karta hosta");

        form = new GuestForm(accommodationService.findAllCountries());
        dialog.add(form);

        Button button = new Button("Show dialog", e -> dialog.open());

        add(dialog, button);
        // end::snippet[]

//        dialog.open();

        // Center the button within the example
        getStyle().set("position", "fixed").set("top", "0").set("right", "0")
                .set("bottom", "0").set("left", "0").set("display", "flex")
                .set("align-items", "center").set("justify-content", "center");
    }


}
