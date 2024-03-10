package edu.ucsd.cse110.successorator;

import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateFormatter {
    private ZonedDateTime clock;

    //Track how many days we should advance the current date for testing.
    private int dateAdvance;

    public DateFormatter(ZonedDateTime clock) {
        this.clock = clock;
        dateAdvance = 0;
    }

    // Get the year to check for Yearly recurrence verification
    public int getYear(ZonedDateTime clock) {
        return clock.minusHours(2).getYear();
    }



    // The formatted date is what is changed
    public String getTodaysDate(ZonedDateTime clock) {
        // Offset the date
        this.clock = clock.minusHours(2);

        // FOR TESTING MOCK UI DATE; THE DATE ADVANCED WILL PERSIST AFTER THE APP IS PAUSED AND RESUMED
        this.clock = this.clock.plusDays(dateAdvance);

        // Offset 2 hours
        int month = this.clock.getMonthValue();
        int day = this.clock.getDayOfMonth();

        String weekDay = this.clock.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());

        // Return a formatted string for the date
        String formattedDate = String.format("Today %s %d/%d", weekDay, month, day);

        return formattedDate;
    }
    public String getTomorrowsDate(ZonedDateTime clock) {
        // Offset the date
        this.clock = clock.minusHours(2);

        // FOR TESTING MOCK UI DATE; THE DATE ADVANCED WILL PERSIST AFTER THE APP IS PAUSED AND RESUMED
        this.clock = this.clock.plusDays(1);

        // Offset 2 hours
        int month = this.clock.getMonthValue();
        int day = this.clock.getDayOfMonth();

        String weekDay = this.clock.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());

        // Return a formatted string for the date
        String formattedDate = String.format("Tomorrow %s %d/%d", weekDay, month, day);

        return formattedDate;
    }

    // Functionality for Mock Date UI to update to next day
    public String addDay(ZonedDateTime clock) {
        dateAdvance++;
        return getTodaysDate(clock);
    }

    public String weeklyDate(ZonedDateTime clock) {
        this.clock = clock.minusHours(2);

        // FOR TESTING MOCK UI DATE; THE DATE ADVANCED WILL PERSIST AFTER THE APP IS PAUSED AND RESUMED
        this.clock = this.clock.plusDays(dateAdvance);
        String weekDay = this.clock.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        // Return a formatted string for the date
        String formattedDate = weekDay.substring(0, 3);;
        return formattedDate;
    }


    public String monthlyDate(ZonedDateTime clock){
        this.clock = clock.minusHours(2);
        this.clock = this.clock.plusDays(dateAdvance);

        String weekDay = this.clock.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        // Return a formatted string for the date
        int day = this.clock.getDayOfMonth();
        String n = countNthDay(day);
        String shortday = weekDay.substring(0, 3);;
        String formattedDate = String.format("%s %s", n, shortday);
        return formattedDate;
    }

    public String yearlyDate(ZonedDateTime clock){
        this.clock = clock.minusHours(2);
        // FOR TESTING MOCK UI DATE; THE DATE ADVANCED WILL PERSIST AFTER THE APP IS PAUSED AND RESUMED
        this.clock = this.clock.plusDays(dateAdvance);
        String weekDay = this.clock.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        // Return a formatted string for the date
        int month = this.clock.getMonthValue();
        int day = this.clock.getDayOfMonth();
        String formattedDate = String.format("%d/%d", month, day);
        return formattedDate;
    }

    private String countNthDay(int dateOfMonth){
        int n = 0;
        while (dateOfMonth > 0){
            dateOfMonth -= 7;
            n++;
        }
        String res = "";

        switch (n) {
            case 1:
                res = "1st";
                break;
            case 2:
                res = "2nd";
                break;
            case 3:
                res = "3rd";
                break;
            default:
                res = n + "th";

        }
        return res;
    }

    public String getPersistentDate(ZonedDateTime clock) {
        this.clock = clock.minusHours(2);
        // FOR TESTING MOCK UI DATE; THE DATE ADVANCED WILL PERSIST AFTER THE APP IS PAUSED AND RESUMED
        String weekDay = this.clock.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        // Return a formatted string for the date
        int month = this.clock.getMonthValue();
        int day = this.clock.getDayOfMonth();
        int year = this.clock.getYear();

        String formattedDate = String.format("%d/%d/%d",year, month, day);
        return formattedDate;
    }

}

