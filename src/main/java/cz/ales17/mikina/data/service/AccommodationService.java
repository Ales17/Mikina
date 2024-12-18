package cz.ales17.mikina.data.service;

import cz.ales17.mikina.data.model.Company;
import cz.ales17.mikina.data.model.Guest;

import java.util.List;

public interface AccommodationService {
    void deleteGuest(Guest guest);

    void saveGuest(Guest toBeSaved);

    void saveCompany(Company toBeSaved);

    List<Guest> findAllGuests();

    List<Guest> findAllForeigners();

    List<Company> findAllCompanies();

    void duplicateGuest(Guest originalGuest);

    Integer averageDaysOfStay(Company c);

    int totalGuestCount(Company c);
}
