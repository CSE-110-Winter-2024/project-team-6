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
        List<ItemEntity> items = findAll();
        int lastIncompleteSortOrder = Integer.MIN_VALUE;
        int lastIncompleteIndex = -1;

        // Find the last incomplete item
        for (int i = 0; i < items.size(); i++) {
            ItemEntity temp = items.get(i);
            if (!temp.isDone) {
                lastIncompleteSortOrder = Math.max(lastIncompleteSortOrder, temp.sortOrder);
                lastIncompleteIndex = i;
            }
        }

        // If no incomplete items were found, append the new item to the start of the list
        if (lastIncompleteIndex == -1) {
            var newItem = new ItemEntity(item.description, item.sortOrder, item.isDone,
                item.date, item.isRecurring, item.recurringType, item.isPending, item.isTomorrow);
            return Math.toIntExact(insert(newItem));
        }

        // Append the new item after the last incomplete item
        shiftSortOrders(lastIncompleteSortOrder + 1, getMaxSortOrder(), 1);
        var newItem = new ItemEntity(item.description, lastIncompleteSortOrder+1,
                                     item.isDone, item.date, item.isRecurring, item.recurringType, item.isPending, item.isTomorrow);

        return Math.toIntExact(insert(newItem));
    }

    @Transaction
    default int prepend(ItemEntity item){
        shiftSortOrders(getMinSortOrder(), getMaxSortOrder(), 1);
        var newItem = new ItemEntity(
                item.description, getMinSortOrder()-1, item.isDone,
                item.date, item.isRecurring, item.recurringType, item.isPending, item.isTomorrow
        );
        return Math.toIntExact(insert(newItem));
    }
//"UPDATE items SET show = '~show', is_done = false WHERE is_done = true AND is_recurring = true;

    @Query("DELETE FROM items WHERE is_done = true AND is_recurring = false AND is_tomorrow = false")
    void removeAllComplete();

    @Query("DELETE FROM items WHERE id = :id")
    void delete(int id);

    @Query("UPDATE items SET is_done = ~is_done WHERE id = :id")
    void markCompleteOrIncomplete(int id);

    @Query("UPDATE items SET is_recurring = ~is_recurring WHERE id = :id")
    void markRecurringOrNonrecurring(int id);

    @Query("UPDATE items SET is_pending = ~is_pending WHERE id = :id")
    void markPending(int id);

    @Query("UPDATE items SET is_tomorrow = ~is_tomorrow WHERE id = :id")
    void markTomorrow(int id);


}
