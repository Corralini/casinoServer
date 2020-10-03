package com.corral.casino.server.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private static DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);

    public static Date stringToDate(String date) throws ParseException {
        return dateFormat.parse(date);
    }

    public static String dateToString(Date date) {
        return dateFormat.format(date);
    }

}
