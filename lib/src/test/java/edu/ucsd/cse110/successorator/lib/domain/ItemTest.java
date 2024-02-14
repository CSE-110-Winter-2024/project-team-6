package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;

import org.junit.Test;

public class ItemTest {

    @org.junit.Test
    public void getDescription() {
        var unexpected = new Item("Call Mom", 0, 0, false);
        var expected = "Call Mom";
        assertEquals(expected, unexpected.getDescription());
    }

    @org.junit.Test
    public void isDone() {
        var unexpected = new Item("Call Mom", 0, 0, false);
        var expected = new Item("Call Mom", 0, 0, false);
        assertEquals(expected, unexpected);
        unexpected.markDone();
        expected.markDone();
        assertEquals(expected, unexpected);
    }

    @org.junit.Test
    public void markDone() {
        var unexpected = new Item("Call Mom", 0, 0, false);
        var expected = new Item("Call Mom", 0, 0, true);
        unexpected.markDone();
        assertEquals(expected, unexpected);
        unexpected.markDone();
        assertNotEquals(expected, unexpected);

    }

    @org.junit.Test
    public void setDescription() {
        var unexpected = new Item("Call Mom", 0, 0, false);
        var expected = new Item("Call Mom", 0, 0, false);
        unexpected.setDescription("Call Dad");
        expected.setDescription("Call Dad");
        assertEquals(expected, unexpected);
    }

    @Test
    public void testWithId() {
        var card = new Item("Call Mom", 0, 0, false);
        var expected = new Item("Call Mom", 42, 0, false);
        var actual = card.withId(42);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithSortOrder() {
        var card = new Item("Call Mom", 0, 0, false);
        var expected = new Item("Call Mom", 0, 42, false);
        var actual = card.withSortOrder(42);
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        var card1 = new Item("Call Mom", 0, 0, false);
        var card2 = new Item("Call Mom", 0, 0, false);
        var card3 = new Item("Call Mom", 1, 0, false);

        assertEquals(card1, card2);
        assertNotEquals(card1, card3);
    }
}