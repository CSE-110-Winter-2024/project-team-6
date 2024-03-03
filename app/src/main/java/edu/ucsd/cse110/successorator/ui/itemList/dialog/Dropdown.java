package edu.ucsd.cse110.successorator.ui.itemList.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.ui.itemList.ItemListFragment;
import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.ui.itemList.TomorrowListFragment;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class Dropdown extends DialogFragment {
    private boolean isShowingToday = true;
    private boolean isShowingTomorrow = false;
    private boolean isShowingPending = false;
    private boolean isShowingRecurring = false;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //FragmentSwitcher fragmentSwitcher = new FragmentSwitcher(getChildFragmentManager(), R.id.fragment_container);

        final String[] differentViews = {"Today", "Tomorrow", "Pending", "Recurring"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                differentViews);

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int index) {
                String options = differentViews[index];
                //need to handle the different options here
                switch (options){
                    case "Today":
                        if(!isShowingToday) {
                            requireActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container, ItemListFragment.newInstance())
                                    .commit();
                            isShowingTomorrow = false;
                            isShowingToday = true;
                            isShowingPending = false;
                            isShowingRecurring = false;
                        }
                        break;
                    case "Tomorrow":
                        if(!isShowingTomorrow) {
                            requireActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container, TomorrowListFragment.newInstance())
                                    .commit();
                            isShowingTomorrow = true;
                            isShowingToday = false;
                            isShowingPending = false;
                            isShowingRecurring = false;
                        }
                        break;


                }

            }
        });
        return builder.create();
    }
}
