package cz.ales17.mikina.data.service;
import cz.ales17.mikina.data.entity.Guest;

import java.util.List;

public interface ReportService {
    byte[] getReportBytes(String content, List<Guest> guests) throws Exception;
}
