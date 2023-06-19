package com.example.application.views.guest;

import com.example.application.data.entity.Country;
import com.example.application.data.entity.Guest;
import com.example.application.data.entity.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GuestFormTest {
    private List<Country> countries;
    private List<Status> statuses;
    private Guest danielPalecek;
    private Country country1;
    private Country country2;
    private Status status1;
    private Status status2;
    private LocalDate birthDate1;
    private LocalDate dateArrived1;
    private LocalDate dateLeft1;
    @BeforeEach
    public void setupData() {
        countries = new ArrayList<>();
        country1 = new Country();
        country1.setCountryName("Německo");
        country2 = new Country();
        country2.setCountryName("Albánie");
        countries.add(country1);
        countries.add(country2);

        statuses = new ArrayList<>();
        status1 = new Status();
        status1.setName("Contacted");
        status2 = new Status();
        status2.setName("Customer");
        statuses.add(status1);
        statuses.add(status2);
        birthDate1 = LocalDate.of(2001,01,02);
        dateArrived1 = LocalDate.of(2023,06,01);
        dateLeft1 = LocalDate.of(2023,06,19);
        danielPalecek = new Guest();
        danielPalecek.setFirstName("Daniel");
        danielPalecek.setLastName("Paleček");
        danielPalecek.setEmail("dan@paleckovi.com");
        danielPalecek.setStatus(status1);
        danielPalecek.setCountry(country2);
        danielPalecek.setBirthDate(birthDate1);
    }

    @Test
    public void formFieldsPopulated() {
        GuestForm form = new GuestForm(countries, statuses);
        form.setGuest(danielPalecek);
        assertEquals("Daniel", form.firstName.getValue());
        assertEquals("Paleček", form.lastName.getValue());
        assertEquals("dan@paleckovi.com", form.email.getValue());
        assertEquals(country2, form.country.getValue());
        assertEquals(status1, form.status.getValue());
    }

    @Test
    public void saveEventHasCorrectValues() {
        GuestForm form = new GuestForm(countries, statuses);
        Guest guest = new Guest();
        form.setGuest(guest);
        form.firstName.setValue("Michal");
        form.lastName.setValue("Havelka");
        form.country.setValue(country1);
        form.email.setValue("michal@havelkovic.com");
        form.status.setValue(status2);
        form.birthDate.setValue(birthDate1);
        form.dateArrived.setValue(dateArrived1);
        form.dateLeft.setValue(dateLeft1);
        AtomicReference<Guest> savedGuestRef = new AtomicReference<>(null);
        form.addSaveListener(e -> savedGuestRef.set(e.getGuest()));
        form.save.click();
        Guest savedGuest = savedGuestRef.get();

        assertEquals("Michal", savedGuest.getFirstName());
        assertEquals("Havelka", savedGuest.getLastName());
        assertEquals("michal@havelkovic.com", savedGuest.getEmail());
        assertEquals(birthDate1, savedGuest.getBirthDate());
        assertEquals(country1, savedGuest.getCountry());
        assertEquals(status2, savedGuest.getStatus());
    }
}