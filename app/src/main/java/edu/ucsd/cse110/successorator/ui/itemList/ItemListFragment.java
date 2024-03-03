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
import edu.ucsd.cse110.successorator.MainViewModel;

import edu.ucsd.cse110.successorator.ui.itemList.dialog.CreateItemDialogFragment;
import edu.ucsd.cse110.successorator.databinding.FragmentCardListBinding;
import edu.ucsd.cse110.successorator.ui.itemList.dialog.CreateRecurringItemDialogFragment;

public class ItemListFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentCardListBinding view;
    private ItemListAdapter adapter;
    private TextView dateText;
    private String formattedDate;
    private DateFormatter dateFormatter;

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

        // Initialize the Model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);


        // Initialize the Adapter (with an empty list for now)


        this.adapter = new ItemListAdapter(requireContext(), List.of(), activityModel::remove, activityModel::append, activityModel::prepend, activityModel::markCompleteOrIncomplete);

        
        activityModel.getOrderedCards().observe(cards -> {
            if(cards == null) return;
            adapter.clear();

            adapter.addAll(new ArrayList<>(cards));
            adapter.notifyDataSetChanged();
            for(int i = 0; i < cards.size(); i++) {
                Log.d("Ordered cards changed", cards.get(i).sortOrder() + " " + i + " " + cards.get(i).getDescription());
            }

            if(activityModel.size() != 0){
                view.placeholderText.setVisibility(View.GONE);
            }
            if(activityModel.size() == 0){
                view.placeholderText.setVisibility(View.VISIBLE);
            }
        });

        dateFormatter = new DateFormatter(ZonedDateTime.now());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            this.view = FragmentCardListBinding.inflate(inflater, container, false);
            // Set the adapter on the ListView
            view.cardList.setAdapter(adapter);
            dateText = this.view.dateView;

            // Persistence of Date
            sharedPreferences = requireActivity().getSharedPreferences("formatted_date", Context.MODE_PRIVATE);

            view.addItem.setOnClickListener(v ->{
                var dialogFragment = CreateRecurringItemDialogFragment.newInstance();
                // Unsure if we should use getSupportFragmentManager() or getParentFragmentManager()
                dialogFragment.show(getParentFragmentManager(),"CreateItemDialogFragment");
            });

            // When pressing the add date button, the Date will advance by 24hrs
            view.addDay.setOnClickListener(v -> {

                // Add day and format it
                formattedDate = dateFormatter.addDay(ZonedDateTime.now());

                // Save formatted date for persistence
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("formatted_date", formattedDate);
                editor.apply();

                // Update UI with formatted date
                dateText.setText(formattedDate);
                activityModel.removeAllComplete();
            });

            return view.getRoot();
    }
    @Override
    public void onResume() {
        super.onResume();

        // Get formatted date and display.
        String savedDate = sharedPreferences.getString("formatted_date", "ERR");

        // Check for date changes
        if (!(dateFormatter.getTodaysDate(ZonedDateTime.now()).equals(savedDate))) {
            activityModel.removeAllComplete();
            formattedDate = dateFormatter.getTodaysDate(ZonedDateTime.now());

            // Edit date persistence
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("formatted_date", formattedDate);
            editor.apply();
        }

        // Set date text from last saved date
        dateText.setText(sharedPreferences.getString("formatted_date", "ERR"));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        activityModel.getOrderedCards().observe(cards -> {
            if(cards == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(cards));
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
