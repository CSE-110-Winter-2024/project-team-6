package edu.ucsd.cse110.successorator.ui.itemList.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.ZonedDateTime;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.MovingPendingItemsBinding;
import edu.ucsd.cse110.successorator.lib.domain.Item;

public class MovePendingItemsDialogFragment extends DialogFragment {
    private MovingPendingItemsBinding view;
    private MainViewModel activityModel;
    static Item item;

    MovePendingItemsDialogFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstancState){
        super.onCreate(savedInstancState);


        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }
    public static MovePendingItemsDialogFragment newInstance(Item item){
        var fragment = new MovePendingItemsDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("item", item);
        fragment.setArguments(args);
        return fragment;
    }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        this.view = MovingPendingItemsBinding.inflate(getLayoutInflater());
        this.view.moveToToday.setChecked(true);

        Bundle args = getArguments();
        if(args != null && args.containsKey("item")){
            item = (Item) args.getSerializable("item");
        }

        return new AlertDialog.Builder(getActivity())
                .setView(view.getRoot())
                .setPositiveButton("Confirm", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }
    public void onPositiveButtonClick(DialogInterface dialog, int which){

        if(view.moveToToday.isChecked()){
            item.setDate(ZonedDateTime.now());
            item.markPending();
            activityModel.remove(item.id());
            activityModel.append(item);
        } else if (view.moveToTomorrow.isChecked()) {
            item.setDate(ZonedDateTime.now().plusDays(1));
            item.markPending();
            activityModel.remove(item.id());
            activityModel.append(item);

        }else if (view.moveToFinish.isChecked()){
            item.setDate(ZonedDateTime.now());
            item.markDone();
            item.markPending();
            activityModel.remove(item.id());
            activityModel.append(item);

        }else if (view.moveToDelete.isChecked()){
            activityModel.remove(item.id());
        }

        dialog.dismiss();
    }
    public void onNegativeButtonClick(DialogInterface dialog, int which){dialog.cancel();}


}
