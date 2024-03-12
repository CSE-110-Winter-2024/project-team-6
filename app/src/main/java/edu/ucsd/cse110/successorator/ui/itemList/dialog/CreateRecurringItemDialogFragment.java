package edu.ucsd.cse110.successorator.ui.itemList.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import edu.ucsd.cse110.successorator.DateFormatter;
import edu.ucsd.cse110.successorator.MainViewModel;

import edu.ucsd.cse110.successorator.databinding.FragmentCardListBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogAddItemBinding;
import edu.ucsd.cse110.successorator.databinding.RecurringViewAddItemBinding;
import edu.ucsd.cse110.successorator.lib.domain.Item;
import edu.ucsd.cse110.successorator.lib.util.ItemBuilder;


public class CreateRecurringItemDialogFragment extends DialogFragment {
    private RecurringViewAddItemBinding view;

    private MainViewModel activityModel;

    private ItemBuilder itemBuilder;
    CreateRecurringItemDialogFragment() {
        // Empty required constructor
    }

    public static CreateRecurringItemDialogFragment newInstance() {
        var fragment = new CreateRecurringItemDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = RecurringViewAddItemBinding.inflate(getLayoutInflater());
        this.view.weeklyBtn.setChecked(true);
        view.HOME.setChecked(true);
        return new AlertDialog.Builder(getActivity())
                .setTitle("New Item")
                .setMessage("Please enter your MIT")
                .setView(view.getRoot())
                .setPositiveButton("Save", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        var description = view.editTextDialog.getText().toString();
        var item = makeRecurrenceItem(description);
        activityModel.append(item);
        dialog.dismiss();
    }

    private void onNegativeButtonClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        itemBuilder = new ItemBuilder();

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

    private Item makeRecurrenceItem(String description){

        ZonedDateTime startDate = ZonedDateTime.of(Integer.valueOf(view.yearInput.getText().toString()), Integer.valueOf(view.monthInput.getText().toString()), Integer.valueOf(view.dayInput.getText().toString()), 0, 0, 0, 0, ZoneId.of("UTC"));
        DateFormatter dateFormatter = new DateFormatter(startDate);

        Item returnitem;
        String recurringChoice = "NONE";
        String categoryChoice = "HOME";

        if (view.dailyBtn.isChecked()){
            description += ", daily";
            recurringChoice = "DAILY";
        } else if (view.weeklyBtn.isChecked()){
            description += ", weekly on " +  startDate.getDayOfWeek().toString();
            recurringChoice = "WEEKLY";
        } else if (view.monthlyBtn.isChecked()){
            description += ", monthly on " +  dateFormatter.monthlyDate(startDate);
            recurringChoice = "MONTHLY";
        } else if (view.yearlyBtn.isChecked()) {
            description += ", yearly on " +  dateFormatter.yearlyDate(startDate);
            recurringChoice = "YEARLY";
        }

        if (view.HOME.isChecked()) {
            categoryChoice = "HOME";
        } else if (view.ERRANDS.isChecked()) {
            categoryChoice = "ERRAND";
        } else if (view.SCHOOL.isChecked()) {
            categoryChoice = "SCHOOL";
        } else {
            categoryChoice = "WORK";
        }

        returnitem = itemBuilder.addDescription(description).addDate(startDate).addRecurring(true)
                .addRecurringType(recurringChoice).addCategory(categoryChoice).build();

        return returnitem;
    }
}


