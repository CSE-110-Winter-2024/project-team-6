package edu.ucsd.cse110.successorator;

import static androidx.test.core.app.ActivityScenario.launch;
import static junit.framework.TestCase.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DateFormatterTest {
    @Test
    public void testStandardDate() {
        ZonedDateTime testClock = ZonedDateTime.of(2024, 2, 14, 5, 3, 2, 1234, ZoneId.of("UTC"));

        DateFormatter dt = new DateFormatter(testClock);

        assertEquals(dt.getTodaysDate(testClock), "Wednesday 2/14");
    }

    @Test
    public void testBeforeRollover() {
        ZonedDateTime testClock = ZonedDateTime.of(2024, 2, 14, 1, 3, 2, 1234, ZoneId.of("UTC"));

        DateFormatter dt = new DateFormatter(testClock);

        assertEquals("Tuesday 2/13", dt.getTodaysDate(testClock));
    }

    @Test
    public void testRolloverChange() {
        ZonedDateTime testClock = ZonedDateTime.of(2024, 2, 14, 1, 3, 2, 1234, ZoneId.of("UTC"));

        DateFormatter dt = new DateFormatter(testClock);
        String receivedDate = dt.getTodaysDate(testClock);
        assertEquals("Tuesday 2/13", receivedDate);

        testClock = testClock.plusHours(1);
        assertEquals("Wednesday 2/14", dt.getTodaysDate(testClock));
    }

    @Test
    public void monthChange() {
        ZonedDateTime testClock = ZonedDateTime.of(2024, 1, 31, 3, 3, 2, 1234, ZoneId.of("UTC"));
        DateFormatter dt = new DateFormatter(testClock);
        assertEquals("Wednesday 1/31", dt.getTodaysDate(testClock));

        testClock = testClock.plusHours(24);
        assertEquals("Thursday 2/1", dt.getTodaysDate(testClock));

    }

    // Check to see if advance days will reformat the date.
    @Test
    public void addDays() {
        ZonedDateTime testClock = ZonedDateTime.of(2024, 2, 15, 3, 3, 2, 1234, ZoneId.of("UTC"));
        DateFormatter dt = new DateFormatter(testClock);
        assertEquals("Thursday 2/15", dt.getTodaysDate(testClock));

        // Advance a whole day to ensure new date properly formatted.
        assertEquals("Friday 2/16", dt.addDay(testClock));
    }
}