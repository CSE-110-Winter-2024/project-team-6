package edu.ucsd.cse110.successorator.ui.itemList.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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


public class CreateRecurringItemDialogFragment extends DialogFragment {
    private RecurringViewAddItemBinding view;

    private MainViewModel activityModel;

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

        ZonedDateTime startDate = ZonedDateTime.of(Integer.valueOf(view.yearInput.getText().toString()), Integer.valueOf(view.monthInput.getText().toString()), Integer.valueOf(view.dayInput.getText().toString()), 0, 0, 0, 0, ZoneId.of("UTC"));
        DateFormatter dateFormatter = new DateFormatter(startDate);
        var item = new Item(description, null, -1, false,
                ZonedDateTime.now(), false, "NONE", false);

        if(view.dailyBtn.isChecked()){
            description += ", daily";
            item = new Item(description, null, -1, false,
                    startDate, true, "DAILY", false);
        }else if(view.weeklyBtn.isChecked()){
            description += ", weekly on " +  startDate.getDayOfWeek().toString();
            item = new Item(description, null, -1, false,
                    startDate, true, "WEEKLY", false);
        }else if(view.monthlyBtn.isChecked()){
            description += ", monthly on " +  dateFormatter.monthlyDate(startDate);
            item = new Item(description, null, -1, false,
                    startDate, true, "MONTHLY",false );
        }else if(view.yearlyBtn.isChecked()){
            description += ", yearly on " +  dateFormatter.yearlyDate(startDate);
            item = new Item(description, null, -1, false,
                    startDate, true, "YEARLY",false);
        }
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
}
