package com.example.meeldetuletuserakendus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

public class MeeldetuletusActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    String timeText;
    TextView textTime;
    Button nupp_vali, nupp_katkesta, nupp_kuupaev, nupp_alarm;
    Calendar c;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeldetuletus);

        nupp_vali = findViewById(R.id.button2);
        nupp_katkesta = findViewById(R.id.nupp_katkesta);
        nupp_kuupaev = findViewById(R.id.button);
        nupp_alarm = findViewById(R.id.button3);

        c = Calendar.getInstance();

        nupp_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAlarm(c);


            }
        });

        nupp_vali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");

            }
        });

        nupp_kuupaev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment kuupaevaValija = new KuupaevaFragment();
                kuupaevaValija.show(getSupportFragmentManager(), "date picker");
            }
        });

        nupp_katkesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                katkestaAlarm();
                Toast.makeText(MeeldetuletusActivity.this, "Meeldetuletus unustatud", Toast.LENGTH_SHORT).show();

            }
        });




    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
    }


    public void onDateSet (DatePicker view, int aasta, int kuu, int paev) {
        c.set(Calendar.YEAR, aasta);
        c.set(Calendar.MONTH, kuu);
        c.set(Calendar.DAY_OF_MONTH, paev);
    }



    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        Objects.requireNonNull(alarmManager).setExact(AlarmManager.RTC_WAKEUP,
                c.getTimeInMillis(), pendingIntent);
    }

    private void katkestaAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(getApplicationContext(), NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), 1, myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }
}

