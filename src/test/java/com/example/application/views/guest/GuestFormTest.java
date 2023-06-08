package com.example.application.views.guest;

import com.example.application.data.entity.Country;
import com.example.application.data.entity.Guest;
import com.example.application.data.entity.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GuestFormTest {
    private List<Country> countries;
    private List<Status> statuses;
    private Guest marcUsher;
    private Country country1;
    private Country country2;
    private Status status1;
    private Status status2;

    @BeforeEach
    public void setupData() {
        countries = new ArrayList<>();
        country1 = new Country();
        country1.setCountryName("Czech Republic");
        country2 = new Country();
        country2.setCountryName("Germany");
        countries.add(country1);
        countries.add(country2);

        statuses = new ArrayList<>();
        status1 = new Status();
        status1.setName("Status 1");
        status2 = new Status();
        status2.setName("Status 2");
        statuses.add(status1);
        statuses.add(status2);

        marcUsher = new Guest();
        marcUsher.setFirstName("Marc");
        marcUsher.setLastName("Usher");
        marcUsher.setEmail("marc@usher.com");
        marcUsher.setStatus(status1);
        marcUsher.setCountry(country2);
    }

    @Test
    public void formFieldsPopulated() {
        GuestForm form = new GuestForm(countries, statuses);
        form.setGuest(marcUsher);
        assertEquals("Marc", form.firstName.getValue());
        assertEquals("Usher", form.lastName.getValue());
        assertEquals("marc@usher.com", form.email.getValue());
        assertEquals(country2, form.country.getValue());
        assertEquals(status1, form.status.getValue());
    }

    @Test
    public void saveEventHasCorrectValues() {
        GuestForm form = new GuestForm(countries, statuses);
        Guest guest = new Guest();
        form.setGuest(guest);
        form.firstName.setValue("John");
        form.lastName.setValue("Doe");
        form.country.setValue(country1);
        form.email.setValue("john@doe.com");
        form.status.setValue(status2);

        AtomicReference<Guest> savedContactRef = new AtomicReference<>(null);
        form.addSaveListener(e -> savedContactRef.set(e.getGuest()));
        form.save.click();
        Guest savedGuest = savedContactRef.get();

        assertEquals("John", savedGuest.getFirstName());
        assertEquals("Doe", savedGuest.getLastName());
        assertEquals("john@doe.com", savedGuest.getEmail());
        assertEquals(country1, savedGuest.getCountry());
        assertEquals(status2, savedGuest.getStatus());
    }
}