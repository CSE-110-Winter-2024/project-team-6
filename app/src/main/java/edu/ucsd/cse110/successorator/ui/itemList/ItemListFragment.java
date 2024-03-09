package edu.ucsd.cse110.successorator.ui.itemList;




import android.content.Context;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.DateFormatter;

import edu.ucsd.cse110.successorator.ui.itemList.dialog.CreateItemDialogFragment;
import edu.ucsd.cse110.successorator.databinding.FragmentCardListBinding;

public class ItemListFragment extends ParentFragment {
    //private MainViewModel activityModel;
    private FragmentCardListBinding view;
    private ItemListAdapter adapter;
    private TextView dateText;
    private String formattedDate;
    private DateFormatter dateFormatter;

    // Track days to artificially advance
    private int advanceCount;

    private SharedPreferences sharedPreferences;

    public ItemListFragment() {
        // Required empty public constructor
    }

    public static ItemListFragment newInstance() {
        ItemListFragment fragment = new ItemListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.adapter = new ItemListAdapter(requireContext(), List.of(), activityModel::remove, activityModel::append, activityModel::prepend, activityModel::markCompleteOrIncomplete, "HOME");

        // Persistence of Date
        sharedPreferences = requireActivity().getApplicationContext().getSharedPreferences("formatted_date", Context.MODE_PRIVATE);
        advanceCount = sharedPreferences.getInt("advance_count", 0);

        activityModel.getOrderedCards().observe(cards -> {
            if(cards == null) return;
            adapter.clear();

            for(int i = 0; i < cards.size(); i++){
                if(cards.get(i).getDate().getDayOfMonth() == ZonedDateTime.now().plusDays(advanceCount).getDayOfMonth()
                        && !cards.get(i).isPending()){
                    Log.d("Card onResume", "card day of month: " +
                            String.valueOf(cards.get(i).getDate().getDayOfMonth()) +
                            " Curr Month: " +
                            ZonedDateTime.now().plusDays(advanceCount).getDayOfMonth() +
                            " " + cards.get(i).getDescription());
                    adapter.add(cards.get(i));
                }
            }


            adapter.addAll(new ArrayList<>(cards));
            adapter.notifyDataSetChanged();
            for(int i = 0; i < cards.size(); i++) {
                Log.d("Ordered cards changed", cards.get(i).sortOrder() + " " + i + " " + cards.get(i).getDescription());
            }

            if(activityModel.size() != 0 && view != null){
                view.placeholderText.setVisibility(View.GONE);
            }
            if(activityModel.size() == 0 && view != null){
                view.placeholderText.setVisibility(View.VISIBLE);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //super.onCreateView(inflater, container, savedInstanceState);

        this.view = FragmentCardListBinding.inflate(inflater, container, false);
        // Set the adapter on the ListView
        view.cardList.setAdapter(adapter);
        dateText = this.view.dateView;

        dateFormatter = new DateFormatter(ZonedDateTime.now());

        view.addItem.setOnClickListener(v -> {
            var dialogFragment = CreateItemDialogFragment.newInstance();

            dialogFragment.show(getParentFragmentManager(),"CreateItemDialogFragment");
        });

        // When pressing the add date button, the Date will advance by 24hrs
        view.addDay.setOnClickListener(v -> {
            // Get number of dates to advance
            advanceCount = sharedPreferences.getInt("advance_count", 0) + 1;

            // Apply the number of advanced days to the current date
            String formattedDate = dateFormatter.getPersistentDate(ZonedDateTime.now().plusDays(advanceCount));

            // Save formatted date for persistence
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("formatted_date_today", formattedDate);
            editor.putInt("advance_count", advanceCount);
            editor.apply();

            // Update UI with formatted date
            dateText.setText(dateFormatter.getTodaysDate(ZonedDateTime.now().plusDays(advanceCount)));
            activityModel.removeAllComplete();

            updateFragment();
        });

        return view.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){

    }
    @Override
    public void onResume() {
        super.onResume();

        // Get formatted date and display.
        String savedDate = sharedPreferences.getString("formatted_date_today", "ERR");
        int advanceCount = sharedPreferences.getInt("advance_count", 0);

        // Determine which goals to load
        updateFragment();

        // Apply the number of advanced days to the current date
        String currDate = dateFormatter.getPersistentDate(ZonedDateTime.now().plusDays(advanceCount));

        // Check for date changes
        if (!(currDate.equals(savedDate))) {
            activityModel.removeAllComplete();

            // Display the formatted date
            formattedDate = dateFormatter.getTodaysDate(ZonedDateTime.now().plusDays(advanceCount));

            // Edit date persistence
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("formatted_date_today", currDate);
            editor.apply();
        }

        // Set date text from last saved date
        dateText.setText(dateFormatter.getTodaysDate(ZonedDateTime.now().plusDays(advanceCount)));
    }

    // Determine which goals to load associated with the date
    public void updateFragment() {
        activityModel.getOrderedCards().observe(cards -> {
            if(cards == null) return;
            adapter.clear();

            for(int i = 0; i < cards.size(); i++){
                if(cards.get(i).getDate().getDayOfMonth() == ZonedDateTime.now().plusDays(advanceCount).getDayOfMonth()
                        && !cards.get(i).isPending()){
                    adapter.add(cards.get(i));
                }
            }


            //adapter.addAll(new ArrayList<>(cards));
            adapter.notifyDataSetChanged();
            for(int i = 0; i < cards.size(); i++) {
                Log.d("Ordered cards changed", cards.get(i).sortOrder() + " " + i + " " + cards.get(i).getDescription());
            }

            if(activityModel.size() != 0 && view != null){
                view.placeholderText.setVisibility(View.GONE);
            }
            if(activityModel.size() == 0 && view != null){
                view.placeholderText.setVisibility(View.VISIBLE);
            }
        });
    }
}
