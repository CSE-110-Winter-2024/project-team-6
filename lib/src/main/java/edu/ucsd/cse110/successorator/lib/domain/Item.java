package edu.ucsd.cse110.successorator.lib.domain;

import java.io.Serializable;
import java.util.Objects;

public class Item implements Serializable {
    private String description;
    private boolean done;

    public Item(String description){
        this.description = description;
        done = false;
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
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return isDone() == item.isDone() && Objects.equals(getDescription(), item.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription(), isDone());
    }
}
