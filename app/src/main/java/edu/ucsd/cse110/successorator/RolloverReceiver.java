package edu.ucsd.cse110.successorator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;

public class RolloverReceiver extends BroadcastReceiver {
    private static final String TAG = "RolloverReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        var app = (SuccessoratorApplication) context.getApplicationContext();
        // CAN UPDATE RESPOSITORY DIRECLTY FROM HERE (through app bc it's in same activity context)


        //Log.i(TAG, dt.getDate());
    }
}