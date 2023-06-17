package com.example.application.data.service;

import com.example.application.data.entity.Country;
import com.example.application.data.entity.Guest;
import com.example.application.data.entity.Status;
import com.example.application.data.repository.CountryRepository;
import com.example.application.data.repository.GuestRepository;
import com.example.application.data.repository.StatusRepository;

import java.util.List;

@org.springframework.stereotype.Service
public class Service {

    private final GuestRepository guestRepository;
    private final CountryRepository countryRepository;
    private final StatusRepository statusRepository;

    public Service(GuestRepository guestRepository,
                   CountryRepository countryRepository,
                   StatusRepository statusRepository) {
        this.guestRepository = guestRepository;
        this.countryRepository = countryRepository;
        this.statusRepository = statusRepository;
    }

    public List<Guest> findAllGuests(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return guestRepository.findAll();
        } else {
            return guestRepository.search(stringFilter);
        }
    }

    public List<Country> findAllCountries() {
        return countryRepository.findAll();
    }

    public List<Country> findGuestsCountries() {
        return countryRepository.findGuestsCountries();
    }

    public List<Country> fincCountriesByName(String filter) {
        return countryRepository.search(filter);
    }

    public int countGuests() {
        return (int) guestRepository.count();
    }

    public void deleteGuest(Guest guest) {
        guestRepository.delete(guest);
    }

    public void saveGuest(Guest guest) {
        if (guest == null) {
            System.err.println("Host je null. Jsi si jistý, že jsi připojil formulář k aplikaci?");
            return;
        }
        guestRepository.save(guest);
    }


    public List<Status> findAllStatuses() {
        return statusRepository.findAll();
    }


    public void saveCountry(Country country) {
        if (country == null) {
            System.err.println("Země je null. Jsi si jistý, že jsi připojil formulář k aplikaci?");
            return;
        }
        countryRepository.save(country);
    }

    public void deleteCountry(Country country) {
        countryRepository.delete(country);
    }
}