package cz.ales17.mikina.data.service;

import cz.ales17.mikina.data.entity.Country;
import cz.ales17.mikina.data.entity.Guest;
import cz.ales17.mikina.data.repository.CountryRepository;
import cz.ales17.mikina.data.repository.GuestRepository;
import cz.ales17.mikina.data.repository.StatusRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
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

    public List<Guest> searchForGuests(String stringFilter, LocalDate arrivedFilter, LocalDate leftFilter, boolean foreignersOnly) {
        String searchTerm = (stringFilter != null && !stringFilter.isEmpty()) ? "%" + stringFilter.toLowerCase() + "%" : null;
        return guestRepository.searchGuests(searchTerm, arrivedFilter, leftFilter, foreignersOnly);
    }

    public List<Guest> findAllForeigners() {
        return guestRepository.findAllForeigners();
    }

    public List<Country> findAllCountries() {
        return countryRepository.findAll();
    }

    public List<Country> findCountriesByName(String filter) {
        return countryRepository.search(filter);
    }

    public void deleteGuest(Guest guest) {
        guestRepository.delete(guest);
    }
    public void duplicateGuest(Guest guest) {
        Guest g = new Guest();
        g.setId(null);
        g.setFirstName(guest.getFirstName());
        g.setLastName(guest.getLastName());
        g.setAddress(guest.getAddress());
        g.setBirthDate(guest.getBirthDate());
        g.setIdNumber(guest.getIdNumber());
        g.setNationality(guest.getNationality());
        guestRepository.save(g);
    }
    public void saveGuest(Guest guest) {
        if (guest == null) {
            System.err.println("Host je null. Jsi si jistý, že jsi připojil formulář k aplikaci?");
            return;
        }
        guestRepository.save(guest);
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

    public List<Guest> findAllGuests() {
        return guestRepository.findAll();
    }
}