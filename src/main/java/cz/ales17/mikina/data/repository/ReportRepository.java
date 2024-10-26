package cz.ales17.mikina.data.repository;

import cz.ales17.mikina.data.model.Company;
import cz.ales17.mikina.data.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByCompanyIs(Company c);
}
