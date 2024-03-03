package edu.ucsd.cse110.successorator.ui.itemList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.ZonedDateTime;

import edu.ucsd.cse110.successorator.DateFormatter;
import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentCardListBinding;
import edu.ucsd.cse110.successorator.ui.itemList.dialog.CreateItemDialogFragment;
import edu.ucsd.cse110.successorator.ui.itemList.dialog.Dropdown;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TomorrowListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TomorrowListFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentCardListBinding view;
    private ItemListAdapter adapter;
    private TextView dateText;
    private String formattedDate;
    private DateFormatter dateFormatter;

    private SharedPreferences sharedPreferences;

    public TomorrowListFragment() {
        // Required empty public constructor
    }

    public static TomorrowListFragment newInstance() {
        TomorrowListFragment fragment = new TomorrowListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the Model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        dateFormatter = new DateFormatter(ZonedDateTime.now());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = FragmentCardListBinding.inflate(inflater, container, false);
        dateText = this.view.dateView;
        formattedDate = dateFormatter.getTomorrowsDate(ZonedDateTime.now());

        // Persistence of Date
        sharedPreferences = requireActivity().getSharedPreferences("formatted_date", Context.MODE_PRIVATE);
        view.addItem.setOnClickListener(v ->{
            var dialogFragment = CreateItemDialogFragment.newInstance();
            // Unsure if we should use getSupportFragmentManager() or getParentFragmentManager()
            dialogFragment.show(getParentFragmentManager(),"CreateItemDialogFragment");
        });

        // When pressing the add date button, the Date will advance by 24hrs
        view.addDay.setOnClickListener(v -> {

            // Add day and format it
            formattedDate = dateFormatter.addDay(ZonedDateTime.now());

            // Save formatted date for persistence
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("formatted_date", formattedDate);
            editor.apply();

            // Update UI with formatted date
            dateText.setText(formattedDate);
            activityModel.removeAllComplete();
        });
        view.dropdownMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dropdown dropdown = new Dropdown();
                dropdown.show(getChildFragmentManager(), "DropdownFragment");
            }
        });

        return view.getRoot();
    }
    public void onResume() {
        super.onResume();
        // Get formatted date and display.
        String savedDate = sharedPreferences.getString("formatted_date", "ERR");

        // Check for date changes
//        if (!(dateFormatter.getTodaysDate(ZonedDateTime.now()).equals(savedDate))) {
//            activityModel.removeAllComplete();
//            formattedDate = dateFormatter.getTodaysDate(ZonedDateTime.now());
//
//            // Edit date persistence
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("formatted_date", formattedDate);
//            editor.apply();
//        }

        // Set date text from last saved date
        dateText.setText(sharedPreferences.getString("formatted_date", "ERR"));
    }
}