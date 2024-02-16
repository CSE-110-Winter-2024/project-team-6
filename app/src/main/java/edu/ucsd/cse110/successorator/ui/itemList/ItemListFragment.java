package edu.ucsd.cse110.successorator.ui.itemList;

import android.os.Bundle;
import android.util.Log;
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
import edu.ucsd.cse110.successorator.databinding.ItemCardBinding;
import edu.ucsd.cse110.successorator.ui.itemList.dialog.CreateItemDialogFragment;
import edu.ucsd.cse110.successorator.databinding.FragmentCardListBinding;

public class ItemListFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentCardListBinding view;
    private ItemListAdapter adapter;
    private TextView dateText;
    private String formattedDate;

    private DateFormatter dateFormatter;

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

        this.adapter = new ItemListAdapter(requireContext(), List.of(), activityModel::remove, activityModel::append, activityModel::prepend, activityModel::markCompleteOrIncomplete);

        activityModel.getOrderedCards().observe(cards -> {
            if(cards == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(cards));
            adapter.notifyDataSetChanged();
            for(int i = 0; i < cards.size(); i++) {
                Log.d("Ordered cards changed", cards.get(i).sortOrder() + " " + i + " " + cards.get(i).getDescription());
            }

            if(activityModel.size() != 0){
                view.placeholderText.setVisibility(View.GONE);
            }
            if(activityModel.size() == 0){
                view.placeholderText.setVisibility(View.VISIBLE);
            }
        });

        dateFormatter = new DateFormatter(ZonedDateTime.now());


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            this.view = FragmentCardListBinding.inflate(inflater, container, false);
            // Set the adapter on the ListView
            view.cardList.setAdapter(adapter);
            dateText = this.view.dateView;
            view.addItem.setOnClickListener(v ->{
                var dialogFragment = CreateItemDialogFragment.newInstance();
                // Unsure if we should use getSupportFragmentManager() or getParentFragmentManager()
                dialogFragment.show(getParentFragmentManager(),"CreateItemDialogFragment");
            });
            // When pressing the add date button, the Date will advance by 24hrs
            view.addDay.setOnClickListener(v -> {
                formattedDate = dateFormatter.addDay(ZonedDateTime.now());

                dateText.setText(formattedDate);
            });
            return view.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Get formatted date and display.
        formattedDate = dateFormatter.getDate(ZonedDateTime.now());

        dateText.setText(formattedDate);
    }
}
