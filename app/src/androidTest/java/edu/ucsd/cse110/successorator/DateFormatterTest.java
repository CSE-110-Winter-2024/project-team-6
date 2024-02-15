package edu.ucsd.cse110.successorator;

import static androidx.test.core.app.ActivityScenario.launch;

import static junit.framework.TestCase.assertEquals;

import android.content.res.Resources;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;

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

        assertEquals(dt.getDate(testClock), "Wednesday 2/14");
    }

    @Test
    public void testBeforeRollover() {
        ZonedDateTime testClock = ZonedDateTime.of(2024, 2, 14, 1, 3, 2, 1234, ZoneId.of("UTC"));

        DateFormatter dt = new DateFormatter(testClock);

        assertEquals("Tuesday 2/13", dt.getDate(testClock));
    }

    @Test
    public void testRolloverChange() {
        ZonedDateTime testClock = ZonedDateTime.of(2024, 2, 14, 1, 3, 2, 1234, ZoneId.of("UTC"));

        DateFormatter dt = new DateFormatter(testClock);
        String receivedDate = dt.getDate(testClock);
        assertEquals("Tuesday 2/13", receivedDate);

        testClock = testClock.plusHours(1);
        assertEquals("Wednesday 2/14", dt.getDate(testClock));
    }

    @Test
    public void monthChange() {
        ZonedDateTime testClock = ZonedDateTime.of(2024, 1, 31, 3, 3, 2, 1234, ZoneId.of("UTC"));
        DateFormatter dt = new DateFormatter(testClock);
        assertEquals("Wednesday 1/31", dt.getDate(testClock));

        testClock = testClock.plusHours(24);
        assertEquals("Thursday 2/1", dt.getDate(testClock));

    }
}