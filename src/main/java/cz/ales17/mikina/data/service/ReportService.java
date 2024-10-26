package cz.ales17.mikina.data.service;

import cz.ales17.mikina.data.model.Company;
import cz.ales17.mikina.data.model.Report;
import cz.ales17.mikina.data.model.ReportType;

import java.util.List;

public interface ReportService {
    List<Report> getReportsByCompany(Company c);

    void saveReport(byte[] bytes, ReportType type, String filename, Company company);
}
