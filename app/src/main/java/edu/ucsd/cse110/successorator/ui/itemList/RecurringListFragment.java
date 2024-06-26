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
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.DateFormatter;
import edu.ucsd.cse110.successorator.R;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = FragmentRecurringListBinding.inflate(inflater, container, false);
        view.cardList.setAdapter(adapter);
        sharedPreferences = requireActivity().getSharedPreferences("formatted_date", Context.MODE_PRIVATE);
        view.addItem.setOnClickListener(v ->{
            var dialogFragment = CreateRecurringItemDialogFragment.newInstance();
            // Unsure if we should use getSupportFragmentManager() or getParentFragmentManager()
            dialogFragment.show(getParentFragmentManager(),"CreateRecurringItemDialogFragment");
        });
        setFocusModeIndicator();
        return view.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){

    }
    public void onResume() {
        super.onResume();

        String focusMode = sharedPreferences.getString("focus_mode", "WORK");

        activityModel.getOrderedCards().observe(cards -> {
            if(cards == null) return;
            adapter.clear();
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

    public void setFocusModeIndicator() {

        String focusMode = sharedPreferences.getString("focus_mode", "NONE");

        switch (focusMode) {
            case "HOME":
                view.focusIndicator.setBackground(ContextCompat.getDrawable(requireActivity().getApplicationContext(), R.drawable.outline_home));
                view.focusIndicator.setText("Focus: Home");
                break;
            case "WORK":
                view.focusIndicator.setBackground(ContextCompat.getDrawable(requireActivity().getApplicationContext(), R.drawable.outline_work));
                view.focusIndicator.setText("Focus: Work");
                break;
            case "SCHOOL":
                view.focusIndicator.setBackground(ContextCompat.getDrawable(requireActivity().getApplicationContext(), R.drawable.outline_school));
                view.focusIndicator.setText("Focus: School");
                break;
            case "ERRAND":
                view.focusIndicator.setBackground(ContextCompat.getDrawable(requireActivity().getApplicationContext(), R.drawable.outline_errands));
                view.focusIndicator.setText("Focus: Errands");
                break;
            default:
                this.view.focusIndicator.setBackground(ContextCompat.getDrawable(requireActivity().getApplicationContext(), R.drawable.outline_done));
                this.view.focusIndicator.setText("Focus: None");
                break;
        }
    }

}
