package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ItemTest {

    // Mock an instance of time for testing
    private ZonedDateTime mockTime = ZonedDateTime.now();

    @org.junit.Test
    public void getDescription() {
        var unexpected = new Item("Call Mom", 0, 0, false,
                                  mockTime, false, "NONE");
        var expected = "Call Mom";
        assertEquals(expected, unexpected.getDescription());
    }

    @org.junit.Test
    public void isDone() {
        var unexpected = new Item("Call Mom", 0, 0, false, mockTime,
                false, "NONE");
        var expected = new Item("Call Mom", 0, 0, false, mockTime,
                false, "NONE");
        assertEquals(expected, unexpected);
        unexpected.markDone();
        expected.markDone();
        assertEquals(expected, unexpected);
    }

    @org.junit.Test
    public void markDone() {
        var unexpected = new Item("Call Mom", 0, 0, false,
                mockTime, false, "NONE");
        var expected = new Item("Call Mom", 0, 0, true,
                mockTime, false, "NONE");
        unexpected.markDone();
        assertEquals(expected, unexpected);
        unexpected.markDone();
        assertNotEquals(expected, unexpected);

    }

    @org.junit.Test
    public void setDescription() {
        var unexpected = new Item("Call Mom", 0, 0, false,
                mockTime, false, "NONE");
        var expected = new Item("Call Mom", 0, 0, false,
                mockTime, false, "NONE");
        unexpected.setDescription("Call Dad");
        expected.setDescription("Call Dad");
        assertEquals(expected, unexpected);
    }

    @Test
    public void testWithId() {
        var card = new Item("Call Mom", 0, 0, false,
                mockTime, false, "NONE");
        var expected = new Item("Call Mom", 42, 0, false,
                mockTime, false, "NONE");

        var actual = card.withId(42);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithSortOrder() {
        var card = new Item("Call Mom", 0, 0, false,
                mockTime, false, "NONE");
        var expected = new Item("Call Mom", 0, 42, false,
                mockTime, false, "NONE");

        var actual = card.withSortOrder(42);
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        var card1 = new Item("Call Mom", 0, 0, false,
                mockTime, false, "NONE");
        var card2 = new Item("Call Mom", 0, 0, false,
                mockTime, false, "NONE");
        var card3 = new Item("Call Mom", 1, 0, false,
                mockTime, false, "NONE");


        assertEquals(card1, card2);
        assertNotEquals(card1, card3);
    }

    @Test
    public void testRecurring() {
        var card = new Item("Call Mom", 0, 0, false,
                mockTime, false, "NONE");

        assertFalse(card.isRecurring());

        card.markRecurring();

        assertTrue(card.isRecurring());
    }

    @Test
    public void testDate() {
        var card = new Item("Call Mom", 0, 0, false,
                mockTime, false, "NONE");

        assertEquals(mockTime, card.getDate());

        String dateTimeString = "2024-03-03T12:00:00-05:00";
        ZonedDateTime newDate = ZonedDateTime.parse(dateTimeString,
                                DateTimeFormatter.ISO_ZONED_DATE_TIME);
        card.setDate(newDate);

        assertEquals(newDate, card.getDate());
    }

    @Test
    public void testRecurrenceType() {
        var card = new Item("Recurrence Test", 0, 0, false,
                mockTime, true, "WEEKLY");

        assertEquals("WEEKLY", card.getRecurringType());
    }
}