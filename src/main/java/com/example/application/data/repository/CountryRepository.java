package com.example.application.data.repository;

import com.example.application.data.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for countries
 */
public interface CountryRepository extends JpaRepository<Country, Long> {
    /**
     * Search for countries by name containing search term
     *
     * @param searchTerm
     * @return List of countries
     */
    @Query("select f from Country f  where f.countryName  " +
            "like lower(concat('%', :searchTerm, '%'))")
    List<Country> search(@Param("searchTerm") String searchTerm);


}
