package com.example.administrator.counter;

import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.os.IBinder;
import android.util.*;
import android.view.*;
import android.widget.*;

import android.app.Activity;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.app.PendingIntent;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Administrator on 2015-12-05.
 */






public class Alarm extends Activity {
    AlarmService mAlarmService = null;
    static final int TIME_START = 0;
    static final int TIME_END = 1;
    static final int TIME_SHOW = 2;
    static final int TIME_CONNECT = 3;
    private int mHour;
    private int mMinute;
    private int h1;
    private int m1;

    private int h2;
    private int m2;

    private TextView mText;
    private Button start_alarm;
    private Button end_alarm;
    private Button show_alarm;
    private Button connect_alarm;

    private ServiceConnection mConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected( ComponentName name,
                                        IBinder service )
        {
            Log.d( "superdroid", "onServiceConnected()" );
            AlarmService.LocalBinder binder = (AlarmService.LocalBinder) service;
            mAlarmService = binder.getAlarmService();
        }

        @Override
        public void onServiceDisconnected( ComponentName name )
        {
            Log.d( "superdroid", "onServiceDisconnected()" );
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_button);
        mText = (TextView)findViewById(R.id.text);
        start_alarm = (Button) findViewById(R.id.start_alarm);
        end_alarm = (Button) findViewById(R.id.end_alarm);
        show_alarm = (Button)findViewById(R.id.show_alarm);
        connect_alarm = (Button)findViewById(R.id.connect_alarm);

        start_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_START);


            }
        });



        end_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                showDialog(TIME_END);
            }

        });

        show_alarm.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                showDialog(TIME_SHOW);
            }
        });

        connect_alarm.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                showDialog(TIME_CONNECT);
            }

        });
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);


    }


    private TimePickerDialog.OnTimeSetListener sTimeSetListener =
            new TimePickerDialog.OnTimeSetListener(){
                public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                    h1 = hourOfDay;
                    m1 = minute;

                }
            };

    private TimePickerDialog.OnTimeSetListener eTimeSetListener =
            new TimePickerDialog.OnTimeSetListener(){
                public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                    h2 = hourOfDay;
                    m2 = minute;

                }
            };
    private void connect_service(){
        Toast.makeText( Alarm.this,
                "Cur Count : " +
                        mAlarmService.getCurCountNumber(),
                Toast.LENGTH_LONG ).show();

    }

    private void updateDisplay(){
        mText.setText(String.format("%d시 %d분에서 %d시 %d분",h1,m1,h2,m2));
        Intent serviceIntent =
                new Intent(this,AlarmService.class);
        serviceIntent.putExtra("h1",h1);
        serviceIntent.putExtra("m1",m1);
        serviceIntent.putExtra("h2",h2);
        serviceIntent.putExtra("m2",m2);
        bindService(serviceIntent, mConnection, BIND_AUTO_CREATE);
        startService(serviceIntent);





    }

    protected  Dialog onCreateDialog(int id){
        switch(id){
            case TIME_START:
                return new TimePickerDialog(this,sTimeSetListener, mHour,mMinute,false);

            case TIME_END:
                return new TimePickerDialog(this,eTimeSetListener, mHour,mMinute,false);
            case TIME_SHOW:
                updateDisplay();
                break;
            case TIME_CONNECT:
                connect_service();

        }
        return null;

    }

};