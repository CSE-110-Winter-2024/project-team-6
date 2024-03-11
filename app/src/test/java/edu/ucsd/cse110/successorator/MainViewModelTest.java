package edu.ucsd.cse110.successorator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.Item;
import edu.ucsd.cse110.successorator.lib.domain.ItemRepository;
import edu.ucsd.cse110.successorator.lib.domain.SimpleItemRepository;
import edu.ucsd.cse110.successorator.util.LiveDataSubjectAdapter;

public class MainViewModelTest extends TestCase {
    private InMemoryDataSource dataSource;
    private ItemRepository itemRepository;

    private ZonedDateTime mockTime = ZonedDateTime.now();
    @Before
    public void setUp() {
        dataSource = new InMemoryDataSource(); // Instantiate the DataSource
        itemRepository = new SimpleItemRepository(dataSource); // Instantiate the ItemRepository with the DataSource
    }
    @Test
    public void testGetOrderedCards() throws InterruptedException{
        List<Item> items = new ArrayList<>();
        items.add(new Item("Item 1", 1, 1, false,
                mockTime, false, "NONE", false, false, "NONE"));
        items.add(new Item("Item 2", 2, 2, false,
                mockTime, false, "NONE", false, false, "NONE"));
        items.add(new Item("Item 3", 3, 3, false,
                mockTime, false, "NONE", false, false, "NONE"));
        dataSource.putItems(items);

        MainViewModel viewModel = new MainViewModel(itemRepository);

        viewModel.getOrderedCards().observe(cards -> {
            assertEquals(3, cards.size());
            assertEquals("Item 1", cards.get(0).getDescription());
            assertEquals("Item 2", cards.get(1).getDescription());
            assertEquals("Item 3", cards.get(2).getDescription());
        });
    }
}