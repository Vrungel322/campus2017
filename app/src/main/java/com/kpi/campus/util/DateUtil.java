package com.kpi.campus.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 29.03.2016.
 */
public class DateUtil {

    /**
     * format of the date
     */
    private static String format = "dd/MM/yyyy";

    /**
     * Get current date in specified format
     * @return current date
     */
    public static String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * Convert string into date object
     * @param date string
     * @return date object
     */
    public static Date convert(String date) {
        try {
            Date thedate = new SimpleDateFormat(format, Locale.GERMAN).parse(date);
            return thedate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
