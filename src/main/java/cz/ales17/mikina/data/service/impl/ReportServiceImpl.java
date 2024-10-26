package cz.ales17.mikina.data.service.impl;

import cz.ales17.mikina.data.model.Company;
import cz.ales17.mikina.data.model.Report;
import cz.ales17.mikina.data.model.ReportType;
import cz.ales17.mikina.data.repository.ReportRepository;
import cz.ales17.mikina.data.service.ReportService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public List<Report> getReportsByCompany(Company c) {
        return reportRepository.findAllByCompanyIs(c);
    }

    @Override
    public void saveReport(byte[] bytes, ReportType type, String filename, Company company) {
        Report report = Report.builder()
                .bytes(bytes)
                .company(company)
                .fileName(filename)
                .type(type)
                .build();
        reportRepository.save(report);
    }
}
