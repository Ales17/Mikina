package cz.ales17.mikina.data.service.impl;

import cz.ales17.mikina.data.model.Company;
import cz.ales17.mikina.data.model.Guest;
import cz.ales17.mikina.data.repository.CompanyRepository;
import cz.ales17.mikina.data.repository.GuestRepository;
import cz.ales17.mikina.data.service.AccommodationService;
import cz.geek.ubyport.StatniPrislusnost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


/**
 * Service class for Accommodation entities.
 */
@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {

    private final GuestRepository guestRepository;
    private final CompanyRepository companyRepository;

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

    @Override
    public void deleteGuest(Guest guest) {
        guestRepository.delete(guest);
    }

    @Override
    public void saveGuest(Guest toBeSaved) {
        if (toBeSaved == null) {
            System.err.println("Guest is null.");
            return;
        }
        guestRepository.save(toBeSaved);
    }

    @Override
    public void saveCompany(Company toBeSaved) {
        companyRepository.save(toBeSaved);
    }

    @Override
    public List<Guest> findAllGuests() {
        return guestRepository.findAll();
    }

    @Override
    public List<Guest> findAllForeigners() {
        return guestRepository.findGuestsByNationalityIsNot(StatniPrislusnost.CZE);
    }

    @Override
    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
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

    @Override
    public Integer averageDaysOfStay(Company c) {
        return guestRepository.averageDaysStay(c.getId());
    }

    @Override
    public int totalGuestCount(Company c) {
        return guestRepository.countGuestsByCompanyIs(c);
    }

}
