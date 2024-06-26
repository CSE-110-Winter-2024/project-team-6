package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;

import org.junit.Before;

import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.util.Subject;


//Citation: ChatGPT was used as an aid to help write some tests
public class ItemRepoTest {

    private InMemoryDataSource dataSource;
    private ItemRepository itemRepository;

    private ZonedDateTime mockTime = ZonedDateTime.now();
    @Before
    public void setUp() {
        dataSource = new InMemoryDataSource(); // Instantiate the DataSource
        itemRepository = new SimpleItemRepository(dataSource); // Instantiate the ItemRepository with the DataSource
    }

    @Test
    public void testFindAll() {
        // Add some items to the DataSource
        List<Item> items = new ArrayList<>();
        items.add(new Item("Item 1", 1, 1, false,
                mockTime, false, "NONE", false, false, "NONE"));
        items.add(new Item("Item 2", 2, 2, true,
                mockTime, false, "NONE", false, false, "NONE"));
        dataSource.putItems(items);

        // Call the findAll method
        Subject<List<Item>> result = itemRepository.findAll();

        // Verify that the result matches the items in the DataSource
        assertEquals(dataSource.getAllItemsSubject().getValue(), result.getValue());
    }

    @Test
    public void testSave() {
        // Create a new item
        Item item = new Item("Test Item", 1, 1, false,
                mockTime, false, "NONE", false, false, "NONE");

        // Call the save method
        itemRepository.save(item);

        // Verify that the item was saved to the DataSource
        assertTrue(dataSource.getItems().contains(item));
    }

    @Test
    public void testSaveList() {
        // Create a list of items
        List<Item> items = new ArrayList<>();
        items.add(new Item("Item 1", 1, 1, false,
                mockTime, false, "NONE", false, false, "NONE"));
        items.add(new Item("Item 2", 2, 2, true,
                mockTime, false, "NONE", false, false, "NONE"));

        // Call the save method with the list
        itemRepository.save(items);

        // Verify that the items were saved to the DataSource
        assertTrue(dataSource.getItems().containsAll(items));
    }

    @Test
    public void testRemove() {
        // Add an item to the DataSource
        Item item = new Item("Test Item", 1, 1, false,
                mockTime, false, "NONE", false, false, "NONE");
        dataSource.putItem(item);

        // Call the remove method
        itemRepository.remove(item.id());

        // Verify that the item was removed from the DataSource
        assertFalse(dataSource.getItems().contains(item));
    }

    @Test
    public void testSize() {
        // Add some items to the DataSource
        List<Item> items = new ArrayList<>();
        items.add(new Item("Item 1", 1, 1, false,
                mockTime, false, "NONE", false, false, "NONE"));
        items.add(new Item("Item 2", 2, 2, false,
                mockTime, false, "NONE", false, false, "NONE"));
        items.add(new Item("Item 3", 3, 3, true,
                mockTime, false, "NONE", false, false, "NONE"));
        dataSource.putItems(items);

        // Call the size method
        int size = itemRepository.size();

        // Verify that the size matches the number of items in the DataSource
        assertEquals(items.size(), size);
    }

    @Test
    public void testAppend_NoIncompleteItems() {
        // Add some complete items to the DataSource
        List<Item> items = new ArrayList<>();

        items.add(new Item("Complete Item 1", 1, 1, true,
                mockTime, false, "NONE", false, false, "NONE"));
        items.add(new Item("Complete Item 2", 2, 2, true,
                mockTime, false, "NONE", false, false, "NONE"));
        dataSource.putItems(items);

        // Call the append method with a new item
        itemRepository.append(new Item("New Item", 3, 3, false,
                mockTime, false, "NONE", false, false, "NONE"));

        // Verify that the new item was not added (no incomplete items)
        assertEquals(2, dataSource.getItems().size());
    }

    @Test
    public void testAppend_WithIncompleteItems() {
        // Add some items to the DataSource (one incomplete and one complete)
        List<Item> items = new ArrayList<>();
        items.add(new Item("Incomplete Item", 1, 1, false,
                mockTime, false, "NONE", false, false, "NONE"));
        items.add(new Item("Complete Item", 2, 2, true,
                mockTime, false, "NONE", false, false, "NONE"));
        dataSource.putItems(items);

        // Call the append method with a new incomplete item
        itemRepository.append(new Item("New Incomplete Item", 3, 3, false,
                mockTime, false, "NONE", false, false, "NONE"));

        // Verify that the new item was added with the correct sort order
        assertEquals(3, dataSource.getItems().size());
        assertEquals(2, dataSource.getItems().get(2).sortOrder());
    }


    @Test
    public void testPrepend() {
        // Add some items to the DataSource
        List<Item> items = new ArrayList<>();
        dataSource = new InMemoryDataSource();
        itemRepository = new SimpleItemRepository(dataSource);

        items.add(new Item("Item 1", 1, 1, false,
                mockTime, false, "NONE", false, false, "NONE"));
        items.add(new Item("Item 2", 2, 2, false,
                mockTime, false, "NONE", false, false, "NONE"));
        dataSource.putItems(items);

        // Call the prepend method with a new item
        itemRepository.prepend(new Item("New Item", 3, 0, false,
                mockTime, false, "NONE", false, false, "NONE"));


        // Verify that the new item was added with the correct sort order
        assertEquals(3, dataSource.getItems().size());
        assertEquals(1, dataSource.getMinSortOrder());
    }


    @Test
    public void testMarkCompleteOrIncomplete() {
        // Add an incomplete item to the DataSource
        Item item = new Item("Incomplete Item", 1, 1, false,
                mockTime, false, "NONE", false, false, "NONE");
        dataSource.putItem(item);

        // Call the markCompleteOrIncomplete method
        itemRepository.markCompleteOrIncomplete(item.id());

        // Verify that the item's done status was toggled
        assertTrue(dataSource.getItems().get(0).isDone());
    }

    @Test
    public void testMarkRecurring() {
        // Add an incomplete item to the DataSource
        Item item = new Item("Test Item", 1, 1, false,
               mockTime, false, "NONE", false, false, "NONE");
        dataSource.putItem(item);

        // Call the markCompleteOrIncomplete method
        itemRepository.markRecurring(item.id());

        // Verify that the item's done status was toggled
        assertTrue(dataSource.getItems().get(0).isRecurring());
    }

    @Test
    public void testRemoveComplete(){
        Item item = new Item("complete Item", 1, 1, true,
                mockTime, false, "NONE", false, false, "NONE");
        Item item2 = new Item("Incomplete Item", 1, 1, false,
                mockTime, false, "NONE", false, false, "NONE");
        dataSource.putItem(item);
        dataSource.putItem(item2);

        //Call removeAllComplete
        itemRepository.removeAllComplete();

        //Check the itemRepository size
        assertEquals(dataSource.getItems().size(), 1);

        //Test with multiple completed Items
        Item item3 = new Item("Complete Item", 2, 2, true,
                mockTime, false, "NONE", false, false, "NONE");
        dataSource.putItem(item);
        dataSource.putItem(item2);
        dataSource.putItem(item3);

        itemRepository.removeAllComplete();

        assertEquals(dataSource.getItems().size(), 1);

    }

    @Test
    public void testAddCategory(){
        Item item = new Item("Finish MMW essay", 1, 1, false,
                mockTime, false, "NONE", false, false, "SCHOOL");
        dataSource.putItem(item);
        assertEquals(dataSource.getItems().get(0).getCategory(), "SCHOOL");
    }

    @Test
    public void testListCategory(){
        List<Item> items = new ArrayList<>();
        items.add(new Item("Don't go to CSE110", 1, 1, false,
                mockTime, false, "NONE", false, false, "SCHOOL"));
        items.add(new Item("Sleep", 2, 2, true,
                mockTime, false, "NONE", false, false, "HOME"));
        items.add(new Item("Fire the boss", 3, 3, true,
                mockTime, false, "NONE", false, false, "WORK"));

        dataSource.putItems(items);
        assertEquals(dataSource.getItems().get(0).getCategory(), "SCHOOL");
        assertEquals(dataSource.getItems().get(1).getCategory(), "HOME");
        assertEquals(dataSource.getItems().get(2).getCategory(), "WORK");
    }

}
