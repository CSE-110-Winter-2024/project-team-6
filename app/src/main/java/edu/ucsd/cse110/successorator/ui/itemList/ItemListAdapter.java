package edu.ucsd.cse110.successorator.ui.itemList;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;
import androidx.fragment.app.FragmentManager;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.R;
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
    int advanceDate;


    Context context;

    public ItemListAdapter(
            Context context,
            FragmentManager fragmentManager,
            List<Item> items,
            Consumer<Integer> onDeleteClick,
            Consumer<Item> appendClick,
            Consumer<Item> prependClick,
            Consumer<Integer> strikethroughClick,
            String fragment,
            int advanceDate
    ){
        super(context, 0, new ArrayList<>(items));
        this.fragmentManager = fragmentManager;
        this.onDeleteClick = onDeleteClick;
        this.appendClick = appendClick;
        this.prependClick = prependClick;
        this.strikethroughClick = strikethroughClick;
        this.fragment = fragment;
        this.advanceDate = advanceDate;
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
        if(flashcard.isDone() && !fragment.equals("RECURRING") && !(fragment.equals("TOMORROW") && !flashcard.getDate().isAfter(ZonedDateTime.now().plusDays(advanceDate)))){
            binding.cardFrontText.setPaintFlags(binding.cardFrontText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            binding.cardFrontText.setPaintFlags(binding.cardFrontText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        // Naive implementation of adding tags.

        // Home by default
        // binding.TAG.setBackground(ContextCompat.getDrawable(super.getContext(), R.drawable.outline_home));

        if (!flashcard.isDone() || fragment.equals("RECURRING") ||
                (fragment.equals("TOMORROW") && flashcard.isRecurring() && !flashcard.getDate().isAfter(ZonedDateTime.now().plusDays(advanceDate)))) { // Items in recurring never grey
            switch (flashcard.getCategory()) {
                case "HOME":
                    binding.TAG.setBackground(ContextCompat.getDrawable(super.getContext(), R.drawable.outline_home));
                    binding.TAG.setText("H");
                    break;
                case "WORK":
                    binding.TAG.setBackground(ContextCompat.getDrawable(super.getContext(), R.drawable.outline_work));
                    binding.TAG.setText("W");
                    break;
                case "SCHOOL":
                    binding.TAG.setBackground(ContextCompat.getDrawable(super.getContext(), R.drawable.outline_school));
                    binding.TAG.setText("S");
                    break;
                case "ERRAND":
                    binding.TAG.setBackground(ContextCompat.getDrawable(super.getContext(), R.drawable.outline_errands));
                    binding.TAG.setText("E");
                    break;
            }

        } else {
            binding.TAG.setBackground(ContextCompat.getDrawable(super.getContext(), R.drawable.outline_done));
            switch (flashcard.getCategory()) {
                case "HOME":
                    binding.TAG.setText("H");
                    break;
                case "WORK":
                    binding.TAG.setText("W");
                    break;
                case "SCHOOL":
                    binding.TAG.setText("S");
                    break;
                case "ERRAND":
                    binding.TAG.setText("E");
                    break;
            }

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
                if (flashcard.isRecurring() && !flashcard.getDate().isAfter(ZonedDateTime.now().plusDays(advanceDate))){
                    Toast.makeText(context, "This goal is still active for Today.  Please mark it finished in that view!", Toast.LENGTH_LONG).show();
                } else {
                    //if (!flashcard.isRecurring()) { // One time tasks can be struck through as normal
                        strikethroughClick.accept(id);
                        getItem(position).markDone();
                        onDeleteClick.accept(id);
                        if (flashcard.isDone()) {
                            appendClick.accept(flashcard);
                        } else {
                            prependClick.accept(flashcard);
                        }
                        /*
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

                         */
                }
            });
        }else if(fragment.equals("RECURRING")){
            binding.getRoot().setOnLongClickListener(v -> {
                var id = flashcard.id();
                assert id != null;
                onDeleteClick.accept(id);
                return true;
            });
        } else if (fragment.equals("PENDING")){
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
