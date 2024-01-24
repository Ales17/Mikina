package cz.ales17.mikina.data.repository;

import cz.ales17.mikina.data.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {

}
