package com.example.meeldetuletuserakendus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Meeldetuletused.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "minu_meeldetuletused";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_PEALKIRI = "meeldetuletus_pealkiri";
    private static final String COLUMN_KIRJELDUS = "meeldetuletus_kirjeldus";
    private static final String COLUMN_KUUPAEV = "meeldetuletus_kuupaev";
    private static final String COLUMN_KELL = "meeldetuletus_kell";

    MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_PEALKIRI + " TEXT, " +
                        COLUMN_KIRJELDUS + " TEXT, " +
                        COLUMN_KUUPAEV + " TEXT, " +
                        COLUMN_KELL+ " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void lisaMeeldetuletus(String pealkiri, String kirjeldus, String kuupaev, String kell) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PEALKIRI, pealkiri);
        cv.put(COLUMN_KIRJELDUS, kirjeldus);
        cv.put(COLUMN_KUUPAEV, kuupaev);
        cv.put(COLUMN_KELL, kell);
        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Lisamine Ebaõnnestus", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Lisatud!", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor loeInfot() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void uuendaInfot(String row_id, String pealkiri, String kirjeldus, String kuupaev, String kell) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PEALKIRI, pealkiri);
        cv.put(COLUMN_KIRJELDUS, kirjeldus);
        cv.put(COLUMN_KUUPAEV, kuupaev);
        cv.put(COLUMN_KELL, kell);

        long tulemus = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
        if(tulemus == -1) {
            Toast.makeText(context, "Uuendamine ebaõnnestus!", Toast.LENGTH_SHORT).show();
        }else {
        }
    }

    void kustutaRida(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if (result == -1){
            Toast.makeText(context, "Kustutamine ebaõnnestus", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Kustutatud!", Toast.LENGTH_SHORT).show();
        }
    }
}
