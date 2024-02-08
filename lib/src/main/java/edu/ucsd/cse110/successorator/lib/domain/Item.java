package edu.ucsd.cse110.successorator.lib.domain;

import java.io.Serializable;

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
}
