package edu.ucsd.cse110.successorator.ui.itemList.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class Dropdown extends DialogFragment {
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

                }

            }
        });
        return builder.create();
    }
}
