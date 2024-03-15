package edu.ucsd.cse110.successorator.ui.itemList.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.ZonedDateTime;

import edu.ucsd.cse110.successorator.DateFormatter;
import edu.ucsd.cse110.successorator.MainViewModel;

import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentCardListBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogAddItemBinding;
import edu.ucsd.cse110.successorator.lib.domain.Item;
import edu.ucsd.cse110.successorator.lib.util.ItemBuilder;


public class CreateItemDialogFragment extends DialogFragment {
    private FragmentDialogAddItemBinding view;

    private MainViewModel activityModel;

    private DateFormatter dateFormatter;

    private SharedPreferences sharedPreferences;

    private int advanceCount;

    private ItemBuilder itemBuilder;

    CreateItemDialogFragment() {
        // Empty required constructor
    }

    public static CreateItemDialogFragment newInstance() {
        var fragment = new CreateItemDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        sharedPreferences = requireActivity().getApplicationContext().getSharedPreferences("formatted_date", Context.MODE_PRIVATE);
        advanceCount = sharedPreferences.getInt("advance_count", 0);

        // Consider the advanced days
        ZonedDateTime curr = ZonedDateTime.now().plusDays(advanceCount);
        dateFormatter = new DateFormatter(curr);

        this.view = FragmentDialogAddItemBinding.inflate(getLayoutInflater());
        view.NONE.setChecked(true);
        // Set the text for radio buttons
        view.WEEKLY.setText("Weekly on " + dateFormatter.weeklyDate(curr));
        view.MONTHLY.setText("Monthly " + dateFormatter.monthlyDate(curr));
        view.YEARLY.setText("Yearly on " + dateFormatter.yearlyDate(curr));

        view.HOME.setChecked(true);
        return new AlertDialog.Builder(getActivity())
                .setTitle("New Item")
                .setMessage("Please enter your MIT")
                .setView(view.getRoot())
                .setPositiveButton("Create", this::onPositiveButtonClick)
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
        sharedPreferences = requireActivity().getApplicationContext().getSharedPreferences("formatted_date", Context.MODE_PRIVATE);
        advanceCount = sharedPreferences.getInt("advance_count", 0);

        // Consider the advanced days
        ZonedDateTime curr = ZonedDateTime.now().plusDays(advanceCount);

        Item returnitem;
        boolean recurBool = false;
        String recurringChoice = "NONE";
        String categoryChoice = "HOME";

        if (view.DAILY.isChecked()){
            description += ", daily";
            recurringChoice = "DAILY";
            recurBool = true;
        } else if (view.WEEKLY.isChecked()){
            recurBool = true;
            description += ", weekly on " +  curr.getDayOfWeek().toString();
            recurringChoice = "WEEKLY";
        } else if (view.MONTHLY.isChecked()){
            recurBool = true;
            description += ", monthly on " +  dateFormatter.monthlyDate(curr);
            recurringChoice = "MONTHLY";
        } else if (view.YEARLY.isChecked()) {
            recurBool = true;
            description += ", yearly on " +  dateFormatter.yearlyDate(curr);
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

        returnitem = itemBuilder.addDescription(description).addDate(curr).addRecurring(recurBool)
                                .addRecurringType(recurringChoice).addCategory(categoryChoice).build();

        return returnitem;
    }

}
