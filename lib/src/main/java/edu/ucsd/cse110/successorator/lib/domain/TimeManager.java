package edu.ucsd.cse110.successorator.lib.domain;

import java.util.Timer;
import java.util.TimerTask;

public class TimeManager {

    private DateFormatter dateFormatter;

    public TimeManager() {
        dateFormatter = new DateFormatter();
        Timer timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                dateFormatter.formatDate();
                dateFormatter.formatTime();
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }
}
