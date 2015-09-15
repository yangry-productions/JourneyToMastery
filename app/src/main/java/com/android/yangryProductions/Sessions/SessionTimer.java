package com.android.yangryProductions.Sessions;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Yang on 07/09/2015.
 *
 * Activity that shows the stopwatch and times a new practice session
 */
public class SessionTimer extends Activity {
    Button butnStart, butnReset;
    TextView time;
    long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    int t = 1;
    int hours = 0;
    int secs = 0;
    int mins = 0;
    //int milliseconds = 0;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.session_timer);
        butnStart = (Button)findViewById(R.id.start_button);
        butnReset = (Button)findViewById(R.id.reset_button);
        time = (TextView)findViewById(R.id.timer_display);

        butnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t==1){
                    //timer start
                    butnStart.setText(R.string.pause);
                    startTime = SystemClock.uptimeMillis();
                    handler.postDelayed(updateTimer,0);
                    t=0;
                }else{
                    //timer pause
                    butnStart.setText(R.string.start);
                    time.setTextColor(Color.BLUE);
                    timeSwapBuff += timeInMilliseconds;
                    handler.removeCallbacks(updateTimer);
                    t=1;
                }
            }
        });

        butnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = 0L;
                timeInMilliseconds = 0L;
                timeSwapBuff = 0L;
                updatedTime = 0L;
                t=1;
                secs=0;
                mins=0;
                //milliseconds=0;
                butnStart.setText(R.string.start);
                handler.removeCallbacks(updateTimer);
                time.setText(R.string.start_time);
            }
        });
    }

    public Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            secs = (int)(updatedTime/1000);
            mins = secs/60; //calculate total number of minutes
            hours = mins/60;    //calculate total number of hours
            mins = mins %60;    //calculate number of minutes to show (in case more than 60mins)
            secs = secs %60;    //calcuate number of seconds to show (in case more than 60secs)
            //milliseconds = (int)(updatedTime % 1000);

            //display maximum two levels of time. i.e. show seconds only, or minutes and seconds, or hours and minutes
            if(hours>0)
                time.setText(hours + "h " + String.format("%02d",mins) + "m ");
            else if(mins>0)
                time.setText(String.format("%02d",mins) + "m " + String.format("%02d",secs) + "s");
            else
                time.setText(secs + "s");

            time.setTextColor(Color.RED);
            handler.postDelayed(this,50);
        }
    };

}
