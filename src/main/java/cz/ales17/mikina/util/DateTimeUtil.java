package cz.ales17.mikina.util;

import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public static DateTimeFormatter dateTimeFormatter = java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    public static DateTimeFormatter unlDateFormatter = java.time.format.DateTimeFormatter.ofPattern("ddMMyyHHmm");
    public static DateTimeFormatter gridDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static DateTimeFormatter pdfDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
    public static DateTimeFormatter printDateFormatter = DateTimeFormatter.ofPattern("dd. MM. yyyy HH.mm");

}
