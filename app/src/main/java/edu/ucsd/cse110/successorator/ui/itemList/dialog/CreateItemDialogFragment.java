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


public class CreateItemDialogFragment extends DialogFragment {
    private FragmentDialogAddItemBinding view;

    private MainViewModel activityModel;

    private DateFormatter dateFormatter;

    private SharedPreferences sharedPreferences;

    private int advanceCount;

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
        if (view.NONE.isChecked()){
            returnitem = new Item(description, null, -1, false,
                    curr, false, "NONE", false);
        } else if (view.DAILY.isChecked()){
            description += ", daily";
            returnitem = new Item(description, null, -1, false,
                    curr, true, "DAILY",false );
        } else if (view.WEEKLY.isChecked()){
            description += ", weekly on " +  ZonedDateTime.now().getDayOfWeek().toString();
            returnitem = new Item(description, null, -1, false,
                    curr, true, "WEEKLY",false);
        } else if (view.MONTHLY.isChecked()){
            description += ", monthly on " +  dateFormatter.monthlyDate(ZonedDateTime.now());
            returnitem = new Item(description, null, -1, false,
                    curr, true, "MONTHLY",false);
        } else {
            description += ", yearly on " +  dateFormatter.yearlyDate(ZonedDateTime.now());
            returnitem = new Item(description, null, -1, false,
                    curr, true, "YEARLY",false);
        }
        return returnitem;
    }

}
