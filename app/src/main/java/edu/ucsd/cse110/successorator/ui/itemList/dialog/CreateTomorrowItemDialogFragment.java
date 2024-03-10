package edu.ucsd.cse110.successorator.ui.itemList.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.time.ZonedDateTime;
import edu.ucsd.cse110.successorator.DateFormatter;
import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogAddItemBinding;
import edu.ucsd.cse110.successorator.lib.domain.Item;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateTomorrowItemDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateTomorrowItemDialogFragment extends DialogFragment {
    private FragmentDialogAddItemBinding view;
    private MainViewModel activityModel;

    private DateFormatter dateFormatter;

    private SharedPreferences sharedPreferences;

    private int advanceCount;
    CreateTomorrowItemDialogFragment() {
        // Empty required constructor
    }
    public static CreateTomorrowItemDialogFragment newInstance() {
        var fragment = new CreateTomorrowItemDialogFragment();
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
        ZonedDateTime curr = ZonedDateTime.now().plusDays(advanceCount + 1);
        dateFormatter = new DateFormatter(curr);

        this.view = FragmentDialogAddItemBinding.inflate(getLayoutInflater());
        view.NONE.setChecked(true);
        // Set the text for radio buttons
        view.WEEKLY.setText("Weekly on " + dateFormatter.weeklyDate(curr));
        view.MONTHLY.setText("Monthly " + dateFormatter.monthlyDate(curr));
        view.YEARLY.setText("Yearly on " + dateFormatter.yearlyDate(curr));

        view.HOUSE.setChecked(true);
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
        ZonedDateTime curr = ZonedDateTime.now().plusDays(advanceCount + 1);

        Item returnitem;
        if (view.NONE.isChecked()){
            returnitem = new Item(description, null, -1, false,
                    curr, false, "NONE", false, true);
        } else if (view.DAILY.isChecked()){
            description += ", daily";
            returnitem = new Item(description, null, -1, false,
                    curr, true, "DAILY",false , true);
        } else if (view.WEEKLY.isChecked()){
            description += ", weekly on " +  ZonedDateTime.now().getDayOfWeek().toString();
            returnitem = new Item(description, null, -1, false,
                    curr, true, "WEEKLY",false, true);
        } else if (view.MONTHLY.isChecked()){
            description += ", monthly on " +  dateFormatter.monthlyDate(ZonedDateTime.now());
            returnitem = new Item(description, null, -1, false,
                    curr, true, "MONTHLY",false, true);
        } else {
            description += ", yearly on " +  dateFormatter.yearlyDate(ZonedDateTime.now());
            returnitem = new Item(description, null, -1, false,
                    curr, true, "YEARLY",false, true);
        }
        return returnitem;
    }
}