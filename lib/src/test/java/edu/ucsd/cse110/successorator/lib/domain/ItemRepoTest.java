package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;

import org.junit.Before;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.util.Subject;


//Citation: ChatGPT was used as an aid to help write some tests
public class ItemRepoTest {

    private InMemoryDataSource dataSource;
    private ItemRepository itemRepository;
    @Before
    public void setUp() {
        dataSource = new InMemoryDataSource(); // Instantiate the DataSource
        itemRepository = new SimpleItemRepository(dataSource); // Instantiate the ItemRepository with the DataSource
    }

    @Test
    public void testFindAll() {
        // Add some items to the DataSource
        List<Item> items = new ArrayList<>();
        items.add(new Item("Item 1", 1, 1, false));
        items.add(new Item("Item 2", 2, 2, true));
        dataSource.putFlashcards(items);

        // Call the findAll method
        Subject<List<Item>> result = itemRepository.findAll();

        // Verify that the result matches the items in the DataSource
        assertEquals(dataSource.getAllFlashcardsSubject().getValue(), result.getValue());
    }

    @Test
    public void testSave() {
        // Create a new item
        Item item = new Item("Test Item", 1, 1, false);

        // Call the save method
        itemRepository.save(item);

        // Verify that the item was saved to the DataSource
        assertTrue(dataSource.getFlashcards().contains(item));
    }

    @Test
    public void testSaveList() {
        // Create a list of items
        List<Item> items = new ArrayList<>();
        items.add(new Item("Item 1", 1, 1, false));
        items.add(new Item("Item 2", 2, 2, true));

        // Call the save method with the list
        itemRepository.save(items);

        // Verify that the items were saved to the DataSource
        assertTrue(dataSource.getFlashcards().containsAll(items));
    }

    @Test
    public void testRemove() {
        // Add an item to the DataSource
        Item item = new Item("Test Item", 1, 1, false);
        dataSource.putFlashcard(item);

        // Call the remove method
        itemRepository.remove(item.id());

        // Verify that the item was removed from the DataSource
        assertFalse(dataSource.getFlashcards().contains(item));
    }

    @Test
    public void testSize() {
        // Add some items to the DataSource
        List<Item> items = new ArrayList<>();
        items.add(new Item("Item 1", 1, 1, false));
        items.add(new Item("Item 2", 2, 2, false));
        items.add(new Item("Item 3", 3, 3, true));
        dataSource.putFlashcards(items);

        // Call the size method
        int size = itemRepository.size();

        // Verify that the size matches the number of items in the DataSource
        assertEquals(items.size(), size);
    }

    @Test
    public void testAppend_NoIncompleteItems() {
        // Add some complete items to the DataSource
        List<Item> items = new ArrayList<>();

        items.add(new Item("Complete Item 1", 1, 1, true));
        items.add(new Item("Complete Item 2", 2, 2, true));
        dataSource.putFlashcards(items);

        // Call the append method with a new item
        itemRepository.append(new Item("New Item", 3, 3, false));

        // Verify that the new item was not added (no incomplete items)
        assertEquals(2, dataSource.getFlashcards().size());
    }

    @Test
    public void testAppend_WithIncompleteItems() {
        // Add some items to the DataSource (one incomplete and one complete)
        List<Item> items = new ArrayList<>();
        items.add(new Item("Incomplete Item", 1, 1, false));
        items.add(new Item("Complete Item", 2, 2, true));
        dataSource.putFlashcards(items);

        // Call the append method with a new incomplete item
        itemRepository.append(new Item("New Incomplete Item", 3, 3, false));

        // Verify that the new item was added with the correct sort order
        assertEquals(3, dataSource.getFlashcards().size());
        assertEquals(2, dataSource.getFlashcards().get(2).sortOrder());
    }


    @Test
    public void testPrepend() {
        // Add some items to the DataSource
        List<Item> items = new ArrayList<>();
        dataSource = new InMemoryDataSource();
        itemRepository = new SimpleItemRepository(dataSource);

        items.add(new Item("Item 1", 1, 1, false));
        items.add(new Item("Item 2", 2, 2, false));
        dataSource.putFlashcards(items);

        // Call the prepend method with a new item
        itemRepository.prepend(new Item("New Item", 3, 0, false));


        // Verify that the new item was added with the correct sort order
        assertEquals(3, dataSource.getFlashcards().size());
        assertEquals(1, dataSource.getMinSortOrder());
    }


    @Test
    public void testMarkCompleteOrIncomplete() {
        // Add an incomplete item to the DataSource
        Item item = new Item("Incomplete Item", 1, 1, false);
        dataSource.putFlashcard(item);

        // Call the markCompleteOrIncomplete method
        itemRepository.markCompleteOrIncomplete(item.id());

        // Verify that the item's done status was toggled
        assertTrue(dataSource.getFlashcards().get(0).isDone());
    }

}
