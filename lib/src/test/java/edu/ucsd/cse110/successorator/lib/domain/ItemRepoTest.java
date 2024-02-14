package edu.ucsd.cse110.successorator.lib.domain;

import org.junit.Before;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.util.Subject;

//ChatGPT aided in creating this test
public class ItemRepoTest {

    private InMemoryDataSource dataSource;
    private ItemRepository itemRepository;
    @Before
    public void setUp() {
        dataSource = new InMemoryDataSource(); // Instantiate the DataSource
        itemRepository = new SimpleItemRepository(dataSource); // Instantiate the ItemRepository with the DataSource
    }
    //Note that for now, we only need to test the prepend method since that is all we are currently using when we add an item to the display
    @Test
    public void testPrepend() {
        // Add some items to the DataSource
        List<Item> items = new ArrayList<>();
        dataSource = new InMemoryDataSource();
        itemRepository = new SimpleItemRepository(dataSource);
        items.add(new Item("Item 1", 1, 1));
        items.add(new Item("Item 2", 2, 2));
        dataSource.putFlashcards(items);

        // Call the prepend method with a new item
        itemRepository.prepend(new Item("New Item", 3, 0));

        // Verify that the new item was added with the correct sort order
        assertEquals(3, dataSource.getFlashcards().size());
        assertEquals(1, dataSource.getMinSortOrder());
    }
}
