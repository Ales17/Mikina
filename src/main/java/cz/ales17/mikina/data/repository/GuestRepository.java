package cz.ales17.mikina.data.repository;

import cz.ales17.mikina.data.model.Company;
import cz.ales17.mikina.data.model.Guest;
import cz.geek.ubyport.StatniPrislusnost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for guests
 */
public interface GuestRepository extends JpaRepository<Guest, Long> {
    /**
     * Search for guests by name containing search term
     *
     * @param searchTerm guest's given or family name (part of)
     * @return List of guests
     */

    @Query("SELECT g FROM Guest g " +
            "WHERE (:searchTerm IS NULL OR lower(g.firstName) LIKE lower(:searchTerm) OR lower(g.lastName) LIKE lower(:searchTerm)) " +
            "AND (:arrivedFilter IS NULL OR g.dateArrived >= :arrivedFilter) " +
            "AND (:leftFilter IS NULL OR g.dateLeft <= :leftFilter) " +
            "AND (:foreignersOnly = FALSE OR g.nationality <> 0) " +
            "AND (:company IS NULL OR g.company = :company)")
    List<Guest> searchGuests(@Param("searchTerm") String searchTerm,
                             @Param("arrivedFilter") LocalDate arrivedFilter,
                             @Param("leftFilter") LocalDate leftFilter,
                             @Param("foreignersOnly") boolean foreignersOnly,
                             @Param("company") Company company);


    List<Guest> findGuestsByNationalityIsNot(StatniPrislusnost nationality);

}