package edu.ucsd.cse110.successorator;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.databinding.AddItemDialogBinding;
import edu.ucsd.cse110.successorator.ui.itemList.dialog.CreateItemDialogFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var view = ActivityMainBinding.inflate(getLayoutInflater(), null, false);
        view.addItem.setOnClickListener(v ->{
            var dialogFragment = CreateItemDialogFragment.newInstance();
            // Unsure if we should use getSupportFragmentManager() or getParentFragmentManager()
            dialogFragment.show(getSupportFragmentManager(),"CreateItemDialogFragment");
        });
        view.placeholderText.setText(R.string.hello_world);

        setContentView(view.getRoot());
    }
}
