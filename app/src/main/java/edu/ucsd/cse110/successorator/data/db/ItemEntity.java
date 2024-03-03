package edu.ucsd.cse110.successorator.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.ZonedDateTime;

import edu.ucsd.cse110.successorator.lib.domain.Item;
import edu.ucsd.cse110.successorator.util.ZonedDateTimeConverter;

@Entity(tableName = "items")
@TypeConverters(ZonedDateTimeConverter.class)
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

    @ColumnInfo(name = "date")
    public ZonedDateTime date;

    @ColumnInfo(name = "is_recurring")
    public boolean isRecurring;

    @ColumnInfo(name = "recurring_type")
    public String recurringType;

    ItemEntity(@NonNull String description, int sortOrder, boolean isDone,
               ZonedDateTime date, boolean isRecurring, String recurringType){
        this.description = description;
        this.sortOrder = sortOrder;
        this.isDone = isDone;
        this.date = date;
        this.isRecurring = isRecurring;
        this.recurringType = recurringType;
    }

    // Given an item create an ItemEntity
    public static ItemEntity fromItem(@NonNull Item item){
        var task = new ItemEntity(item.getDescription(), item.sortOrder(), item.isDone(),
                item.getDate(), item.isRecurring(), item.getRecurringType());
        task.id = item.id();
        return task;
    }

    // Create a new Item
    public @NonNull Item toItem(){
        return new Item(description, id, sortOrder, isDone, date, isRecurring, recurringType);
    }

}