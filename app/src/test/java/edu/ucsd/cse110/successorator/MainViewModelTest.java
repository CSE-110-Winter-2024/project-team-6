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
    public void testMarkCompleteOrIncomplete(){
        List<Item> items = new ArrayList<>();
        items.add(new Item("Item 1", 1, 1, false,
                mockTime, false, "NONE", false, false, "NONE"));
        dataSource.putItems(items);
        MainViewModel viewModel = new MainViewModel(itemRepository);
        viewModel.markCompleteOrIncomplete(1);

        viewModel.getOrderedCards().observe(cards -> {
            assertEquals(true, cards.get(0).isDone());

        });

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

    @Test
    public void testAppend(){
        List<Item> items = new ArrayList<>();
        items.add(new Item("Item 1", 1, 1, false,
                mockTime, false, "NONE", false, false, "NONE"));
        items.add(new Item("Item 2", 2, 2, false,
                mockTime, false, "NONE", false, false, "NONE"));
        items.add(new Item("Item 3", 3, 3, false,
                mockTime, false, "NONE", false, false, "NONE"));
        dataSource.putItems(items);


        MainViewModel viewModel = new MainViewModel(itemRepository);

        viewModel.append(new Item("Item 4", 4, 4, false,
                mockTime, false, "NONE", false, false, "NONE"));

        viewModel.getOrderedCards().observe(cards -> {
            assertEquals(4, cards.size());
            assertEquals("Item 1", cards.get(0).getDescription());
            assertEquals("Item 2", cards.get(1).getDescription());
            assertEquals("Item 3", cards.get(2).getDescription());
            assertEquals("Item 4", cards.get(3).getDescription());
        });
    }
    @Test
    public void testPrepend() {
        List<Item> items = new ArrayList<>();
        items.add(new Item("Item 1", 1, 1, false,
                mockTime, false, "NONE", false, false, "NONE"));
        items.add(new Item("Item 2", 2, 2, false,
                mockTime, false, "NONE", false, false, "NONE"));
        items.add(new Item("Item 3", 3, 3, false,
                mockTime, false, "NONE", false, false, "NONE"));
        dataSource.putItems(items);

        MainViewModel viewModel = new MainViewModel(itemRepository);

        viewModel.prepend(new Item("Item 4", 4, 4, false,
                mockTime, false, "NONE", false, false, "NONE"));
        viewModel.getOrderedCards().observe(cards -> {
            assertEquals(4, cards.size());
            assertEquals("Item 1", cards.get(1).getDescription());
            assertEquals("Item 2", cards.get(2).getDescription());
            assertEquals("Item 3", cards.get(3).getDescription());
            assertEquals("Item 4", cards.get(0).getDescription());
        });
    }
    @Test
    public void testRemove(){
        List<Item> items = new ArrayList<>();
        items.add(new Item("Item 1", 1, 1, false,
                mockTime, false, "NONE", false, false, "NONE"));
        items.add(new Item("Item 2", 2, 2, false,
                mockTime, false, "NONE", false, false, "NONE"));
        items.add(new Item("Item 3", 3, 3, false,
                mockTime, false, "NONE", false, false, "NONE"));
        dataSource.putItems(items);

        MainViewModel viewModel = new MainViewModel(itemRepository);

        viewModel.remove(2);
        viewModel.getOrderedCards().observe(cards -> {
            assertEquals(2, cards.size());
            assertEquals("Item 1", cards.get(0).getDescription());
            assertEquals("Item 3", cards.get(1).getDescription());

        });
    }
    @Test
    public void testSize(){
        List<Item> items = new ArrayList<>();
        items.add(new Item("Item 1", 1, 1, false,
                mockTime, false, "NONE", false, false, "NONE"));
        items.add(new Item("Item 2", 2, 2, false,
                mockTime, false, "NONE", false, false, "NONE"));
        items.add(new Item("Item 3", 3, 3, false,
                mockTime, false, "NONE", false, false, "NONE"));
        dataSource.putItems(items);

        MainViewModel viewModel = new MainViewModel(itemRepository);

        assertEquals(viewModel.size(), 3);
    }
    @Test
    public void testRemoveAllComplete(){
        List<Item> items = new ArrayList<>();
        items.add(new Item("Item 1", 1, 1, true,
                mockTime, false, "NONE", false, false, "NONE"));
        items.add(new Item("Item 2", 2, 2, false,
                mockTime, false, "NONE", false, false, "NONE"));
        items.add(new Item("Item 3", 3, 3, true,
                mockTime, false, "NONE", false, false, "NONE"));
        items.add(new Item("Item 4", 4, 4, false,
                mockTime, false, "NONE", false, false, "NONE"));
        items.add(new Item("Item 5", 5, 5, true,
                mockTime, false, "NONE", false, false, "NONE"));
        dataSource.putItems(items);

        MainViewModel viewModel = new MainViewModel(itemRepository);

        viewModel.removeAllComplete();

        viewModel.getOrderedCards().observe(cards -> {
            assertEquals(2, cards.size());
            assertEquals("Item 2", cards.get(0).getDescription());
            assertEquals("Item 4", cards.get(1).getDescription());

        });
    }

    @Test
    public void filterCategory() {
        List<Item> items = new ArrayList<>();
        items.add(new Item("Caffe Calabria - coffee", 1, 1, true,
                mockTime, false, "NONE", false, false, "ERRAND"));
        items.add(new Item("cook dinner", 1, 1, true,
                mockTime, false, "NONE", false, false, "HOME"));
        items.add(new Item("Homework", 1, 1, true,
                mockTime, false, "NONE", false, false, "SCHOOL"));
        dataSource.putItems(items);

        MainViewModel viewModel = new MainViewModel(itemRepository);

        viewModel.getOrderedCards().observe(cards -> {
            assertEquals("SCHOOL", cards.get(2).getCategory());
        });
    }

    @Test
    public void testDateViews() {
        List<Item> items = new ArrayList<>();
        items.add(new Item("Caffe Calabria - coffee", 1, 1, true,
                mockTime, false, "NONE", false, false, "ERRAND"));
        // Item set for tomorrow
        items.add(new Item("cook dinner", 2, 2, true,
                mockTime.plusDays(1), false, "NONE", false, false, "HOME"));
        // Have pending item
        items.add(new Item("Homework", 3, 3, true,
                mockTime, false, "NONE", true, false, "SCHOOL"));
        dataSource.putItems(items);

        MainViewModel viewModel = new MainViewModel(itemRepository);

        viewModel.getOrderedCards().observe(cards -> {
            assertEquals(3, cards.size());
            assertEquals(mockTime.plusDays(1), cards.get(1).getDate());
            assertTrue(cards.get(2).isPending());
        });
    }

    @Test
    public void testRecurringTasks() {
        List<Item> items = new ArrayList<>();
        items.add(new Item("Caffe Calabria - coffee", 1, 1, true,
                mockTime, false, "NONE", false, false, "ERRAND"));
        items.add(new Item("cook dinner", 2, 2, true,
                mockTime, false, "DAILY", false, false, "HOME"));
        items.add(new Item("Homework", 3, 3, true,
                mockTime, false, "WEEKLY", false, false, "SCHOOL"));
        dataSource.putItems(items);

        MainViewModel viewModel = new MainViewModel(itemRepository);

        // Make sure the recurrence functions are correctly registers
        viewModel.getOrderedCards().observe(cards -> {
            assertEquals("NONE", cards.get(0).getRecurringType());
            assertEquals("DAILY", cards.get(1).getRecurringType());
            assertEquals("WEEKLY", cards.get(2).getRecurringType());
        });
    }
}