package com.meetlive.app.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormatter {

    private static String inputdateFormat = "yyyy/MM/dd";
    private static String outputdateFormat = "dd MMM yyyy";


    private static final com.meetlive.app.utils.DateFormatter ourInstance = new com.meetlive.app.utils.DateFormatter();

    public static com.meetlive.app.utils.DateFormatter getInstance() {
        return ourInstance;
    }

    public DateFormatter() {
    }


    public String format(String date) {
        DateFormat sdfIn = new SimpleDateFormat(inputdateFormat, Locale.US);
        DateFormat sdfOut = new SimpleDateFormat(outputdateFormat, Locale.US);

        Date dateIn = null;
        try {
            dateIn = sdfIn.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sdfOut.format(dateIn);
    }


    public String formatDateTime(String date) {
        DateFormat sdfOut = new SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.US);
        String inFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        DateFormat sdfIn = new SimpleDateFormat(inFormat, Locale.US);
        sdfIn.setTimeZone(TimeZone.getTimeZone("UTC"));


        Date dateIn = null;
        try {
            dateIn = sdfIn.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sdfOut.format(dateIn);
    }

    public String formatDate(String date) {
        DateFormat sdfOut = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        String inFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        DateFormat sdfIn = new SimpleDateFormat(inFormat, Locale.US);
        sdfIn.setTimeZone(TimeZone.getTimeZone("UTC"));


        Date dateIn = null;
        try {
            dateIn = sdfIn.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sdfOut.format(dateIn);
    }
}
