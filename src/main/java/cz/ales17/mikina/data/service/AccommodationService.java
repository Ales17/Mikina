package cz.ales17.mikina.data.service;

import cz.ales17.mikina.data.entity.Company;
import cz.ales17.mikina.data.entity.Guest;
import cz.ales17.mikina.data.repository.CompanyRepository;
import cz.ales17.mikina.data.repository.GuestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


/**
 * Service class for Accommodation entities.
 */
@Service
public class AccommodationService {

    private final GuestRepository guestRepository;
    private final CompanyRepository companyRepository;

    public AccommodationService(GuestRepository guestRepository,
                                CompanyRepository companyRepository) {
        this.guestRepository = guestRepository;
        this.companyRepository = companyRepository;
    }

    public List<Guest> searchForAllGuests(String stringFilter, LocalDate arrivedFilter, LocalDate leftFilter) {
        return searchGuests(stringFilter, arrivedFilter, leftFilter, false, null);
    }

    public List<Guest> searchForForeignGuests(String stringFilter, LocalDate arrivedFilter, LocalDate leftFilter) {
        return searchGuests(stringFilter, arrivedFilter, leftFilter, true, null);
    }

    public List<Guest> searchGuests(String stringFilter, LocalDate arrivedFilter, LocalDate leftFilter, boolean foreignersOnly, Company company) {
        String searchTerm = (stringFilter != null && !stringFilter.isEmpty()) ? "%" + stringFilter.toLowerCase() + "%" : null;
        return guestRepository.searchGuests(searchTerm, arrivedFilter, leftFilter, foreignersOnly, company);
    }


    public void deleteGuest(Guest guest) {
        guestRepository.delete(guest);
    }

    public void duplicateGuest(Guest originalGuest) {
        Guest duplicatedGuest = new Guest();
        duplicatedGuest.setId(null);
        duplicatedGuest.setFirstName(originalGuest.getFirstName());
        duplicatedGuest.setLastName(originalGuest.getLastName());
        duplicatedGuest.setAddress(originalGuest.getAddress());
        duplicatedGuest.setBirthDate(originalGuest.getBirthDate());
        duplicatedGuest.setIdNumber(originalGuest.getIdNumber());
        duplicatedGuest.setNationality(originalGuest.getNationality());
        duplicatedGuest.setCompany(originalGuest.getCompany());
        guestRepository.save(duplicatedGuest);
    }

    public void saveGuest(Guest toBeSaved) {
        if (toBeSaved == null) {
            System.err.println("Guest is null.");
            return;
        }
        guestRepository.save(toBeSaved);
    }

    public void saveCompany(Company toBeSaved) {
        companyRepository.save(toBeSaved);
    }

    public List<Guest> findAllGuests() {
        return guestRepository.findAll();
    }

    public List<Guest> findAllForeigners() {
        return guestRepository.findAllForeigners();
    }

    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }
}
