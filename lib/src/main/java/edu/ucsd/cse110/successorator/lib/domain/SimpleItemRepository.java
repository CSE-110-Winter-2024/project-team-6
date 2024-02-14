package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class SimpleItemRepository implements ItemRepository {
    private final InMemoryDataSource dataSource;


    public SimpleItemRepository(InMemoryDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Subject<Item> find(int id) {
        return dataSource.getFlashcardSubject(id);
    }

    @Override
    public Subject<List<Item>> findAll() {
        return dataSource.getAllFlashcardsSubject();
    }

    @Override
    public void save(Item flashcard) {
        dataSource.putFlashcard(flashcard);
    }

    @Override
    public void save(List<Item> flashcards) {
        dataSource.putFlashcards(flashcards);
    }

    @Override
    public void remove(int id) {
        dataSource.removeFlashcard(id);
    }
    @Override
    public int size(){return dataSource.getFlashcards().size(); }

    @Override
    public void append(Item item){
        List<Item> items = dataSource.getFlashcards();

        int lastIncompleteIndex = -1;
        for(int i = 0; i < items.size(); i++){
            Item temp = items.get(i);
            if(!temp.isDone()){
                lastIncompleteIndex = i;
            }
        }
        if (lastIncompleteIndex == -1) {
            //dataSource.putFlashcard(item.withSortOrder(dataSource.getMaxSortOrder() + 1));
            return;
        }
        int lastIncompleteSortOrder = items.get(lastIncompleteIndex).sortOrder();
        dataSource.shiftSortOrders(lastIncompleteSortOrder + 1, dataSource.getMaxSortOrder(), 1);
        dataSource.putFlashcard(item.withSortOrder(lastIncompleteSortOrder + 1));
    }

    @Override
    public void prepend(Item flashcard){
        dataSource.shiftSortOrders(0, dataSource.getMaxSortOrder(), 1);
        dataSource.putFlashcard(
                flashcard.withSortOrder(dataSource.getMinSortOrder() - 1)
        );
    }

    @Override
    public void markCompleteOrIncomplete(int id){
        dataSource.markCompleteOrIncomplete(id);
    }


}
