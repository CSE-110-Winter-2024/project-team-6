package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.Item;

@Dao
public interface ItemDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(ItemEntity item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(List<ItemEntity> items);

    @Query("SELECT * FROM items WHERE id = :id")
    ItemEntity find(int id);

    @Query("SELECT * FROM items ORDER BY sort_order")
    List<ItemEntity> findAll();

    @Query("SELECT * FROM items WHERE id = :id")
    LiveData<ItemEntity> findAsLiveData(int id);

    @Query("SELECT * FROM items ORDER BY sort_order")
    LiveData<List<ItemEntity>> findAllAsLiveData();

    @Query("SELECT COUNT(*) FROM items")
    int count();

    @Query("SELECT MIN(sort_order) FROM items")
    int getMinSortOrder();

    @Query("SELECT MAX(sort_order) FROM items")
    int getMaxSortOrder();

    @Query("UPDATE items SET sort_order = sort_order + :by " + "WHERE sort_order >= :from AND sort_order <= :to")
    void shiftSortOrders(int from, int to, int by);

    @Transaction
    default int append(ItemEntity item){
        var maxSortOrder = getMaxSortOrder();
        var newItem = new ItemEntity(
                item.description, maxSortOrder + 1
        );
        return Math.toIntExact(insert(newItem));
    }

    @Transaction
    default int prepend(ItemEntity item){
        shiftSortOrders(getMinSortOrder(), getMaxSortOrder(), 1);
        var newItem = new ItemEntity(
                item.description, getMinSortOrder()-1
        );
        return Math.toIntExact(insert(newItem));
    }

    @Query("DELETE FROM items WHERE id = :id")
    void delete(int id);



}


