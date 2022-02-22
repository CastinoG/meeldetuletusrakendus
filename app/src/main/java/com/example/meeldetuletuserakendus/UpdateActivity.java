package com.example.meeldetuletuserakendus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class UpdateActivity extends AppCompatActivity {

    EditText meeldetuletus_input, kirjeldus_input;
    Button uuendamise_nupp, kustutamise_nupp, kuupaeva_nupp, kella_nupp, meeldetuletuse_nupp;
    private DatePickerDialog datePickerDialog;
    int tund, minut;
    Calendar kalender;
    String id, pealkiri, kirjeldus, kuupaev, kell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        esialgneAjaValija();
        kalender = Calendar.getInstance();
        meeldetuletus_input = findViewById(R.id.meeldetuletus_input2);
        kirjeldus_input = findViewById(R.id.kirjeldus_input2);
        kuupaeva_nupp = findViewById(R.id.kuupaeva_nupp2);
        uuendamise_nupp = findViewById(R.id.uuendamise_nupp);
        kustutamise_nupp = findViewById(R.id.kustutamise_nupp);
        meeldetuletuse_nupp = findViewById(R.id.meeldetuletuse_nupp);
        kella_nupp = findViewById(R.id.kella_nupp2);

        getAndSetIntentData();

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(pealkiri);
        }

        uuendamise_nupp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                pealkiri=meeldetuletus_input.getText().toString().trim();
                kirjeldus=kirjeldus_input.getText().toString().trim();
                myDB.uuendaInfot(id, pealkiri, kirjeldus, kuupaev, kell);
                Toast.makeText(UpdateActivity.this, "Uuendatud!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        meeldetuletuse_nupp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alustaAlarm(kalender);
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                pealkiri=meeldetuletus_input.getText().toString().trim();
                kirjeldus=kirjeldus_input.getText().toString().trim();

                myDB.uuendaInfot(id, pealkiri, kirjeldus, kuupaev, kell);
                Toast.makeText(UpdateActivity.this, "Info uuendatud ja meeldetuletus tehtud!", Toast.LENGTH_LONG).show();
            }
        });

        kustutamise_nupp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kinnitusKast();
            }
        });
    }

    private void katkestaAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(getApplicationContext(), NotificationPublisher.class);
        int channelID = Integer.parseInt(id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), channelID, myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    private void alustaAlarm(Calendar kalender) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationPublisher.class);
        int channelID = Integer.parseInt(id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, channelID, intent, 0);
        if (kalender.before(Calendar.getInstance())) {
            kalender.add(Calendar.DATE, channelID);
        }
        Objects.requireNonNull(alarmManager).setExact(AlarmManager.RTC_WAKEUP,
                kalender.getTimeInMillis(), pendingIntent);
    }

    void getAndSetIntentData() {
        if(getIntent().hasExtra("id") && getIntent().hasExtra("pealkiri") && getIntent().hasExtra("kirjeldus") && getIntent().hasExtra("kuupaev") && getIntent().hasExtra("kell")) {
            id = getIntent().getStringExtra("id");
            pealkiri = getIntent().getStringExtra("pealkiri");
            kirjeldus = getIntent().getStringExtra("kirjeldus");
            kuupaev = getIntent().getStringExtra("kuupaev");
            kell = getIntent().getStringExtra("kell");
            meeldetuletus_input.setText(pealkiri);
            kirjeldus_input.setText(kirjeldus);
            kuupaeva_nupp.setText(kuupaev);
            kella_nupp.setText(kell);

        }else{
            Toast.makeText(this, "Info puudub", Toast.LENGTH_SHORT).show();
        }
    }

    void kinnitusKast() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kustutada " + pealkiri + "?");
        builder.setMessage("Kas olete kindel, et tahate kustutada " + pealkiri + "?");
        builder.setPositiveButton("Jah", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                myDB.kustutaRida(id);
                katkestaAlarm();
                finish();
            }
        });
        builder.setNegativeButton("Ei", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }

    private void esialgneAjaValija() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int aasta, int kuu, int paev) {
                int kuus = kuu + 1;
                kuupaev = teeKuupaevaString(paev, kuus, aasta);
                kalender.set(Calendar.YEAR, aasta);
                kalender.set(Calendar.MONTH, kuu);
                kalender.set(Calendar.DAY_OF_MONTH, paev);
                kuupaeva_nupp.setText(kuupaev);
            }
        };
        Calendar kal = Calendar.getInstance();
        int aasta = kal.get(Calendar.YEAR);
        int kuu = kal.get(Calendar.MONTH);
        int paev = kal.get(Calendar.DAY_OF_MONTH);
        int stiil = android.app.AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, stiil, dateSetListener, aasta, kuu, paev);
        datePickerDialog.getDatePicker().setMinDate(kal.getTimeInMillis());
    }

    private String teeKuupaevaString(int paev, int kuu, int aasta) {
        return paev + ". " + kuuNimetus(kuu) + " " + aasta;
    }

    private String kuuNimetus(int kuu) {
        if(kuu == 1)
            return "jaanuar";
        if(kuu == 2)
            return "veebruar";
        if(kuu == 3)
            return "märts";
        if(kuu == 4)
            return "aprill";
        if(kuu == 5)
            return "mai";
        if(kuu == 6)
            return "juuni";
        if(kuu == 7)
            return "juuli";
        if(kuu == 8)
            return "august";
        if(kuu == 9)
            return "september";
        if(kuu == 10)
            return "oktoober";
        if(kuu == 11)
            return "november";
        if(kuu == 12)
            return "detsember";
        return "JAANUAR";
    }

    public void avaKuupaevaValija(View view) {
        datePickerDialog.show();
    }

    public void avaAjaValija(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int valitudTund, int valitudMinut) {
                tund = valitudTund;
                minut = valitudMinut;
                kalender.set(Calendar.HOUR_OF_DAY, valitudTund);
                kalender.set(Calendar.MINUTE, valitudMinut);
                kalender.set(Calendar.SECOND, 0);
                kella_nupp.setText(String.format(Locale.getDefault(), "%02d:%02d", tund, minut));
                kell = String.valueOf(kella_nupp.getText());
            }
        };
        int stiil = android.app.AlertDialog.THEME_HOLO_LIGHT;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, stiil, onTimeSetListener, tund, minut, true);
        timePickerDialog.setTitle("Valige aeg");
        timePickerDialog.show();
    }
}