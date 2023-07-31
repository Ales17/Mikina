package com.example.application.data.repository;

import com.example.application.data.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.util.List;
/**
 * Repository for guests
 */
public interface GuestRepository extends JpaRepository<Guest, Long> {
    /**
     * Search for guests by name containing search term
     * @param searchTerm
     * @return List of guests
     */
    @Query("select c from Guest c " +
            "where (lower(c.firstName) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.lastName) like lower(concat('%', :searchTerm, '%'))) AND (c.dateArrived >= :arrived AND c.dateLeft <= :left)")
    List<Guest> search(@Param("searchTerm") String searchTerm, @Param("arrived") LocalDate arrived, @Param("left") LocalDate left);

    @Query("SELECT c FROM Guest c " +
            "WHERE (:searchTerm IS NULL OR lower(c.firstName) LIKE lower(:searchTerm) OR lower(c.lastName) LIKE lower(:searchTerm)) " +
            "AND (:arrivedFilter IS NULL OR c.dateArrived BETWEEN :arrivedFilter AND :leftFilter) " +
            "AND (:leftFilter IS NULL OR c.dateLeft BETWEEN :arrivedFilter AND :leftFilter)")
    List<Guest> searchImproved(@Param("searchTerm") String searchTerm, @Param("arrivedFilter") LocalDate arrivedFilter, @Param("leftFilter") LocalDate leftFilter);
    @Query("SELECT c FROM Guest c " +
            "WHERE (:searchTerm IS NULL OR lower(c.firstName) LIKE lower(:searchTerm) OR lower(c.lastName) LIKE lower(:searchTerm)) " +
            "AND (:arrivedFilter IS NULL OR c.dateArrived >= :arrivedFilter) " +
            "AND (:leftFilter IS NULL OR c.dateLeft <= :leftFilter)")
    List<Guest> searchGuests(@Param("searchTerm") String searchTerm, @Param("arrivedFilter") LocalDate arrivedFilter, @Param("leftFilter") LocalDate leftFilter);


}