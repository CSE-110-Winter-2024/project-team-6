package edu.ucsd.cse110.successorator.ui.itemList.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.ZonedDateTime;

import edu.ucsd.cse110.successorator.MainViewModel;

import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentCardListBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogAddItemBinding;
import edu.ucsd.cse110.successorator.lib.domain.Item;


public class CreateItemDialogFragment extends DialogFragment {
    private FragmentDialogAddItemBinding view;

    private MainViewModel activityModel;

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
        this.view = FragmentDialogAddItemBinding.inflate(getLayoutInflater());
        view.NONE.setChecked(true);
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
        Item returnitem;
        if (view.NONE.isChecked()){
            returnitem = new Item(description, null, -1, false,
                    ZonedDateTime.now(), false, "NONE");
        } else if (view.DAILY.isChecked()){
            returnitem = new Item(description, null, -1, false,
                    ZonedDateTime.now(), true, "DAILY");
        } else if (view.WEEKLY.isChecked()){
            returnitem = new Item(description, null, -1, false,
                    ZonedDateTime.now(), true, "WEEKLY");
        } else if (view.MONTHLY.isChecked()){
            returnitem = new Item(description, null, -1, false,
                    ZonedDateTime.now(), true, "MONTHLY");
        } else {
            returnitem = new Item(description, null, -1, false,
                    ZonedDateTime.now(), true, "YEARLY");
        }
        return returnitem;
    }

}
