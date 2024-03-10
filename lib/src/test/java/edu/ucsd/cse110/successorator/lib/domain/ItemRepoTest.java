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
                mockTime, false, "NONE", false, false));
        items.add(new Item("Item 2", 2, 2, true,
                mockTime, false, "NONE", false, false));
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
                mockTime, false, "NONE", false, false);

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
                mockTime, false, "NONE", false, false));
        items.add(new Item("Item 2", 2, 2, true,
                mockTime, false, "NONE", false, false));

        // Call the save method with the list
        itemRepository.save(items);

        // Verify that the items were saved to the DataSource
        assertTrue(dataSource.getItems().containsAll(items));
    }

    @Test
    public void testRemove() {
        // Add an item to the DataSource
        Item item = new Item("Test Item", 1, 1, false,
                mockTime, false, "NONE", false, false);
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
                mockTime, false, "NONE", false, false));
        items.add(new Item("Item 2", 2, 2, false,
                mockTime, false, "NONE", false, false));
        items.add(new Item("Item 3", 3, 3, true,
                mockTime, false, "NONE", false, false));
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
                mockTime, false, "NONE", false, false));
        items.add(new Item("Complete Item 2", 2, 2, true,
                mockTime, false, "NONE", false, false));
        dataSource.putItems(items);

        // Call the append method with a new item
        itemRepository.append(new Item("New Item", 3, 3, false,
                mockTime, false, "NONE", false, false));

        // Verify that the new item was not added (no incomplete items)
        assertEquals(2, dataSource.getItems().size());
    }

    @Test
    public void testAppend_WithIncompleteItems() {
        // Add some items to the DataSource (one incomplete and one complete)
        List<Item> items = new ArrayList<>();
        items.add(new Item("Incomplete Item", 1, 1, false,
                mockTime, false, "NONE", false, false));
        items.add(new Item("Complete Item", 2, 2, true,
                mockTime, false, "NONE", false, false));
        dataSource.putItems(items);

        // Call the append method with a new incomplete item
        itemRepository.append(new Item("New Incomplete Item", 3, 3, false,
                mockTime, false, "NONE", false, false));

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
                mockTime, false, "NONE", false, false));
        items.add(new Item("Item 2", 2, 2, false,
                mockTime, false, "NONE", false, false));
        dataSource.putItems(items);

        // Call the prepend method with a new item
        itemRepository.prepend(new Item("New Item", 3, 0, false,
                mockTime, false, "NONE", false, false));


        // Verify that the new item was added with the correct sort order
        assertEquals(3, dataSource.getItems().size());
        assertEquals(1, dataSource.getMinSortOrder());
    }


    @Test
    public void testMarkCompleteOrIncomplete() {
        // Add an incomplete item to the DataSource
        Item item = new Item("Incomplete Item", 1, 1, false,
                mockTime, false, "NONE", false, false);
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
               mockTime, false, "NONE", false, false);
        dataSource.putItem(item);

        // Call the markCompleteOrIncomplete method
        itemRepository.markRecurring(item.id());

        // Verify that the item's done status was toggled
        assertTrue(dataSource.getItems().get(0).isRecurring());
    }

    @Test
    public void testRemoveComplete(){
        Item item = new Item("complete Item", 1, 1, true,
                mockTime, false, "NONE", false, false);
        Item item2 = new Item("Incomplete Item", 1, 1, false,
                mockTime, false, "NONE", false, false);
        dataSource.putItem(item);
        dataSource.putItem(item2);

        //Call removeAllComplete
        itemRepository.removeAllComplete();

        //Check the itemRepository size
        assertEquals(dataSource.getItems().size(), 1);

        //Test with multiple completed Items
        Item item3 = new Item("Complete Item", 2, 2, true,
                mockTime, false, "NONE", false, false);
        dataSource.putItem(item);
        dataSource.putItem(item2);
        dataSource.putItem(item3);

        itemRepository.removeAllComplete();

        assertEquals(dataSource.getItems().size(), 1);

    }
    @Test
    /*
    BDD Scenario US2: No recurrence/One-time
    Given its Saturday, Feb 24th
    And I want to add the one time goal “Pay ticket” for today
    When I click the “+” button
    And I enter “Pay ticket”
    And I select “One-time”
    And I select “Create”
    Then I see the goal added to today’s to do list
     */
    public void testAddGoalRecurrence(){
        ZonedDateTime time = ZonedDateTime.of(java.time.LocalDate.of ( 2024 , 2 , 24 ) , java.time.LocalTime.of ( 9 , 30 ) , java.time.ZoneId.of ( "America/New_York" ) );
        Item item = new Item("Pay ticket", 1, 1, false,
                mockTime, false, "NONE", false, false);
        dataSource.putItem(item);

        assertEquals(dataSource.getItems().get(0), item);

    }

    @Test
    /*
    BDD Scenario US6: Adding Goals to pending
    Given I am in the pending list view
    And I want to add “Call Mom” to pending
    When I tap the “+” button
    And I enter the goal “Call Mom”
    And I tap the create button
    Then I see a goal in the Pending List view
    And the goal says “Call Mom”
     */
    public void testAddGoalsToPending(){
        Item item = new Item("Call Mom", 1, 1, false,
                mockTime, false, "NONE", true, false);
        dataSource.putItem(item);

        String description = "Call Mom";
        //Test the description
        assertEquals(dataSource.getItems().get(0).getDescription(), description);
    }

    @Test
    /*
    BDD Scenario US8: Adding Goals to next day
    Given I am in the “Today” List view
    When I tap the “v”
    And I tap Tomorrow
    Then I am taken to the Tomorrow view.
    When I tap “+”
    And add the goal “Get Mail”
    Then the goal “Get Mail” is added to the Tomorrow view.
     */
    public void testAddGoalsToNextDay(){
        Item item = new Item("Get Mail", 1, 1, false,
                mockTime, false, "NONE", true, true);
        dataSource.putItem(item);

        assertTrue(dataSource.getItems().get(0).isTomorrow());
    }

    @Test
    /*
    BDD Scenario US9: Moving Pending goals
    Given the app is opened
    and there is one goal on the pending list
    and I have held the goal for long enough
    and the menu popped up with the following options: Move to Today, Move to Tomorrow, Finish, Delete
    When I press ‘Move to Today’
    Then the goal is gone from the pending list and appears on today’s list
     */
    public void testMovePendingGoal(){
        Item item = new Item("Some Goal", 1, 1, false,
                mockTime, false, "NONE", true, false);
        dataSource.putItem(item);
        //Mark Pending as false
        dataSource.markPending(item.id());

        assertFalse(dataSource.getItems().get(0).isPending());
    }


}
