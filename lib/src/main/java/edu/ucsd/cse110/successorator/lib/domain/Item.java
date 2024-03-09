package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
public class Item implements Serializable {
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


    public Item(String description, @Nullable Integer id, int sortOrder, boolean done,
                ZonedDateTime date, boolean recurring, String recurringType, boolean pending){
        this.sortOrder = sortOrder;
        this.id = id;
        this.description = description;
        this.done = done;
        this.date = date;
        this.recurring = recurring;
        this.recurringType = recurringType;
        this.pending = pending;

    }

    public @Nullable Integer id() {
        return id;
    }

    public int sortOrder() {
        return sortOrder;
    }

    public Item withSortOrder(int sortOrder) {
        return new Item(this.description, this.id, sortOrder, this.done, this.date,
                this.recurring, this.recurringType, this.pending);
    }

    public Item withId(int id) {
        return new Item(this.description, id, this.sortOrder, this.done, this.date,
                this.recurring, this.recurringType, this.pending);
    }

    public String getDescription(){
        return description;
    }

    public boolean isDone(){
        return this.done;
    }

    public void markDone(){
        this.done = !this.done;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setDate(ZonedDateTime date) { this.date = date; }

    public ZonedDateTime getDate() { return this.date; }

    public void markRecurring() { this.recurring = !this.recurring; }

    public boolean isRecurring() { return this.recurring; }

    public String getRecurringType() { return this.recurringType; }

    public void setRecurringType(String recurringType) { this.recurringType = recurringType; }

    public boolean isPending() { return this.pending; }

    public void markPending() { this.pending = !this.pending; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return sortOrder == item.sortOrder && done == item.done &&
                Objects.equals(description, item.description) &&
                Objects.equals(id, item.id) && Objects.equals(date, item.date) &&
                recurring == item.recurring && Objects.equals(recurringType, item.recurringType) &&
                pending == item.pending;


    }

    @Override
    public int hashCode() {
        return Objects.hash(description, sortOrder, id, done, date, recurring, recurringType, pending);
    }
}