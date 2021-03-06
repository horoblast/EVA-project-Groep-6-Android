package com.example.bremme.eva_projectg6.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.bremme.eva_projectg6.domein.Challenge;

import java.io.Console;
import java.util.List;


/**
 * Created by brechttanghe on 2/12/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    public static final String DATABASE_NAME = "challenge.db";
    public static final String TABLE_NAME_ALL_CHALLENGES = "allChallenges_table";
    public static final String TABLE_NAME_COMPLETED_CHALLENGES = "completedChallenges_data";
    public static final String ID = "ID";
    public static final String NAME = "NAME";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String DIFFICULTY = "DIFFICULTY";
    public static final String URL = "URL";
    public static final String ISCHILDFRIENDLY = "ISCHILDFRIENDLY";
    public static final String ISSTUDENTFRIENDLY = "ISSTUDENTFRIENDLY";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        db = this.getWritableDatabase();
        onCreate(db);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("Database me", "Dropping all tables");
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ALL_CHALLENGES);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_COMPLETED_CHALLENGES);
        db.execSQL("create table if not exists " + TABLE_NAME_ALL_CHALLENGES + " (ID TEXT ,NAME TEXT,DESCRIPTION TEXT, DIFFICULTY TEXT, URL STRING, ISSTUDENTFRIENDLY INT, ISCHILDFRIENDLY INT)");
        db.execSQL("create table if not exists " + TABLE_NAME_COMPLETED_CHALLENGES + " (ID TEXT ,NAME TEXT,DESCRIPTION TEXT, DIFFICULTY TEXT, URL STRING, ISSTUDENTFRIENDLY INT, ISCHILDFRIENDLY INT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ALL_CHALLENGES);
        onCreate(db);
    }
    public void clearCompletedTable()
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_COMPLETED_CHALLENGES);
        db.execSQL("create table if not exists " + TABLE_NAME_COMPLETED_CHALLENGES + " (ID TEXT ,NAME TEXT,DESCRIPTION TEXT, DIFFICULTY TEXT, URL STRING, ISSTUDENTFRIENDLY INT, ISCHILDFRIENDLY INT)");;
    }
    public void putCompletedChallengesInDB(List<Challenge> challenges){
        for(Challenge c : challenges){
            insertCompletedChallenges(c);
        }
    }

    public void putAllChallengesInDb(Challenge[] challenges){
        for(int i = 0; i < challenges.length; i++){
            inserAllChallenges(challenges[i]);
        }

    }

    private void inserAllChallenges(Challenge challenge){
        db.insert(TABLE_NAME_ALL_CHALLENGES, null , getContentValues(challenge));
    }

    private void insertCompletedChallenges(Challenge challenge){
        db.insert(TABLE_NAME_COMPLETED_CHALLENGES, null , getContentValues(challenge));
    }

    private ContentValues getContentValues(Challenge challenge){
        ContentValues contentValues = new ContentValues();

        try{
            contentValues.put(ID, challenge.getId());
            contentValues.put(NAME, challenge.getName());
            contentValues.put(DESCRIPTION, challenge.getDescription());
            contentValues.put(DIFFICULTY, challenge.getDifficulty().toString());
            contentValues.put(URL, challenge.getUrlImage());
            contentValues.put(ISSTUDENTFRIENDLY, challenge.isStudentFriendly());
            contentValues.put(ISCHILDFRIENDLY, challenge.isChildFriendly());
            /*contentValues.put(COL_1 , challenge.getId());
            contentValues.put(COL_2 , challenge.getName());
            contentValues.put(COL_3 , challenge.getDescription());
            contentValues.put(COL_4 , challenge.getDifficulty().toString());
            contentValues.put(COL_5 , challenge.getUrlImage());*/
        }catch (Exception ex){
            Log.i("Wegschrijven naar db", "niet alle gegevens zijn weggeschreven");
        }

        return contentValues;
    }

    public Cursor getAllData(){
        Log.i("Get", "AllData getting");
        db = this.getWritableDatabase();
       // Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_ALL_CHALLENGES, null);
       Cursor res = db.rawQuery("select * from " + TABLE_NAME_ALL_CHALLENGES ,null);
        return res;
    }
    public Cursor getCompletedChallenges()
    {
        db = this.getWritableDatabase();
        // Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_ALL_CHALLENGES, null);
        Cursor res = db.rawQuery("select * from " + TABLE_NAME_COMPLETED_CHALLENGES ,null);
        return res;
    }
    public int getCountData()
    {
        return getReadableDatabase().rawQuery("select * from " + TABLE_NAME_ALL_CHALLENGES ,null).getCount();
    }


}
