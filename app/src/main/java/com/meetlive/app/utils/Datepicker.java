package com.meetlive.app.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Datepicker {

    int mYear, mMonth, mDay;
    String selectedDate;
    long timeStamp = 0;

    String dateFormat = "yyyy/MM/dd";
    String dateFormatForYear = "yyyy";

    public void selectDateFromCurrent(Context context, final TextView editText, final DateCallback callback) {

        final Calendar dob = Calendar.getInstance();

        mYear = dob.get(Calendar.YEAR);
        mMonth = dob.get(Calendar.MONTH);
        mDay = dob.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //etPickDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        try {
                            Date date = new Date(year - 1900, monthOfYear, dayOfMonth);

                            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
                            selectedDate = formatter.format(date);
                            editText.setText(selectedDate);
                            timeStamp = date.getTime() / 1000L;

                            callback.onDateGot(selectedDate, timeStamp);

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public void selectDateFrom(Context context, final TextView editText, String fromDate, final DateCallback callback) {

        final Calendar dob = Calendar.getInstance();

        mYear = dob.get(Calendar.YEAR);
        mMonth = dob.get(Calendar.MONTH);
        mDay = dob.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //etPickDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        try {
                            Date date = new Date(year - 1900, monthOfYear, dayOfMonth);

                            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
                            selectedDate = formatter.format(date);
                            editText.setText(selectedDate);
                            timeStamp = date.getTime() / 1000L;

                            callback.onDateGot(selectedDate, timeStamp);

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                    }
                }, mYear, mMonth, mDay);

       // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

        //  Date date = new Date(fromDate);
        //   datePickerDialog.getDatePicker().setMinDate(date.getTime() / 1000L);

      /*  String myDate = "2014/10/29 18:10:45";
        SimpleDateFormat sdf = new SimpleDateFormat(fromDate);
        Date date = null;
        try {
            date = sdf.parse(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();
        datePickerDialog.getDatePicker().setMinDate(date.getTime());
        datePickerDialog.show();*/
    }

    public void selectDob(Context context, final TextView editText, final String type) {

        final Calendar dob = Calendar.getInstance();

        mYear = dob.get(Calendar.YEAR);
        mMonth = dob.get(Calendar.MONTH);
        mDay = dob.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //etPickDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        try {
                            Date date = new Date(year - 1900, monthOfYear, dayOfMonth);

                            if (type.equalsIgnoreCase("DateOfBirth")) {

                                SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
                                selectedDate = formatter.format(date);
                                editText.setText(selectedDate);
                            } else {
                                SimpleDateFormat formatter = new SimpleDateFormat(dateFormatForYear);
                                selectedDate = formatter.format(date);
                                editText.setText(selectedDate);
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);
//        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

    }
}