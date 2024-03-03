package edu.ucsd.cse110.successorator.ui.itemList;

import android.content.Context;

import androidx.core.util.Consumer;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.Item;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
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


public class TomorrowListAdapter extends ArrayAdapter<Item> {
    Consumer<Integer> onDeleteClick;
    Consumer<Item> appendClick;
    Consumer<Item> prependClick;
    Consumer<Integer> strikethroughClick;

    public TomorrowListAdapter (
            Context context,
            List<Item> items,
            Consumer<Integer> onDeleteClick,
            Consumer<Item> appendClick,
            Consumer<Item> prependClick,
            Consumer<Integer> strikethroughClick
    ){
        super(context, 0, new ArrayList<>(items));
        this.onDeleteClick = onDeleteClick;
        this.appendClick = appendClick;
        this.prependClick = prependClick;
        this.strikethroughClick = strikethroughClick;

    }
    //need to change the first parameter to get whatever items are
    //supposed to be on this view (tomorrow)
    //change the return
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
