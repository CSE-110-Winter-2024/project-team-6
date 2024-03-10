package edu.ucsd.cse110.successorator.lib.domain;


import java.util.List;

import edu.ucsd.cse110.successorator.lib.util.Subject;

public interface ItemRepository {
    Subject<Item> find(int id);

    Subject<List<Item>> findAll();

    void save(Item item);

    void save(List<Item> items);

    void remove(int id);

    void append(Item item);

    void prepend(Item item);

    void markCompleteOrIncomplete(int id);

    int size();

    void removeAllComplete();

    void markRecurring(int id);

    void markPending(int id);

    void markTomorrow(int id);

}