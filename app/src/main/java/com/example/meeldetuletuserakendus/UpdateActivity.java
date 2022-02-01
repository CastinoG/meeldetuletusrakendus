package com.example.meeldetuletuserakendus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class UpdateActivity extends AppCompatActivity {

    EditText meeldetuletus_input, kirjeldus_input;
    Button uuendamise_nupp, kustutamise_nupp, kuupaeva_nupp, kella_nupp;
    private DatePickerDialog datePickerDialog;
    int hour, minute;


    String id, pealkiri, kirjeldus, kuupaev, kell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        initDatePicker();

        meeldetuletus_input = findViewById(R.id.meeldetuletus_input2);
        kirjeldus_input = findViewById(R.id.kirjeldus_input2);
        kuupaeva_nupp = findViewById(R.id.kuupaeva_nupp2);
        uuendamise_nupp = findViewById(R.id.uuendamise_nupp);
        kustutamise_nupp = findViewById(R.id.kustutamise_nupp);
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


                myDB.updateData(id, pealkiri, kirjeldus, kuupaev, kell);
                finish();

            }
        });
        kustutamise_nupp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();

            }
        });




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

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kustutada " + pealkiri + "?");
        builder.setMessage("Kas olete kindel, et tahate kustutada " + pealkiri + "?");
        builder.setPositiveButton("Jah", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                myDB.deleteOneRow(id);
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

    private String tananeKuupaev() {
        Calendar cal = Calendar.getInstance();
        int aasta = cal.get(Calendar.YEAR);
        int kuu = cal.get(Calendar.MONTH);
        kuu = kuu + 1;
        int paev = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(paev, kuu, aasta);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int aasta, int kuu, int paev) {
                kuu = kuu + 1;
                kuupaev = makeDateString(paev, kuu, aasta);
                kuupaeva_nupp.setText(kuupaev);

            }
        };

        Calendar cal = Calendar.getInstance();
        int aasta = cal.get(Calendar.YEAR);
        int kuu = cal.get(Calendar.MONTH);
        int paev = cal.get(Calendar.DAY_OF_MONTH);

        int style = android.app.AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, aasta, kuu, paev);
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());


    }


    private String makeDateString(int paev, int kuu, int aasta) {
        return paev + ". " + kuuNimetus(kuu) + " " + aasta;
    }

    private String kuuNimetus(int kuu) {
        if(kuu == 1)
            return "Jaanuar";
        if(kuu == 2)
            return "Veebruar";
        if(kuu == 3)
            return "Märts";
        if(kuu == 4)
            return "Aprill";
        if(kuu == 5)
            return "Mai";
        if(kuu == 6)
            return "Juuni";
        if(kuu == 7)
            return "Juuli";
        if(kuu == 8)
            return "August";
        if(kuu == 9)
            return "September";
        if(kuu == 10)
            return "Oktoober";
        if(kuu == 11)
            return "November";
        if(kuu == 12)
            return "Detsember";

        return "JAANUAR";
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                kella_nupp.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                kell = String.valueOf(kella_nupp.getText());


            }
        };

        int style = android.app.AlertDialog.THEME_HOLO_LIGHT;



        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, true);



        timePickerDialog.setTitle("Valige aeg");
        timePickerDialog.show();
    }
}