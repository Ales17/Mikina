package cz.ales17.mikina.util;

import cz.ales17.mikina.data.model.Company;
import cz.ales17.mikina.data.model.Guest;

import java.util.List;

public interface ReportGenerator {

    byte[] getReportBytes(Company c, List<Guest> guests) throws Exception;
}
