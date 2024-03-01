package cz.ales17.mikina.data.service;

import cz.ales17.mikina.data.entity.Company;
import cz.ales17.mikina.data.entity.Guest;

import java.util.List;

public interface AccommodationService {
    void deleteGuest(Guest guest);

    void saveGuest(Guest toBeSaved);

    void saveCompany(Company toBeSaved);

    List<Guest> findAllGuests();

    List<Guest> findAllForeigners();

    List<Company> findAllCompanies();

    void duplicateGuest(Guest originalGuest);
}
