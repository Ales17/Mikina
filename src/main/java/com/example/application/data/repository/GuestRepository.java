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



    @Query("select c from Guest c where c.dateArrived >= :arrived and c.dateLeft <= :left and (lower(c.firstName) like lower(concat('%', :searchTerm, '%')) or lower(c.lastName) like lower(concat('%', :searchTerm, '%')) )")
    List<Guest> search2(@Param("searchTerm") String searchTerm,@Param("arrived")LocalDate arrived, @Param("left")LocalDate left);
}