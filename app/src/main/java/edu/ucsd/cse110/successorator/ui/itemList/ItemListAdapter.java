package edu.ucsd.cse110.successorator.ui.itemList;
import java.time.ZonedDateTime;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.databinding.ItemCardBinding;
import edu.ucsd.cse110.successorator.lib.domain.Item;
import edu.ucsd.cse110.successorator.ui.itemList.dialog.MovePendingItemsDialogFragment;

public class ItemListAdapter extends ArrayAdapter<Item> {

    Consumer<Integer> onDeleteClick;
    Consumer<Item> appendClick;
    Consumer<Item> prependClick;
    Consumer<Integer> strikethroughClick;

    String fragment;
    FragmentManager fragmentManager;


    public ItemListAdapter(
            Context context,
            FragmentManager fragmentManager,
            List<Item> items,
            Consumer<Integer> onDeleteClick,
            Consumer<Item> appendClick,
            Consumer<Item> prependClick,
            Consumer<Integer> strikethroughClick,
            String fragment
    ){
        super(context, 0, new ArrayList<>(items));
        this.fragmentManager = fragmentManager;
        this.onDeleteClick = onDeleteClick;
        this.appendClick = appendClick;
        this.prependClick = prependClick;
        this.strikethroughClick = strikethroughClick;
        this.fragment = fragment;

    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the flashcard for this position.
        Item flashcard = getItem(position);

        assert flashcard != null;

        // Check if a view is being reused...
        ItemCardBinding binding;
        if (convertView != null) {
            // if so, bind to it
            binding = ItemCardBinding.bind(convertView);
        } else {
            // otherwise inflate a new view from our layout XML.
            var layoutInflater = LayoutInflater.from(getContext());
            binding = ItemCardBinding.inflate(layoutInflater, parent, false);
        }

        if(flashcard.isDone()){
            binding.cardFrontText.setPaintFlags(binding.cardFrontText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            binding.cardFrontText.setPaintFlags(binding.cardFrontText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        if(fragment.equals("HOME")) {
            binding.getRoot().setOnClickListener(v -> {
                var id = flashcard.id();
                assert id != null;
                strikethroughClick.accept(id);
                getItem(position).markDone();
                onDeleteClick.accept(id);
                if (flashcard.isDone()) {
                    appendClick.accept(flashcard);
                } else {
                    prependClick.accept(flashcard);
                }
            });
        }else if(fragment.equals("RECURRING")){
            binding.getRoot().setOnClickListener(v -> {
                var id = flashcard.id();
                assert id != null;
                onDeleteClick.accept(id);
            });
        } else if (fragment.equals("PENDING")){
            // TODO: Note that current functionality is duplicate of Recurring;
            // TODO: Currently placeholder for functionality to be implementing in US9
            binding.getRoot().setOnLongClickListener(v -> {
                var id = flashcard.id();
                assert id != null;

                showMovePendingItemsDialog(getItem(position));

                return true;
            });
        }

        // Populate the view with the flashcard's data.
        binding.cardFrontText.setText(flashcard.getDescription());
        return binding.getRoot();

    }

    // The below methods aren't strictly necessary, usually.
    // But get in the habit of defining them because they never hurt
    // (as long as you have IDs for each item) and sometimes you need them.

    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public long getItemId(int position) {
        var item = getItem(position);
        assert item != null;

        var id = item.id();
        assert id != null;

        return id;
    }

    public void showMovePendingItemsDialog(Item item) {
        MovePendingItemsDialogFragment dialogFragment = MovePendingItemsDialogFragment.newInstance(item);
        dialogFragment.show(fragmentManager, "MovePendingItemsDialogFragment");
    }
}
