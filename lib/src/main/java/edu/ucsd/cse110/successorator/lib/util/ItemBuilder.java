package edu.ucsd.cse110.successorator.lib.util;

import androidx.annotation.Nullable;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import edu.ucsd.cse110.successorator.lib.domain.Item;

/*
 * ItemBuilder class to improve SRP for fragment dialogs that create items.
 */

public class ItemBuilder {
    private String description;

    private int sortOrder;
    private final @Nullable Integer id;
    private boolean done;

    // Track the date that the Item is associated with
    private ZonedDateTime date;

    // Track if Item is a recurring goal
    private boolean recurring;

    // Track type of recurring if is recurring
    private String recurringType;

    private boolean pending;

    public ItemBuilder() {

        // A default date that will never equal present
        String dateTimeString = "2000-03-03T12:00:00-05:00";
        ZonedDateTime defaultDate = ZonedDateTime.parse(dateTimeString,
                DateTimeFormatter.ISO_ZONED_DATE_TIME);

        this.description = "";
        this.sortOrder = -1;
        this.id = null;
        this.done = false;
        this.date = defaultDate;
        this.recurring = false;
        this.recurringType = "NONE";
        this.pending = false;
    }

    public ItemBuilder addDescription(String description) {
        this.description = description;
        return this;
    }

    public ItemBuilder addSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public ItemBuilder addDone(boolean done) {
        this.done = done;
        return this;
    }

    public ItemBuilder addDate(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public ItemBuilder addRecurring(boolean recurring) {
        this.recurring = recurring;
        return this;
    }

    public ItemBuilder addRecurringType(String recurringType) {
        this.recurringType = recurringType;
        return this;
    }

    public ItemBuilder addPending(boolean pending) {
        this.pending = pending;
        return this;
    }

    public Item build() {
        return new Item(description, id, sortOrder, done, date, recurring, recurringType, pending);
    }
}
