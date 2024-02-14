package edu.ucsd.cse110.successorator;

import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateFormatter {
    private ZonedDateTime clock;
    DateFormatter(ZonedDateTime clock) {
        this.clock = clock.minusHours(2);
    }
    // The formatted date is what is changed
    public String getDate() {
        // Offset 2 hours
        int month = clock.getMonthValue();
        int day = clock.getDayOfMonth();

        String weekDay = clock.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());

        String formattedDate = String.format("%s %d/%d", weekDay, month, day);

        return formattedDate;
    }
}
