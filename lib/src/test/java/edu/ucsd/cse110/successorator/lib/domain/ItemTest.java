package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import edu.ucsd.cse110.successorator.lib.util.ItemBuilder;

public class ItemTest {

    // Mock an instance of time for testing
    private ZonedDateTime mockTime = ZonedDateTime.now();
    private ZonedDateTime tomorrowTime = ZonedDateTime.now().plusDays(1);

    @org.junit.Test
    public void getDescription() {
        var unexpected = new Item("Call Mom", 0, 0, false,
                                  mockTime, false, "NONE", false, false);
        var expected = "Call Mom";
        assertEquals(expected, unexpected.getDescription());
    }

    @org.junit.Test
    public void isDone() {
        var unexpected = new Item("Call Mom", 0, 0, false, mockTime,
                false, "NONE", false, false);
        var expected = new Item("Call Mom", 0, 0, false, mockTime,
                false, "NONE", false, false);
        assertEquals(expected, unexpected);
        unexpected.markDone();
        expected.markDone();
        assertEquals(expected, unexpected);
    }

    @org.junit.Test
    public void markDone() {
        var unexpected = new Item("Call Mom", 0, 0, false,
                mockTime, false, "NONE", false, false);
        var expected = new Item("Call Mom", 0, 0, true,
                mockTime, false, "NONE", false, false);
        unexpected.markDone();
        assertEquals(expected, unexpected);
        unexpected.markDone();
        assertNotEquals(expected, unexpected);

    }

    @org.junit.Test
    public void setDescription() {
        var unexpected = new Item("Call Mom", 0, 0, false,
                mockTime, false, "NONE", false, false);
        var expected = new Item("Call Mom", 0, 0, false,
                mockTime, false, "NONE", false, false);
        unexpected.setDescription("Call Dad");
        expected.setDescription("Call Dad");
        assertEquals(expected, unexpected);
    }

    @Test
    public void testWithId() {
        var card = new Item("Call Mom", 0, 0, false,
                mockTime, false, "NONE", false, false);
        var expected = new Item("Call Mom", 42, 0, false,
                mockTime, false, "NONE", false, false);

        var actual = card.withId(42);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithSortOrder() {
        var card = new Item("Call Mom", 0, 0, false,
                mockTime, false, "NONE", false, false);
        var expected = new Item("Call Mom", 0, 42, false,
                mockTime, false, "NONE", false, false);

        var actual = card.withSortOrder(42);
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        var card1 = new Item("Call Mom", 0, 0, false,
                mockTime, false, "NONE", false, false);
        var card2 = new Item("Call Mom", 0, 0, false,
                mockTime, false, "NONE", false, false);
        var card3 = new Item("Call Mom", 1, 0, false,
                mockTime, false, "NONE", false, false);


        assertEquals(card1, card2);
        assertNotEquals(card1, card3);
    }

    @Test
    public void testRecurring() {
        var card = new Item("Call Mom", 0, 0, false,
                mockTime, false, "NONE", false, false);

        assertFalse(card.isRecurring());

        card.markRecurring();

        assertTrue(card.isRecurring());
    }

    @Test
    public void testDate() {
        var card = new Item("Call Mom", 0, 0, false,
                mockTime, false, "NONE", false, false);

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
                mockTime, true, "WEEKLY", false, false);

        assertEquals("WEEKLY", card.getRecurringType());
    }

    @Test
    public void testPending() {
        var card = new Item("Pending test", 0, 0, false,
                mockTime, false, "NONE", false, false);

        assertFalse(card.isPending());

        card.markPending();

        assertTrue(card.isPending());
    }

    @Test
    public void testTomorrow(){
        var card = new Item("Pending test", 0, 0, false,
                tomorrowTime, false, "NONE", false, false);

        assertEquals(card.getDate().getDayOfMonth(),ZonedDateTime.now().plusDays(1).getDayOfMonth());

    }

    @Test
    public void testItemBuilder() {
        ItemBuilder itemBuilder = new ItemBuilder();

        var expCard = new Item("Recurrence Test", null, 5, false,
                mockTime, true, "WEEKLY", false, false);

        var builderCard = itemBuilder.addDescription("Recurrence Test")
                .addSortOrder(5)
                .addDate(mockTime)
                .addRecurring(true)
                .addRecurringType("WEEKLY")
                .build();

        assertEquals(expCard, builderCard);
    }
}