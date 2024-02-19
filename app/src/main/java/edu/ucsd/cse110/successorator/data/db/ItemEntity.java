package edu.ucsd.cse110.successorator.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


import edu.ucsd.cse110.successorator.lib.domain.Item;

@Entity(tableName = "items")
public class ItemEntity {
    @PrimaryKey(autoGenerate = true)

    @ColumnInfo(name = "id")
    public Integer id = null;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "sort_order")
    public int sortOrder;

    @ColumnInfo(name = "is_done")
    public boolean isDone;

    ItemEntity(@NonNull String description, int sortOrder, boolean isDone){
        this.description = description;
        this.sortOrder = sortOrder;
        this.isDone = isDone;
    }

    public static ItemEntity fromItem(@NonNull Item item){
        var task = new ItemEntity(item.getDescription(), item.sortOrder(), item.isDone());
        task.id = item.id();
        return task;
    }

    public @NonNull Item toItem(){
        return new Item(description, id, sortOrder, isDone);
    }

}