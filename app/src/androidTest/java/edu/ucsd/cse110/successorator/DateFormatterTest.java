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

        assertEquals(dt.getDate(), "Wednesday 2/14");
    }

    @Test
    public void testBeforeRollover() {
        ZonedDateTime testClock = ZonedDateTime.of(2024, 2, 14, 1, 3, 2, 1234, ZoneId.of("UTC"));

        DateFormatter dt = new DateFormatter(testClock);

        assertEquals(dt.getDate(), "Tuesday 2/13");
    }

    @Test
    public void testRolloverChange() {
        ZonedDateTime testClock = ZonedDateTime.of(2024, 2, 14, 1, 3, 2, 1234, ZoneId.of("UTC"));
        DateFormatter dt = new DateFormatter(testClock);
        assertEquals(dt.getDate(), "Tuesday 2/13");

        testClock = testClock.plusHours(1);
        DateFormatter dt2 = new DateFormatter(testClock);
        assertEquals(dt2.getDate(), "Wednesday 2/14");
    }

    @Test
    public void monthChange() {
        ZonedDateTime testClock = ZonedDateTime.of(2024, 1, 31, 3, 3, 2, 1234, ZoneId.of("UTC"));
        DateFormatter dt = new DateFormatter(testClock);
        assertEquals(dt.getDate(), "Wednesday 1/31");

        testClock = testClock.plusHours(24);
        DateFormatter dt2 = new DateFormatter(testClock);
        assertEquals(dt2.getDate(), "Thursday 2/1");

    }
}