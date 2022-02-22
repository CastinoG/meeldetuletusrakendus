package com.example.meeldetuletuserakendus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton lisa_nupp, teavituse_nupp;
    MyDatabaseHelper myDB;
    ArrayList<String> meeldetuletus_id, meeldetuletus_pealkiri, meeldetuletus_kirjeldus, meeldetuletus_kuupaev, meeldetuletus_kell;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);

        this.setTitle("Märkmik");

        lisa_nupp = findViewById(R.id.lisa_nupp);
        lisa_nupp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LisaActivity.class);
                startActivity(intent);
            }
        });

        teavituse_nupp = findViewById(R.id.teavituseNupp);
        teavituse_nupp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intenta = new Intent(MainActivity.this, MeeldetuletusActivity.class);
                startActivity(intenta);
            }
        });

        myDB = new MyDatabaseHelper(MainActivity.this);
        meeldetuletus_id = new ArrayList<>();
        meeldetuletus_pealkiri = new ArrayList<>();
        meeldetuletus_kirjeldus = new ArrayList<>();
        meeldetuletus_kuupaev = new ArrayList<>();
        meeldetuletus_kell = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new CustomAdapter(MainActivity.this, this, meeldetuletus_id, meeldetuletus_pealkiri, meeldetuletus_kirjeldus, meeldetuletus_kuupaev, meeldetuletus_kell);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }

    void storeDataInArrays() {
        Cursor cursor = myDB.loeInfot();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Andmed puuduvad", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                meeldetuletus_id.add(cursor.getString(0));
                meeldetuletus_pealkiri.add(cursor.getString(1));
                meeldetuletus_kirjeldus.add(cursor.getString(2));
                meeldetuletus_kuupaev.add(cursor.getString(3));
                meeldetuletus_kell.add(cursor.getString(4));
            }
        }
    }
}