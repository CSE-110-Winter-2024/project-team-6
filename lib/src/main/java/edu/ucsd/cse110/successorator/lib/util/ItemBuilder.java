package edu.ucsd.cse110.successorator.lib.util;

import androidx.annotation.Nullable;

import java.time.ZonedDateTime;

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
    private boolean deleted;

    private boolean tomorrow;

    private boolean show;

    public ItemBuilder() {
        this.description = "";
        this.sortOrder = -1;
        this.id = null;
        this.done = false;
        this.date = ZonedDateTime.now();
        this.recurring = false;
        this.recurringType = "NONE";
        this.pending = false;
        this.tomorrow = false;
        this.show = true;
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
    public ItemBuilder addDeleted(boolean deleted){
        this.deleted = deleted;
        return this;
    }

    public Item build() {
        return new Item(description, id, sortOrder, done, date, recurring, recurringType, pending, tomorrow);
    }
}
