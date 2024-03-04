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
        var item = new Item(description, null, -1, false,
                ZonedDateTime.now(), false, "NONE");

        if(view.dailyBtn.isChecked()){
            item = new Item(description, null, -1, false,
                    startDate, true, "DAILY");
        }else if(view.weeklyBtn.isChecked()){
            item = new Item(description, null, -1, false,
                    startDate, true, "WEEKLY");
        }else if(view.monthlyBtn.isChecked()){
            item = new Item(description, null, -1, false,
                    startDate, true, "MONTHLY");
        }else if(view.yearlyBtn.isChecked()){
            item = new Item(description, null, -1, false,
                    startDate, true, "YEARLY");
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
