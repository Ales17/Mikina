package com.example.application.data.service;

import com.example.application.data.entity.Country;
import com.example.application.data.entity.Guest;
import com.example.application.data.entity.Status;
import com.example.application.data.repository.CountryRepository;
import com.example.application.data.repository.GuestRepository;
import com.example.application.data.repository.StatusRepository;

import java.time.LocalDate;
import java.util.List;

@org.springframework.stereotype.Service
/**
 * Service class for Accommodation entities.
 */
public class AccommodationService {

    private final GuestRepository guestRepository;
    private final CountryRepository countryRepository;
    private final StatusRepository statusRepository;

    public AccommodationService(GuestRepository guestRepository,
                                CountryRepository countryRepository,
                                StatusRepository statusRepository) {
        this.guestRepository = guestRepository;
        this.countryRepository = countryRepository;
        this.statusRepository = statusRepository;
    }

    public List<Guest> searchForGuests(String stringFilter, LocalDate arrivedFilter, LocalDate leftFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return guestRepository.search("%", arrivedFilter, leftFilter);
        } else {
            return guestRepository.search(stringFilter, arrivedFilter, leftFilter);
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

    public List<Guest> findAllGuests2() {
        return guestRepository.findAll();
    }
}