package edu.ucsd.cse110.successorator.lib.domain;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatter {
    Calendar calender;

    public DateFormatter() {
        calender = Calendar.getInstance();
    }

    public String formatDate() {
        String dateFormat = DateFormat.getDateInstance().format(calender);
        return dateFormat;
    }

    public String formatTime() {
        String timeFormat = DateFormat.getTimeInstance().format(calender);
        return timeFormat;
    }

}
