package edu.ucsd.cse110.successorator.ui.itemList.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.ZonedDateTime;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.PendingViewAddItemBinding;
import edu.ucsd.cse110.successorator.lib.domain.Item;
import edu.ucsd.cse110.successorator.lib.util.ItemBuilder;

public class CreatePendingItemDialogFragment extends DialogFragment {
    private PendingViewAddItemBinding view;

    // Create a ItemBuilder
    private ItemBuilder itemBuilder;

    private MainViewModel activityModel;

    CreatePendingItemDialogFragment(){
        // Empty required constructor
    }

    public static CreatePendingItemDialogFragment newInstance(){
        var fragment = new CreatePendingItemDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        this.view = PendingViewAddItemBinding.inflate(getLayoutInflater());
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

        String categoryChoice = "HOME";

        if (view.HOME.isChecked()) {
            categoryChoice = "HOME";
        } else if (view.ERRANDS.isChecked()) {
            categoryChoice = "ERRAND";
        } else if (view.SCHOOL.isChecked()) {
            categoryChoice = "SCHOOL";
        } else {
            categoryChoice = "WORK";
        }

        var item = itemBuilder.addDescription(description)
                                .addDate(ZonedDateTime.now().minusDays(1))
                                .addCategory(categoryChoice)
                                .addPending(true)
                                .build();

        activityModel.append(item);
        dialog.dismiss();
    }

    private void onNegativeButtonClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // Instantiate an ItemBuilder
        this.itemBuilder = new ItemBuilder();

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }
}