package com.android.yangryProductions.Sessions;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

/**
 * Created by Yang on 07/09/2015.
 *
 * Activity that shows the stopwatch and times a new practice session
 */
public class SessionTimer extends Activity implements OnClickListener {

    private Chronometer chronometer;
    private boolean timerPaused = true; //boolean flag to help track if the timer is paused or not
    private long pausedTime = 0;    //capture paused time to help resume timer correctly
    private long elapsedTime = 0;   //capture the total elapsed time

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.session_timer);

        chronometer = (Chronometer)findViewById(R.id.chronometer);
        findViewById(R.id.start_button).setOnClickListener(this);
        findViewById(R.id.reset_button).setOnClickListener(this);
        findViewById(R.id.saveSession_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.start_button:
                if(timerPaused) {   //case if the start button is pressed before timer is initially started or is already paused
                    chronometer.setBase(SystemClock.elapsedRealtime() - pausedTime);
                    chronometer.start();
                    timerPaused = false;
                }else{  //case if the start button is pressed while the timer is running
                    chronometer.stop();
                    pausedTime =  SystemClock.elapsedRealtime() - chronometer.getBase();
                    elapsedTime += pausedTime;
                    timerPaused = true;
                }
                break;
            case R.id.reset_button:
                chronometer.stop();
                chronometer.setBase(SystemClock.elapsedRealtime());
                pausedTime=0;
                timerPaused=true;
                break;
            case R.id.saveSession_button:
                chronometer.stop();
                pausedTime = SystemClock.elapsedRealtime() - chronometer.getBase();
                elapsedTime += pausedTime;
                timerPaused = true;
                ((TextView)findViewById(R.id.elapsedTime)).setText(String.valueOf(elapsedTime));
                break;
        }
        //update the start button text depending on the state of the timer
        if(timerPaused)
            ((Button)findViewById(R.id.start_button)).setText(R.string.start);
        else
            ((Button)findViewById(R.id.start_button)).setText(R.string.pause);
    }
}
