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

        this.adapter = new ItemListAdapter(requireContext(), getParentFragmentManager(), List.of(), activityModel::remove, activityModel::append, activityModel::prepend, activityModel::markCompleteOrIncomplete, "HOME");


        activityModel.getOrderedCards().observe(cards -> {
            if(cards == null) return;
            adapter.clear();

            for(int i = 0; i < cards.size(); i++){
                if(cards.get(i).getDate().getDayOfMonth() == ZonedDateTime.now().plusDays(1).getDayOfMonth()){
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

        dateFormatter = new DateFormatter(ZonedDateTime.now());



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = FragmentTomorrowListBinding.inflate(inflater, container, false);
        view.cardList.setAdapter(adapter);
        dateText = this.view.dateView;




        // Persistence of Date
        sharedPreferences = requireActivity().getSharedPreferences("formatted_date", Context.MODE_PRIVATE);

        //change the title to tomorrow
        formattedDate = dateFormatter.getTomorrowsDate(ZonedDateTime.now());

        // Save formatted date for persistence
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("formatted_date", formattedDate);
        editor.apply();

        // Update UI with formatted date
        dateText.setText(formattedDate);


        view.addItem.setOnClickListener(v ->{
            var dialogFragment = CreateTomorrowItemDialogFragment.newInstance();
            // Unsure if we should use getSupportFragmentManager() or getParentFragmentManager()
            dialogFragment.show(getParentFragmentManager(),"CreateTomorrowItemDialogFragment");
        });

        // When pressing the add date button, the Date will advance by 24hrs
        view.addDay.setOnClickListener(v -> {

            // Add day and format it
            formattedDate = dateFormatter.addDay(ZonedDateTime.now());

            // Save formatted date for persistence
            SharedPreferences.Editor editor1 = sharedPreferences.edit();
            editor1.putString("formatted_date", formattedDate);
            editor1.apply();

            // Update UI with formatted date
            dateText.setText(formattedDate);
            activityModel.removeAllComplete();
        });

        return view.getRoot();
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        activityModel.getOrderedCards().observe(cards -> {
            if(cards == null) return;
            adapter.clear();
            for(int i = 0; i < cards.size(); i++){
                if(cards.get(i).getDate().getDayOfMonth() == ZonedDateTime.now().plusDays(1).getDayOfMonth()){
                    adapter.add(cards.get(i));
                }
            }
            //adapter.addAll(new ArrayList<>(cards));
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
    public void onResume() {
        super.onResume();
        // Get formatted date and display.
        String savedDate = sharedPreferences.getString("formatted_date", "ERR");

        // Check for date changes
        if (!(dateFormatter.getTodaysDate(ZonedDateTime.now()).equals(savedDate))) {
            activityModel.removeAllComplete();
            formattedDate = dateFormatter.getTomorrowsDate(ZonedDateTime.now());

            // Edit date persistence
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("formatted_date", formattedDate);
            editor.apply();
        }

        dateText.setText(sharedPreferences.getString("formatted_date", "ERR"));
    }
}