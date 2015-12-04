package com.example.bremme.eva_projectg6.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.bremme.eva_projectg6.domein.Challenge;
import com.google.gson.JsonArray;




/**
 * Created by brechttanghe on 2/12/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private RestApiRepository repo;
    private SQLiteDatabase db = this.getWritableDatabase();

    public static final String DATABASE_NAME = "challenge.db";
    public static final String TABLE_NAME = "challenge_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "DESCRIPTION";
    public static final String COL_4 = "DIFFICULTY";
    public static final String COL_5 = "URL";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        onCreate(db);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        repo = new RestApiRepository();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("create table " + TABLE_NAME + " (ID TEXT ,NAME TEXT,DESCRIPTION TEXT, DIFFICULTY TEXT, URL STRING)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void putAllDataInDb(Challenge[] challenges){
        SQLiteDatabase db = this.getWritableDatabase();
        //Challenge[] challenges = repo.getAllChallenges(result,language);

        for(int i = 0; i < challenges.length; i++){
            insertData(challenges[i]);
        }
    }

    public void insertData(Challenge challenge){

        ContentValues contentValues = new ContentValues();

        try{
            contentValues.put(COL_1 , challenge.getId());
            contentValues.put(COL_2 , challenge.getName());
            contentValues.put(COL_3 , challenge.getDescription());
            contentValues.put(COL_4 , challenge.getDifficulty().toString());
            contentValues.put(COL_5 , challenge.getUrlImage());
        }catch (Exception ex){
            Log.i("Wegschrijven naar db", "niet alle gegevens zijn weggeschreven");
        }


        db.insert(TABLE_NAME , null , contentValues);

        //db.close();
    }



}
