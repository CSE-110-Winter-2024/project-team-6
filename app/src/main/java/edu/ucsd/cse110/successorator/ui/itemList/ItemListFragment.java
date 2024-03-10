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

import edu.ucsd.cse110.successorator.lib.domain.Item;
import edu.ucsd.cse110.successorator.ui.itemList.dialog.CreateItemDialogFragment;
import edu.ucsd.cse110.successorator.databinding.FragmentCardListBinding;

public class ItemListFragment extends ParentFragment {
    //private MainViewModel activityModel;
    private FragmentCardListBinding view;
    private ItemListAdapter adapter;
    private TextView dateText;

    private boolean dateChanged = false;

    private String formattedDate;
    private DateFormatter dateFormatter;

    // Track days to artificially advance
    private int advanceCount;

    private boolean firstRun = true;

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
        dateChanged = false;
        this.adapter = new ItemListAdapter(requireContext(), getParentFragmentManager(), List.of(), activityModel::remove, activityModel::append, activityModel::prepend, activityModel::markCompleteOrIncomplete, "HOME");

        // Persistence of Date
        sharedPreferences = requireActivity().getApplicationContext().getSharedPreferences("formatted_date", Context.MODE_PRIVATE);
        advanceCount = sharedPreferences.getInt("advance_count", 0);
        dateFormatter = new DateFormatter(ZonedDateTime.now());
        String savedDate = sharedPreferences.getString("formatted_date_today", "ERR");

        // Apply the number of advanced days to the current date
        String currDate = dateFormatter.getPersistentDate(ZonedDateTime.now().plusDays(advanceCount));

        //
        // Check for date changes
        activityModel.getOrderedCards().observe(cards -> {
            if(cards != null && firstRun && !(currDate.equals(savedDate))) {
                finishRecurringTasks();
                firstRun = false;
            }
        });
        updateFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //super.onCreateView(inflater, container, savedInstanceState);

        this.view = FragmentCardListBinding.inflate(inflater, container, false);
        // Set the adapter on the ListView
        view.cardList.setAdapter(adapter);
        dateText = this.view.dateView;
        dateChanged = false;
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
            dateChanged = true;
            // Save formatted date for persistence
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("formatted_date_today", formattedDate);
            editor.putInt("advance_count", advanceCount);
            editor.apply();

            // Update UI with formatted date
            dateText.setText(dateFormatter.getTodaysDate(ZonedDateTime.now().plusDays(advanceCount)));
            activityModel.removeAllComplete();
            updateTomorrow();
            finishRecurringTasks();
            updateFragment();
            dateChanged = false;
        });

        return view.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){

    }
    @Override
    public void onResume() {
        super.onResume();
        dateChanged = false;
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
            dateChanged = true;
            finishRecurringTasks();
            updateTomorrow();
            updateFragment();
            dateChanged = false;


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
    public void finishRecurringTasks(){
        List<Item> cards = activityModel.getOrderedCards().getValue();
            if (cards == null) {
                return;
            }
            ZonedDateTime tempTime = ZonedDateTime.now().plusDays(advanceCount);
            for(int i = 0; i < cards.size(); i++){
                if((tempTime.getDayOfYear() >= cards.get(i).getDate().getDayOfYear()  || tempTime.getYear() > cards.get(i).getDate().getYear())) {
                    if (cards.get(i).getRecurringType().equals("WEEKLY") && cards.get(i).getDate().getDayOfWeek().toString().equals(tempTime.getDayOfWeek().toString())) {
                        if (cards.get(i).isDone()) {
                            cards.get(i).markDone();
                            activityModel.remove(cards.get(i).id());
                            activityModel.append(cards.get(i));
                        }
                        //add all monthly recurring tasks recurring today
                    } else if (cards.get(i).getRecurringType().equals("MONTHLY") && cards.get(i).getDate().getDayOfMonth() == tempTime.getDayOfMonth()) {
                        if (cards.get(i).isDone()) {
                            cards.get(i).markDone();
                            activityModel.remove(cards.get(i).id());
                            activityModel.append(cards.get(i));
                        }
                        //add all daily recurring tasks
                    } else if (cards.get(i).getRecurringType().equals("DAILY")) {
                        if (cards.get(i).isDone()) {
                            cards.get(i).markDone();
                            activityModel.remove(cards.get(i).id());
                            activityModel.append(cards.get(i));
                        }
                    } else if (cards.get(i).getRecurringType().equals("YEARLY") && cards.get(i).getDate().getDayOfYear() == tempTime.getDayOfYear()) {
                        if (cards.get(i).isDone()) {
                            cards.get(i).markDone();
                            activityModel.remove(cards.get(i).id());
                            activityModel.append(cards.get(i));
                        }
                    }
                }
            }
    }

    public void updateTomorrow(){
        List<Item> cards = activityModel.getOrderedCards().getValue();
        if (cards == null) return;
        for(int i = 0; i < cards.size(); i++){
            if (cards.get(i).isTomorrow()){
                cards.get(i).markTomorrow();
                activityModel.remove(cards.get(i).id());
                activityModel.append(cards.get(i));
            }
        }
    }

    public void updateFragment() {
        activityModel.getOrderedCards().observe(cards -> {
            if(cards == null) return;
            adapter.clear();
            ZonedDateTime tempTime = ZonedDateTime.now().plusDays(advanceCount);
            for(int i = 0; i < cards.size(); i++){
                if(!cards.get(i).isPending()) {
                    if((tempTime.getDayOfYear() >= cards.get(i).getDate().getDayOfYear()  || tempTime.getYear() > cards.get(i).getDate().getYear())) {
                        if (cards.get(i).isRecurring()) {
                            // If the card is recurring then we want to display it if its not already finished and its past or equal to start date
                            if (!cards.get(i).isDone()) {
                                adapter.add(cards.get(i));
                            } else {
                                //We also want to display it if the recurring date comes again
                                //If it is finished already we want to unfinish it and display it, otherwise just display
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
                                } else if (cards.get(i).isDone() && cards.get(i).isRecurring() && !dateChanged) {
                                    adapter.add(cards.get(i));
                                }

                                //If the task is done but we haven't moved to next day yet we still want to display it
                            }
                        } else {
                            //If the card isn't recurring we want to display it since we already deleted all complete one-time tasks
                            //BUT if its occurring tomorrow we don't want to display it
                            if (!cards.get(i).isTomorrow()) {
                                adapter.add(cards.get(i));
                            }

                        }
                    }
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
