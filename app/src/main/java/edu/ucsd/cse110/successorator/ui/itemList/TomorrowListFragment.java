package edu.ucsd.cse110.successorator.ui.itemList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.DateFormatter;
import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentCardListBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentTomorrowListBinding;
import edu.ucsd.cse110.successorator.lib.domain.Item;
import edu.ucsd.cse110.successorator.ui.itemList.dialog.CreateItemDialogFragment;
import edu.ucsd.cse110.successorator.ui.itemList.dialog.CreateTomorrowItemDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TomorrowListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TomorrowListFragment extends ParentFragment {
    private MainViewModel activityModel;
    private FragmentTomorrowListBinding view;
    private ItemListAdapter adapter;
    private TextView dateText;
    private String formattedDate;
    private DateFormatter dateFormatter;

    private SharedPreferences sharedPreferences;

    private int advanceCount;

    public TomorrowListFragment() {
        // Required empty public constructor
    }

    public static TomorrowListFragment newInstance() {
        TomorrowListFragment fragment = new TomorrowListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the Model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        this.adapter = new ItemListAdapter(requireContext(), getParentFragmentManager(), List.of(), activityModel::remove, activityModel::append, activityModel::prepend, activityModel::markCompleteOrIncomplete, "TOMORROW");


        activityModel.getOrderedCards().observe(cards -> {
            if(cards == null) return;
            adapter.clear();

            for(int i = 0; i < cards.size(); i++){
                if(cards.get(i).isTomorrow()){
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = FragmentTomorrowListBinding.inflate(inflater, container, false);
        view.cardList.setAdapter(adapter);
        dateText = this.view.dateView;
        // Set up a DateFormatter
        dateFormatter = new DateFormatter(ZonedDateTime.now());

        // Persistence of Date
        sharedPreferences = requireActivity().getSharedPreferences("formatted_date", Context.MODE_PRIVATE);
        int advanceCount = sharedPreferences.getInt("advance_count", 0);

        //change the title to tomorrow
        formattedDate = dateFormatter.getTomorrowsDate(ZonedDateTime.now().plusDays(advanceCount));

        // Update UI with formatted date
        dateText.setText(formattedDate);
        updateFragment();

        view.addItem.setOnClickListener(v ->{
            var dialogFragment = CreateTomorrowItemDialogFragment.newInstance();
            // Unsure if we should use getSupportFragmentManager() or getParentFragmentManager()
            dialogFragment.show(getParentFragmentManager(),"CreateTomorrowItemDialogFragment");
        });

        return view.getRoot();
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
    }
    public void onResume() {
        super.onResume();
        // Get formatted date and display, the saved date for current day
        String savedDateForTodayView = sharedPreferences.getString("formatted_date_today", "ERR");
        advanceCount = sharedPreferences.getInt("advance_count", 0);


        // Determine which goals to load dependent on the date
        updateFragment();

        String currTodayDate = dateFormatter.getPersistentDate(ZonedDateTime.now().plusDays(advanceCount));

        // Check for date changes
        if (!(currTodayDate.equals(savedDateForTodayView))) {
            activityModel.removeAllComplete();
            // Format a string for tomorrow's date
            formattedDate = dateFormatter.getTomorrowsDate(ZonedDateTime.now().plusDays(advanceCount + 1));

            // Edit date persistence
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("formatted_date_today", currTodayDate);
            editor.apply();
        }

        dateText.setText(dateFormatter.getTomorrowsDate(ZonedDateTime.now().plusDays(advanceCount)));
    }

    // Determine which goals to load dependent on the date
    public void updateFragment() {

        // Get focus mode
        String focusMode = sharedPreferences.getString("focus_mode", "NONE");

        activityModel.getOrderedCards().observe(cards -> {
            if(cards == null) return;
            adapter.clear();
            ZonedDateTime tempTime = ZonedDateTime.now().plusDays(advanceCount + 1);

            String[] arrayOfCategories = {"HOME","WORK","SCHOOL","ERRAND"};

            int timesToCheck = arrayOfCategories.length;
            // There is a focus mode selected
            if (!focusMode.equals("NONE")) {
                timesToCheck = 1;
            }

            for(int j = 0; j < timesToCheck; j++) {  // Go through all category tags
                for (int i = 0; i < cards.size(); i++) {
                    if ((focusMode.equals("NONE") && cards.get(i).getCategory().equals(arrayOfCategories[j])) ||
                         cards.get(i).getCategory().equals(focusMode)) {
                        if (!cards.get(i).isPending() && !cards.get(i).isDone()) {
                            if ((tempTime.getDayOfYear() >= cards.get(i).getDate().getDayOfYear() || tempTime.getYear() > cards.get(i).getDate().getYear())) {
                                if (cards.get(i).isRecurring()) {
                                    if (cards.get(i).getRecurringType().equals("WEEKLY") && cards.get(i).getDate().getDayOfWeek().toString().equals(tempTime.getDayOfWeek().toString())) {
                                        adapter.add(cards.get(i));
                                        //add all monthly recurring tasks recurring today
                                    } else if (cards.get(i).getRecurringType().equals("MONTHLY") && cards.get(i).getDate().getDayOfMonth() == tempTime.getDayOfMonth()) {
                                        adapter.add(cards.get(i));
                                        //add all daily recurring tasks
                                    } else if (cards.get(i).getRecurringType().equals("DAILY")) {
                                        adapter.add(cards.get(i));
                                    } else if (cards.get(i).getRecurringType().equals("YEARLY") && cards.get(i).getDate().getDayOfYear() == tempTime.getDayOfYear()) {
                                        adapter.add(cards.get(i));
                                    }
                                } else {
                                    //DISPLAY if its a tomorrow task
                                    if (cards.get(i).isTomorrow()) {
                                        adapter.add(cards.get(i));
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Consider done tasks here
            for(int i = 0; i < cards.size(); i++) {
                if (cards.get(i).isDone() && (focusMode.equals("NONE") || cards.get(i).getCategory().equals(focusMode))) {
                    if ((tempTime.getDayOfYear() >= cards.get(i).getDate().getDayOfYear() || tempTime.getYear() > cards.get(i).getDate().getYear())) {
                        if (cards.get(i).isRecurring()) {
                            if (cards.get(i).getRecurringType().equals("WEEKLY") && cards.get(i).getDate().getDayOfWeek().toString().equals(tempTime.getDayOfWeek().toString())) {
                                adapter.add(cards.get(i));
                                //add all monthly recurring tasks recurring today
                            } else if (cards.get(i).getRecurringType().equals("MONTHLY") && cards.get(i).getDate().getDayOfMonth() == tempTime.getDayOfMonth()) {
                                adapter.add(cards.get(i));
                                //add all daily recurring tasks
                            } else if (cards.get(i).getRecurringType().equals("DAILY")) {
                                adapter.add(cards.get(i));
                            } else if (cards.get(i).getRecurringType().equals("YEARLY") && cards.get(i).getDate().getDayOfYear() == tempTime.getDayOfYear()) {
                                adapter.add(cards.get(i));
                            }
                        } else {
                            //DISPLAY if its a tomorrow task
                            if (cards.get(i).isTomorrow()) {
                                adapter.add(cards.get(i));
                            }
                        }
                    }
                }
            }
            adapter.notifyDataSetChanged();
            for(int i = 0; i < cards.size(); i++) {
                Log.d("Ordered cards changed", cards.get(i).sortOrder() + " " + i + " " + cards.get(i).getDescription());
            }

            if(activityModel.size() != 0){
                this.view.placeholderText.setVisibility(View.GONE);
            }

            if(activityModel.size() == 0){
                this.view.placeholderText.setVisibility(View.VISIBLE);
            }
        });
    }

}