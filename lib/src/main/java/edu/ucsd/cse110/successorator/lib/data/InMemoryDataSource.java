package edu.ucsd.cse110.successorator.lib.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.Item;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

/**
 * Class used as a sort of "database" of decks and flashcards that exist. This
 * will be replaced with a real database in the future, but can also be used
 * for testing.
 */
public class InMemoryDataSource {
    private int nextId = 0;

    private int minSortOrder = Integer.MAX_VALUE;
    private int maxSortOrder = Integer.MIN_VALUE;

    private final Map<Integer, Item> items
            = new HashMap<>();
    private final Map<Integer, MutableSubject<Item>> itemSubjects
            = new HashMap<>();
    private final MutableSubject<List<Item>> allItemsSubject
            = new SimpleSubject<>();


    public InMemoryDataSource() {
    }

    public final static List<Item> DEFAULT_CARDS = List.of(
            /*
            new Item("Single Responsibility Principle", 0, 0),
            new Item("Open-Closed Principle", 1, 1),
            new Item("Liskov Substitution Principle", 2, 2),
            new Item("Interface Segregation Principle", 3, 3),
            new Item("Dependency Inversion Principle", 4, 4),
            new Item("Least Knowledge Principle (Law of Demeter)", 5, 5)
    */
    );


    public static InMemoryDataSource fromDefault() {
        var data = new InMemoryDataSource();
        data.putItems(DEFAULT_CARDS);
        return data;
    }

    public List<Item> getItems() {
        return List.copyOf(items.values());
    }

    public Item getItem(int id) {
        return items.get(id);
    }

    public Subject<Item> getItemSubject(int id) {
        if (!itemSubjects.containsKey(id)) {
            var subject = new SimpleSubject<Item>();
            subject.setValue(getItem(id));
            itemSubjects.put(id, subject);
        }
        return itemSubjects.get(id);
    }

    public Subject<List<Item>> getAllItemsSubject() {
        return allItemsSubject;
    }

    public int getMinSortOrder() {
        return minSortOrder;
    }

    public int getMaxSortOrder() {
        return maxSortOrder;
    }



    public void putItem(Item card) {
        var fixedCard = preInsert(card);
        items.put(fixedCard.id(), fixedCard);
        postInsert();
        assertSortOrderConstraints();

        if (itemSubjects.containsKey(fixedCard.id())) {
            itemSubjects.get(fixedCard.id()).setValue(fixedCard);
        }
        allItemsSubject.setValue(getItems());
    }

    public void putItems(List<Item> cards) {
        var fixedCards = cards.stream()
                .map(this::preInsert)
                .collect(Collectors.toList());

        fixedCards.forEach(card -> items.put(card.id(), card));
        postInsert();
        assertSortOrderConstraints();

        fixedCards.forEach(card -> {
            if (itemSubjects.containsKey(card.id())) {
                itemSubjects.get(card.id()).setValue(card);
            }
        });
        allItemsSubject.setValue(getItems());
    }

    public void removeItem(int id) {
        var card = items.get(id);
        var sortOrder = card.sortOrder();

        items.remove(id);
        shiftSortOrders(sortOrder, maxSortOrder, -1);

        if (itemSubjects.containsKey(id)) {
            itemSubjects.get(id).setValue(null);
        }
        allItemsSubject.setValue(getItems());
    }

    public void markCompleteOrIncomplete(int id){
        items.get(id).markDone();

        if(itemSubjects.containsKey(id)){
            itemSubjects.get(id).setValue(items.get(id));
        }
        allItemsSubject.setValue(getItems());
    }

    public void markRecurring(int id){
        items.get(id).markRecurring();

        if(itemSubjects.containsKey(id)){
            itemSubjects.get(id).setValue(items.get(id));
        }
        allItemsSubject.setValue(getItems());
    }



    public void shiftSortOrders(int from, int to, int by) {
        var cards = items.values().stream()
                .filter(card -> card.sortOrder() >= from && card.sortOrder() <= to)
                .map(card -> card.withSortOrder(card.sortOrder() + by))
                .collect(Collectors.toList());

        putItems(cards);
    }

    /**
     * Private utility method to maintain state of the fake DB: ensures that new
     * cards inserted have an id, and updates the nextId if necessary.
     */
    private Item preInsert(Item item) {
        var id = item.id();
        if (id == null) {
            // If the item has no id, give it one.
            item = item.withId(nextId++);
        }
        else if (id > nextId) {
            // If the item has an id, update nextId if necessary to avoid giving out the same
            // one. This is important for when we pre-load cards like in fromDefault().
            nextId = id + 1;
        }
        return item;
    }

    /**
     * Private utility method to maintain state of the fake DB: ensures that the
     * min and max sort orders are up to date after an insert.
     */
    private void postInsert() {
        // Keep the min and max sort orders up to date.
        minSortOrder = items.values().stream()
                .map(Item::sortOrder)
                .min(Integer::compareTo)
                .orElse(Integer.MAX_VALUE);

        maxSortOrder = items.values().stream()
                .map(Item::sortOrder)
                .max(Integer::compareTo)
                .orElse(Integer.MIN_VALUE);
    }

    /**
     * Safety checks to ensure the sort order constraints are maintained.
     * <p></p>
     * Will throw an AssertionError if any of the constraints are violated,
     * which should never happen. Mostly here to make sure I (Dylan) don't
     * write incorrect code by accident!
     */
    private void assertSortOrderConstraints() {
        // Get all the sort orders...
        var sortOrders = items.values().stream()
                .map(Item::sortOrder)
                .collect(Collectors.toList());

        // Non-negative...
        assert sortOrders.stream().allMatch(i -> i >= 0);

        // Unique...
        assert sortOrders.size() == sortOrders.stream().distinct().count();

        // Between min and max...
        assert sortOrders.stream().allMatch(i -> i >= minSortOrder);
        assert sortOrders.stream().allMatch(i -> i <= maxSortOrder);
    }
}
