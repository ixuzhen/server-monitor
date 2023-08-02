package cn.luckynow.monitoringserver;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

/**
 * @description:
 * @author：xz
 * @date: 2023/8/2
 */
public class Test01 {

    @Test
    public void testDate(){
        String dateString = "Fri Jul 21 18:22:29 2023";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy", Locale.ENGLISH);
        try {
            LocalDateTime parsedDateTime = LocalDateTime.parse(dateString, formatter);
            Timestamp timestamp = Timestamp.from(parsedDateTime.toInstant(java.time.ZoneOffset.ofHours(8)));
            System.out.println("Parsed date and time: " + parsedDateTime);
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing date and time: " + e.getMessage());
        }
    }

}
