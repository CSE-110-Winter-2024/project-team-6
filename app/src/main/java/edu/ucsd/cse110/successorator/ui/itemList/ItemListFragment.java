package edu.ucsd.cse110.successorator.ui.itemList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.DateFormatter;
import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.ui.itemList.dialog.CreateItemDialogFragment;
import edu.ucsd.cse110.successorator.databinding.FragmentCardListBinding;

public class ItemListFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentCardListBinding view;
    private ItemListAdapter adapter;

    private TextView dateText;
    private String formattedDate;



    public ItemListFragment() {
        // Required empty public constructor
    }

    public static ItemListFragment newInstance() {
        ItemListFragment fragment = new ItemListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the Model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);


        // Initialize the Adapter (with an empty list for now)

        this.adapter = new ItemListAdapter(requireContext(), List.of(), id ->{
            //var dialogFragment = ConfirmDeleteCardDialogFragment.newInstance(id);
            //dialogFragment.show(getParentFragmentManager(), "ConfirmDeleteCardDialogFragment");
        });

        activityModel.getOrderedCards().observe(cards -> {
            if(cards == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(cards));
            adapter.notifyDataSetChanged();
            if(activityModel.size() != 0){
                view.placeholderText.setVisibility(View.GONE);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            this.view = FragmentCardListBinding.inflate(inflater, container, false);
            // Set the adapter on the ListView
            view.cardList.setAdapter(adapter);

            // Initialize Date Formatting
            dateText = this.view.dateView;

            view.addItem.setOnClickListener(v ->{
                var dialogFragment = CreateItemDialogFragment.newInstance();
                // Unsure if we should use getSupportFragmentManager() or getParentFragmentManager()
                dialogFragment.show(getParentFragmentManager(),"CreateItemDialogFragment");
            });

            // When pressing the add date button, the Date will advance by 24hrs
            view.addDay.setOnClickListener(v -> {
                ZonedDateTime clock = ZonedDateTime.now();
                DateFormatter dateTracker = new DateFormatter(clock);

                formattedDate = dateTracker.addDay();

                dateText.setText(formattedDate);
            });

            return view.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Get formatted date and display.
        ZonedDateTime clock = ZonedDateTime.now();
        DateFormatter dateTracker = new DateFormatter(clock);

        formattedDate = dateTracker.getDate();

        dateText.setText(formattedDate);
    }
}
