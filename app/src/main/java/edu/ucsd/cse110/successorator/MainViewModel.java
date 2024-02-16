package edu.ucsd.cse110.successorator;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.util.Log;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.Item;
import edu.ucsd.cse110.successorator.lib.domain.ItemRepository;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class MainViewModel extends ViewModel {
    // Domain state (true "Model" state)
    private final ItemRepository itemRepository;

    // UI state
    private final MutableSubject<List<Item>> orderedCards;
    private final MutableSubject<Item> topCard;
    private final MutableSubject<String> displayedText;

    private final MutableSubject<Boolean> isDone;

    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getItemRepository());
                    });

    public MainViewModel(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;

        // Create the observable subjects.
        this.orderedCards = new SimpleSubject<>();
        this.topCard = new SimpleSubject<>();
        this.displayedText = new SimpleSubject<>();
        this.isDone = new SimpleSubject<>();

        // When the list of cards changes (or is first loaded), reset the ordering.
        itemRepository.findAll().observe(cards -> {
            if (cards == null) return; // not ready yet, ignore

            var newOrderedCards = cards.stream()
                    .sorted(Comparator.comparingInt(Item::sortOrder))
                    .collect(Collectors.toList());
            orderedCards.setValue(newOrderedCards);
        });

    }

    public void markCompleteOrIncomplete(int id) { itemRepository.markCompleteOrIncomplete(id); }

    public Subject<String> getDisplayedText() {
        return displayedText;
    }


    public Subject<List<Item>> getOrderedCards() {
        return orderedCards;
    }


    public void append(Item item){
        itemRepository.append(item);
    }

    public void prepend(Item item){
        itemRepository.prepend(item);
    }

    public void remove(int id){
        itemRepository.remove(id);
    }

    public int size(){return itemRepository.size();}

}
