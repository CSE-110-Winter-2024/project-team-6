package edu.ucsd.cse110.successorator.ui.itemList;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.DateFormatter;
import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentCardListBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentPendingListBinding;
import edu.ucsd.cse110.successorator.lib.domain.Item;
import edu.ucsd.cse110.successorator.ui.itemList.dialog.ConfirmDeleteCardDialogFragment;
import edu.ucsd.cse110.successorator.ui.itemList.dialog.CreateItemDialogFragment;
import edu.ucsd.cse110.successorator.ui.itemList.dialog.CreatePendingItemDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PendingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PendingListFragment extends ParentFragment {
    private FragmentPendingListBinding view;
    private ItemListAdapter adapter;

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
        //TODO: MODIFY THIS TO DO PENDING FUNCTIONALITY INSTEAD OF RECURRING FUNCTIONALITY
        this.adapter = new ItemListAdapter(requireContext(), getParentFragmentManager(), List.of(), activityModel::remove, activityModel::append, activityModel::prepend, activityModel::markCompleteOrIncomplete, "PENDING");
        activityModel.getOrderedCards().observe(cards -> {
            if(cards == null) return;
            adapter.clear();

            for(int i = 0; i < cards.size(); i++){
                if(cards.get(i).isPending()){
                    adapter.add(cards.get(i));
                }
            }
            adapter.addAll(new ArrayList<>(cards));
            adapter.notifyDataSetChanged();
            for(int i = 0; i < cards.size(); i++) {
                Log.d("Ordered cards changed", cards.get(i).sortOrder() + " " + i + " " + cards.get(i).getDescription());
            }

            if(activityModel.size() != 0 && view != null){
                view.placeholderText.setVisibility(View.GONE);
            }
            if(activityModel.size() == 0 && view != null){
                view.placeholderText.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = FragmentPendingListBinding.inflate(inflater, container, false);
        view.cardList.setAdapter(adapter);
        view.addItem.setOnClickListener(v ->{
            var dialogFragment = CreatePendingItemDialogFragment.newInstance();
            // Unsure if we should use getSupportFragmentManager() or getParentFragmentManager()
            dialogFragment.show(getParentFragmentManager(),"CreatePendingItemDialogFragment");
        });

        return view.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        activityModel.getOrderedCards().observe(cards -> {
            if(cards == null) return;
            adapter.clear();
            for(int i = 0; i < cards.size(); i++){
                //Log.d("IS_PENDING", cards.get(i).isPending() + " " + cards.get(i).getDescription());
                if(cards.get(i).isPending()){
                    adapter.add(cards.get(i));
                }
            }
            //adapter.addAll(new ArrayList<>(cards));
            adapter.notifyDataSetChanged();
            for(int i = 0; i < cards.size(); i++) {
                Log.d("Ordered cards changed", cards.get(i).sortOrder() + " " + i + " " + cards.get(i).getDescription());
            }

            if(activityModel.size() != 0){
                this.view.placeholderText.setVisibility(View.GONE);
            }
            if(activityModel.size() == 0){
                this.view.placeholderText.setVisibility(View.VISIBLE);
            }
        });
    }
    public void onResume() {
        super.onResume();
    }

}