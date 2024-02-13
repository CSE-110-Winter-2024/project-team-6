package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;

public class ItemTest {

    @org.junit.Test
    public void getDescription() {
        var unexpected = new Item("Call Mom", 0, 0);
        var expected = "Call Mom";
        assertEquals(expected, unexpected.getDescription());
    }

    @org.junit.Test
    public void isDone() {
        var unexpected = new Item("Call Mom", 0, 0);
        var expected = new Item("Call Mom", 0, 0);
        assertEquals(expected, unexpected);
        unexpected.markDone();
        expected.markDone();
        assertEquals(expected, unexpected);
    }

    @org.junit.Test
    public void markDone() {
        var unexpected = new Item("Call Mom", 0, 0);
        var expected = new Item("Call Mom", 0, 0);
        unexpected.markDone();
        expected.markDone();
        assertEquals(expected, unexpected);
    }

    @org.junit.Test
    public void setDescription() {
        var unexpected = new Item("Call Mom", 0, 0);
        var expected = new Item("Call Mom", 0, 0);
        unexpected.setDescription("Call Dad");
        expected.setDescription("Call Dad");
        assertEquals(expected, unexpected);
    }
}