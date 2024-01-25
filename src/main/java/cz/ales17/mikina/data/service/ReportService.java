package cz.ales17.mikina.data.service;
import cz.ales17.mikina.data.entity.Company;
import cz.ales17.mikina.data.entity.Guest;

import java.util.List;

public interface ReportService {

    byte[] getReportBytes(Company c, List<Guest> guests) throws Exception;
}
