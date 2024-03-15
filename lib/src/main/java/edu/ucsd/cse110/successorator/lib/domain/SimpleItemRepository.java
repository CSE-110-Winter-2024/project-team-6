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
        return dataSource.getItemSubject(id);
    }

    @Override
    public Subject<List<Item>> findAll() {
        return dataSource.getAllItemsSubject();
    }

    @Override
    public void save(Item item) {
        dataSource.putItem(item);
    }

    @Override
    public void save(List<Item> items) {
        dataSource.putItems(items);
    }

    @Override
    public void remove(int id) {
        dataSource.removeItem(id);
    }
    @Override
    public int size(){return dataSource.getItems().size(); }

    @Override
    public void append(Item item){
        List<Item> items = dataSource.getItems();

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
        dataSource.putItem(item.withSortOrder(lastIncompleteSortOrder + 1));
    }


    @Override
    public void prepend(Item item){
        dataSource.shiftSortOrders(0, dataSource.getMaxSortOrder(), 1);
        dataSource.putItem(
                item.withSortOrder(dataSource.getMinSortOrder() - 1)
        );
    }

    @Override
    public void removeAllComplete(){
        List<Item> items = dataSource.getItems();
        for(int i = 0; i < items.size(); i++){
            Item temp = items.get(i);
            if(temp.isDone() && !temp.isRecurring() && !temp.isTomorrow()){
                remove(temp.id());
            }
        }
    }

    @Override
    public void resetFinishedRecurring(){
        List<Item> items = dataSource.getItems();
        for(int i = 0; i < items.size(); i++){
            Item temp = items.get(i);
            if(temp.isDone() && temp.isRecurring()){
                dataSource.markCompleteOrIncomplete(temp.id());
                if(temp.getRecurringType().equals("WEEKLY")){
                    dataSource.advanceWeek(temp.id());
                }else if(temp.getRecurringType().equals("MONTHLY")){
                    dataSource.advanceMonth(temp.id());
                }else if(temp.getRecurringType().equals("DAILY")){
                    dataSource.advanceDay(temp.id());

                }else if(temp.getRecurringType().equals("YEARLY")) {
                    dataSource.advanceYear(temp.id());
                }
                dataSource.putItem(temp);
            }
        }
    }


    @Override
    public void markCompleteOrIncomplete(int id){
        dataSource.markCompleteOrIncomplete(id);
    }

    @Override
    public void markRecurring(int id) { dataSource.markRecurring(id); }

    @Override
    public void markPending(int id) { dataSource.markPending(id);}

    @Override
    public void markTomorrow(int id){dataSource.markTomorrow(id);}


}
