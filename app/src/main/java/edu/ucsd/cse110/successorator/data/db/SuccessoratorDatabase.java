package edu.ucsd.cse110.successorator.data.db;

import androidx.room.RoomDatabase;
import androidx.room.Database;

@Database(entities = {ItemEntity.class}, version = 2)
public abstract class SuccessoratorDatabase extends RoomDatabase {
    public abstract ItemDao itemDao();
}