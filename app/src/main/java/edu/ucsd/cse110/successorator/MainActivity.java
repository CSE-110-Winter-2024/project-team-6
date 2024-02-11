package edu.ucsd.cse110.successorator;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
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

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView dateText = findViewById(R.id.date_view);
                                long date = System.currentTimeMillis();
                                // Update date only at 2:00A.M.
                                date = date - 7200000;
                                SimpleDateFormat sdf = new SimpleDateFormat("EEEE M/dd");
                                String dateString = sdf.format(date);
                                dateText.setText(dateString);
                            }
                        });
                    }
                } catch (InterruptedException e) {}
            }
        };

        thread.start();
        setContentView(view.getRoot());
    }
}
