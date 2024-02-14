package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Item implements Serializable {
    private String description;

    private int sortOrder;
    private final @Nullable Integer id;
    private boolean done;

    public Item(String description, @Nullable Integer id, int sortOrder){
        this.sortOrder = sortOrder;
        this.id = id;
        this.description = description;
        done = false;
    }

    public @Nullable Integer id() {
        return id;
    }

    public int sortOrder() {
        return sortOrder;
    }

    public Item withSortOrder(int sortOrder) {
        return new Item(this.description, this.id, sortOrder);
    }

    public Item withId(int id) {
        return new Item(this.description, id, this.sortOrder);
    }
    public String getDescription(){
        return description;
    }

    public boolean isDone(){
        return done;
    }

    public void markDone(){
        done = true;
    }

    public void setDescription(String description){
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return sortOrder == item.sortOrder && done == item.done && Objects.equals(description, item.description) && Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, sortOrder, id, done);
    }
}
