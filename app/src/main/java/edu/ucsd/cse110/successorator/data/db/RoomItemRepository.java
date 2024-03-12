package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.util.LiveDataSubjectAdapter;
import edu.ucsd.cse110.successorator.lib.domain.Item;
import edu.ucsd.cse110.successorator.lib.domain.ItemRepository;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class RoomItemRepository implements ItemRepository {
    private final ItemDao itemDao;

    public RoomItemRepository(ItemDao itemDao){
        this.itemDao = itemDao;
    }

    @Override
    public Subject<Item> find(int id){
        var entityLiveData = itemDao.findAsLiveData(id);
        var itemLiveData = Transformations.map(entityLiveData, ItemEntity::toItem);
        return new LiveDataSubjectAdapter<>(itemLiveData);
    }

    @Override
    public Subject<List<Item>> findAll(){
        var entitiesLiveData = itemDao.findAllAsLiveData();
        var itemsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(ItemEntity::toItem)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(itemsLiveData);
    }

    @Override
    public void save(Item item){
        itemDao.insert(ItemEntity.fromItem(item));
    }

    @Override
    public void save(List<Item> items){
        var entities = items.stream()
                .map(ItemEntity::fromItem)
                .collect(Collectors.toList());
        itemDao.insert(entities);
    }

    @Override
    public void append(Item item){
        itemDao.append(ItemEntity.fromItem(item));
    }

    @Override
    public void prepend(Item item){
        itemDao.prepend(ItemEntity.fromItem(item));
    }

    @Override
    public void remove(int id){
        itemDao.delete(id);
    }

    @Override
    public int size(){return itemDao.count();}

    @Override
    public void markCompleteOrIncomplete(int id){itemDao.markCompleteOrIncomplete(id);}

    @Override
    public void removeAllComplete(){itemDao.removeAllComplete();};

    @Override
    public void resetFinishedRecurring(){itemDao.resetFinishedRecurring();}

    @Override
    public void markRecurring(int id) {itemDao.markRecurringOrNonrecurring(id);}

    @Override
    public void markPending(int id) {itemDao.markPending(id);}

    @Override
    public void markTomorrow(int id){itemDao.markTomorrow(id);}

}
