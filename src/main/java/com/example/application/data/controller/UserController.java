package com.example.application.data.controller;


import com.example.application.data.entity.Guest;
import com.example.application.data.service.AccommodationService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UserController {

    private AccommodationService accommodationService;

    public UserController(AccommodationService accommodationService) {
        this.accommodationService = accommodationService;
    }

    @GetMapping("/export-users")
    public void exportCSV(HttpServletResponse response) throws Exception {

        //set file name and content type
        String filename = "users.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        //create a csv writer
        StatefulBeanToCsv<Guest> writer = new StatefulBeanToCsvBuilder<Guest>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();

        //write all users to csv file
        writer.write(accommodationService.findAllGuests(null));
                
    }
}