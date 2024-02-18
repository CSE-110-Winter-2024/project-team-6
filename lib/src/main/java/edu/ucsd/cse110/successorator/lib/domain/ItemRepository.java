package edu.ucsd.cse110.successorator.lib.domain;


import java.util.List;

import edu.ucsd.cse110.successorator.lib.util.Subject;

public interface ItemRepository {
    Subject<Item> find(int id);

    Subject<List<Item>> findAll();

    void save(Item flashcard);

    void save(List<Item> flashcards);

    void remove(int id);

    void append(Item flashcard);

    void prepend(Item flashcard);

    void markCompleteOrIncomplete(int id);

    int size();

    void removeAllComplete();

}