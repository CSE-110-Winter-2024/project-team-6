package edu.ucsd.cse110.successorator;


import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.time.ZonedDateTime;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.ui.itemList.dialog.CreateItemDialogFragment;

public class MainActivity extends AppCompatActivity {
    private TextView dateText;
    private String formattedDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var view = ActivityMainBinding.inflate(getLayoutInflater(), null, false);
        view.addItem.setOnClickListener(v ->{
            var dialogFragment = CreateItemDialogFragment.newInstance();
            // Unsure if we should use getSupportFragmentManager() or getParentFragmentManager()
            dialogFragment.show(getSupportFragmentManager(),"CreateItemDialogFragment");
        });

        dateText = view.dateView;

        setContentView(view.getRoot());
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Setup for AlarmManager for future implementation; will allow Receiver to interact with DataBase
        var intent = new Intent(this, RolloverReceiver.class);
        var pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            throw new RuntimeException(e);
        }

        // Get formatted date and display.
        ZonedDateTime clock = ZonedDateTime.now();
        DateFormatter dateTracker = new DateFormatter(clock);

        formattedDate = dateTracker.getDate();

        dateText.setText(formattedDate);
    }
}
