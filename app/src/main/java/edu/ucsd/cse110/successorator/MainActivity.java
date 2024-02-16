package edu.ucsd.cse110.successorator;


import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.time.ZonedDateTime;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        this.view = ActivityMainBinding.inflate(getLayoutInflater(), null, false);

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
    }
}
