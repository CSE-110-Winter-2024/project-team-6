package edu.ucsd.cse110.successorator.ui.itemList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.databinding.ItemCardBinding;
import edu.ucsd.cse110.successorator.lib.domain.Item;

public class ItemListAdapter extends ArrayAdapter<Item> {

    Consumer<Integer> onDeleteClick;

    public ItemListAdapter(
            Context context,
            List<Item> items,
            Consumer<Integer> onDeleteClick
    ){
        super(context, 0, new ArrayList<>(items));
        this.onDeleteClick = onDeleteClick;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the flashcard for this position.
        var flashcard = getItem(position);
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
