package edu.ucsd.cse110.successorator;

import static androidx.test.core.app.ActivityScenario.launch;
import static junit.framework.TestCase.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.ItemRepository;
import edu.ucsd.cse110.successorator.lib.domain.SimpleItemRepository;
import edu.ucsd.cse110.successorator.ui.itemList.ItemListFragment;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainViewModelTest {

    private InMemoryDataSource dataSource;
    private ItemRepository itemRepository;

    private ZonedDateTime mockTime = ZonedDateTime.now();

    private MainViewModel viewmodel;

    private MainActivity activity;

    @Before
    public void setUp() {
        dataSource = new InMemoryDataSource(); // Instantiate the DataSource
        itemRepository = new SimpleItemRepository(dataSource); // Instantiate the ItemRepository with the DataSource
        viewmodel = new MainViewModel(itemRepository);
    }
    @Test
    /**
     * We start the app by tapping the app icon on Tuesday 2/20
     * You should see “Today, Tues 2/20” at the top and a “+” to the right of it
     */
    public void testTodayView() {

    }

}
