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

        var sharedPreferences1 = getSharedPreferences("successorator", MODE_PRIVATE);
        var sharedPreferences2 = getSharedPreferences("formatted_date", MODE_PRIVATE);
        var isFirstRun = sharedPreferences1.getBoolean("isFirstRun", true);

        // When restarting application, should reset any artificially set time.
        sharedPreferences2.edit()
                .putInt("advance_count", 0)
                .apply();

        if(isFirstRun && database.itemDao().count() == 0){
            itemRepository.save(InMemoryDataSource.DEFAULT_CARDS);

            sharedPreferences1.edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }

    public ItemRepository getItemRepository() {
        return itemRepository;
    }
}

