package com.example.meeldetuletuserakendus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Locale;

public class LisaActivity extends AppCompatActivity {

    EditText meeldetuletus_input, kirjeldus_input;
    Button lisamise_nupp, kuupaeva_nupp, kella_nupp;
    private DatePickerDialog datePickerDialog;
    public String kuupaev = null;
    public String kell = null;
    int tund, minut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lisa);
        initDatePicker();
        kuupaeva_nupp = findViewById(R.id.kuupaeva_nupp);
        kella_nupp = findViewById(R.id.kella_nupp);
        meeldetuletus_input = findViewById(R.id.meeldetuletus_input);
        kirjeldus_input = findViewById(R.id.kirjeldus_input);
        lisamise_nupp = findViewById(R.id.lisamise_nupp);

        lisamise_nupp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper MyDB = new MyDatabaseHelper(LisaActivity.this);
                MyDB.lisaMeeldetuletus(meeldetuletus_input.getText().toString().trim(),
                        kirjeldus_input.getText().toString().trim(),
                        kuupaev.trim(),
                        kell.trim());
            }
        });
    }

    private String tananeKuupaev() {
        Calendar kalender = Calendar.getInstance();
        int aasta = kalender.get(Calendar.YEAR);
        int kuu = kalender.get(Calendar.MONTH);
        kuu = kuu + 1;
        int paev = kalender.get(Calendar.DAY_OF_MONTH);
        return koostaKuupaevaString(paev, kuu, aasta);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int aasta, int kuu, int paev) {
                int kuus = kuu + 1;
                kuupaev = koostaKuupaevaString(paev, kuus, aasta);
                kuupaeva_nupp.setText(kuupaev);
            }
        };

        Calendar kalender = Calendar.getInstance();
        int aasta = kalender.get(Calendar.YEAR);
        int kuu = kalender.get(Calendar.MONTH);
        int paev = kalender.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, aasta, kuu, paev);
        datePickerDialog.getDatePicker().setMinDate(kalender.getTimeInMillis());
    }

    private String koostaKuupaevaString(int paev, int kuu, int aasta) {
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
        return "JAN";
    }

    public void avaKuupaevaValija(View view) {
        datePickerDialog.show();
    }

    public void avaAjaValija(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker timePicker, int valitudTund, int valitudMinut) {
                tund = valitudTund;
                minut = valitudMinut;
                kella_nupp.setText(String.format(Locale.getDefault(), "%02d:%02d", tund, minut));
                kell = String.valueOf(kella_nupp.getText());
            }
        };

        int stiil = AlertDialog.THEME_HOLO_LIGHT;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, stiil, onTimeSetListener, tund, minut, true);
        timePickerDialog.setTitle("Valige aeg");
        timePickerDialog.show();
    }
}