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
import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.DateFormatter;
import edu.ucsd.cse110.successorator.databinding.FragmentRecurringListBinding;
import edu.ucsd.cse110.successorator.ui.itemList.dialog.ConfirmDeleteCardDialogFragment;
import edu.ucsd.cse110.successorator.ui.itemList.dialog.CreateRecurringItemDialogFragment;

public class RecurringListFragment extends ParentFragment {
    private FragmentRecurringListBinding view;
    private ItemListAdapter adapter;
    private TextView dateText;
    private String formattedDate;
    private DateFormatter dateFormatter;

    private SharedPreferences sharedPreferences;

    public RecurringListFragment() {
        // Required empty public constructor
    }

    public static RecurringListFragment newInstance() {
        RecurringListFragment fragment = new RecurringListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = requireActivity().getApplicationContext().getSharedPreferences("formatted_date", Context.MODE_PRIVATE);
        this.adapter = new ItemListAdapter(requireContext(), getParentFragmentManager(), List.of(), id ->{
            var dialogFragment = ConfirmDeleteCardDialogFragment.newInstance(id);
            dialogFragment.show(getParentFragmentManager(), "ConfirmDeleteCardDialogFragment");
        }, activityModel::append, activityModel::prepend, activityModel::markCompleteOrIncomplete, "RECURRING", sharedPreferences.getInt("advance_count", 0));
        /*activityModel.getOrderedCards().observe(cards -> {
            if(cards == null) return;
            adapter.clear();

            for(int i = 0; i < cards.size(); i++){
                if(cards.get(i).isRecurring()){
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
        });*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = FragmentRecurringListBinding.inflate(inflater, container, false);
        view.cardList.setAdapter(adapter);
        view.addItem.setOnClickListener(v ->{
            var dialogFragment = CreateRecurringItemDialogFragment.newInstance();
            // Unsure if we should use getSupportFragmentManager() or getParentFragmentManager()
            dialogFragment.show(getParentFragmentManager(),"CreateRecurringItemDialogFragment");
        });

        return view.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){

    }
    public void onResume() {
        super.onResume();
        activityModel.getOrderedCards().observe(cards -> {
            if(cards == null) return;
            adapter.clear();
            String[] arrayOfCategories = {"HOME","WORK","SCHOOL","ERRAND"};
            for(int j = 0; j < arrayOfCategories.length; j++) {  // Go through all category tags
                for (int i = 0; i < cards.size(); i++) {
                    if (cards.get(i).getCategory().equals(arrayOfCategories[j])) {
                        if (cards.get(i).isRecurring()) {
                            adapter.add(cards.get(i));
                        }
                    }
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

}
