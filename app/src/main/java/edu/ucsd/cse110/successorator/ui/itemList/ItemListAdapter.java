package edu.ucsd.cse110.successorator.ui.itemList;
import java.time.ZonedDateTime;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.databinding.ItemCardBinding;
import edu.ucsd.cse110.successorator.lib.domain.Item;

public class ItemListAdapter extends ArrayAdapter<Item> {

    Consumer<Integer> onDeleteClick;
    Consumer<Item> appendClick;
    Consumer<Item> prependClick;
    Consumer<Integer> strikethroughClick;

    String fragment;

    Context context;

    public ItemListAdapter(
            Context context,
            List<Item> items,
            Consumer<Integer> onDeleteClick,
            Consumer<Item> appendClick,
            Consumer<Item> prependClick,
            Consumer<Integer> strikethroughClick,
            String fragment
    ){
        super(context, 0, new ArrayList<>(items));
        this.onDeleteClick = onDeleteClick;
        this.appendClick = appendClick;
        this.prependClick = prependClick;
        this.strikethroughClick = strikethroughClick;
        this.fragment = fragment;

        this.context = context;

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


        // Strikethroughs not affected in recurring view
        // They are also not reflected in recurring tasks in the tomorrow view that are not already done
        if(flashcard.isDone() && !fragment.equals("RECURRING") && !(fragment.equals("TOMORROW") && flashcard.isRecurring())){
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
        } else if (fragment.equals("TOMORROW")) {
            binding.getRoot().setOnClickListener(v -> {
                var id = flashcard.id();
                assert id != null;

                // If Recurring Task still active in Today, don't mark done
                if (flashcard.isRecurring() && !flashcard.isDone()) {
                    Toast.makeText(context, "This goal is still active for Today. If you've finished this goal for Today, mark it finished in that view!", Toast.LENGTH_LONG).show();
                } else {
                    if (!flashcard.isRecurring()) { // One time tasks can be struck through as normal
                        strikethroughClick.accept(id);
                        getItem(position).markDone();
                        onDeleteClick.accept(id);
                        if (flashcard.isDone()) {
                            appendClick.accept(flashcard);
                        } else {
                            prependClick.accept(flashcard);
                        }
                    } else {  // If recurring and done
                        strikethroughClick.accept(id);
                        onDeleteClick.accept(id);
                        Log.d("Tomorrow Recurring", String.valueOf(flashcard.isDone()));
                        if (flashcard.isDone()) {
                            appendClick.accept(flashcard);
                        } else {
                            prependClick.accept(flashcard);
                        }
                    }
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
            binding.getRoot().setOnClickListener(v -> {
                var id = flashcard.id();
                assert id != null;
                onDeleteClick.accept(id);
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


}
