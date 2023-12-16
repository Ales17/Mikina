package cz.ales17.mikina.data.repository;

import cz.ales17.mikina.data.entity.Guest;
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
     * @param searchTerm
     * @return List of guests
     */

    @Query("SELECT c FROM Guest c " +
            "WHERE (:searchTerm IS NULL OR lower(c.firstName) LIKE lower(:searchTerm) OR lower(c.lastName) LIKE lower(:searchTerm)) " +
            "AND (:arrivedFilter IS NULL OR c.dateArrived >= :arrivedFilter) " +
            "AND (:leftFilter IS NULL OR c.dateLeft <= :leftFilter) AND (:foreignersOnly = FALSE OR c.nationality <> 0)")
    List<Guest> searchGuests(@Param("searchTerm") String searchTerm, @Param("arrivedFilter") LocalDate arrivedFilter, @Param("leftFilter") LocalDate leftFilter, boolean foreignersOnly);


    @Query("SELECT c from Guest c where c.nationality <> 0")
    List<Guest> findAllForeigners();


}