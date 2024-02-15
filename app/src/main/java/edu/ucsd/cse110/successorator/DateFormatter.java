package edu.ucsd.cse110.successorator;

import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateFormatter {
    private ZonedDateTime clock;

    //Track how many days we should advance the current date for testing.
    private int dateAdvance;

    public DateFormatter(ZonedDateTime clock) {
        this.clock = clock.minusHours(2);
        dateAdvance = 0;
    }


    // The formatted date is what is changed
    public String getDate(ZonedDateTime clock) {
        this.clock = clock.minusHours(2);


        // FOR TESTING MOCK UI DATE; THE DATE ADVANCED WILL PERSIST AFTER THE APP IS PAUSED AND RESUMED
        this.clock = clock.plusDays(dateAdvance);


        // Offset 2 hours
        int month = clock.getMonthValue();
        int day = clock.getDayOfMonth();

        String weekDay = clock.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());

        // Return a formatted string for the date
        String formattedDate = String.format("%s %d/%d", weekDay, month, day);

        return formattedDate;
    }

    // Functionality for Mock Date UI to update to next day
    public String addDay(ZonedDateTime clock) {
        dateAdvance++;
        return getDate(clock.plusDays(dateAdvance));
    }
}
