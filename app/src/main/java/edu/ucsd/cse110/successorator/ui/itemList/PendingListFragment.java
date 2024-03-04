package edu.ucsd.cse110.successorator.ui.itemList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.ZonedDateTime;

import edu.ucsd.cse110.successorator.DateFormatter;
import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentCardListBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentPendingListBinding;
import edu.ucsd.cse110.successorator.ui.itemList.dialog.CreateItemDialogFragment;
import edu.ucsd.cse110.successorator.ui.itemList.dialog.CreatePendingItemDialogFragment;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PendingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PendingListFragment extends ParentFragment {
    // private MainViewModel activityModel;
    private FragmentPendingListBinding view;
    private ItemListAdapter adapter;
    private TextView dateText;
    private String formattedDate;
    private DateFormatter dateFormatter;

    private SharedPreferences sharedPreferences;

    public PendingListFragment() {
        // Required empty public constructor
    }

    public static PendingListFragment newInstance() {
        PendingListFragment fragment = new PendingListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        // Initialize the Model
//        var modelOwner = requireActivity();
//        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
//        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
//        this.activityModel = modelProvider.get(MainViewModel.class);

        dateFormatter = new DateFormatter(ZonedDateTime.now());



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = FragmentPendingListBinding.inflate(inflater, container, false);
        dateText = this.view.dateView;




        // Persistence of Date
        sharedPreferences = requireActivity().getSharedPreferences("formatted_date", Context.MODE_PRIVATE);

        //change the title to tomorrow
        formattedDate = "Pending";

        // Save formatted date for persistence
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("formatted_date", formattedDate);
        editor.apply();

        // Update UI with formatted date
        dateText.setText(formattedDate);


        view.addItem.setOnClickListener(v ->{
            var dialogFragment = CreatePendingItemDialogFragment.newInstance();
            // Unsure if we should use getSupportFragmentManager() or getParentFragmentManager()
            dialogFragment.show(getParentFragmentManager(),"CreatePendingItemDialogFragment");
        });

        // When pressing the add date button, the Date will advance by 24hrs
        view.addDay.setOnClickListener(v -> {

            // Add day and format it
            formattedDate = dateFormatter.addDay(ZonedDateTime.now());

            // Save formatted date for persistence
            SharedPreferences.Editor editor1 = sharedPreferences.edit();
            editor1.putString("formatted_date", formattedDate);
            editor1.apply();

            // Update UI with formatted date
            dateText.setText(formattedDate);
            activityModel.removeAllComplete();
        });

        return view.getRoot();
    }
    public void onResume() {
        super.onResume();
        dateText.setText(sharedPreferences.getString("formatted_date", "ERR"));
    }
}