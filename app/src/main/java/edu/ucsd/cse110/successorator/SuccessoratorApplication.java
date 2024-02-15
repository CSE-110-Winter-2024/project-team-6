package edu.ucsd.cse110.successorator;

import android.app.Application;

import androidx.room.Room;
import edu.ucsd.cse110.successorator.data.db.RoomItemRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.ItemRepository;
import edu.ucsd.cse110.successorator.lib.domain.SimpleItemRepository;

public class SuccessoratorApplication extends Application {
    private InMemoryDataSource dataSource;
    private ItemRepository itemRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        var database = Room.databaseBuilder(
                        getApplicationContext(),
                        SuccessoratorDatabase.class,
                        "successorator-database"
                )
                .allowMainThreadQueries()
                .build();
        this.itemRepository = new RoomItemRepository(database.itemDao());

        var sharedPreferences = getSharedPreferences("successorator", MODE_PRIVATE);
        var isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);

        if(isFirstRun && database.itemDao().count() == 0){
            itemRepository.save(InMemoryDataSource.DEFAULT_CARDS);

            sharedPreferences.edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }

    public ItemRepository getItemRepository() {
        return itemRepository;
    }
}

