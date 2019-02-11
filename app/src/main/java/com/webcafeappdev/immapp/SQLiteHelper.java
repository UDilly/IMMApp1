package com.webcafeappdev.immapp;

/**
 * Created by Urgent Nkala on 10/14/2018.
 */


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper {
    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    public void queryData(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

// This code inserts data into table incident

    public void insertData(String Name, String Date, String Description, String IncidentClass, String Locations, byte[] image) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO INCIDENT VALUES (NULL, ?, ?, ?, ?, ?, ?)";


        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, Name);
        statement.bindString(2, Date);
        statement.bindString(3, Description);
        statement.bindString(4, IncidentClass);
        statement.bindString(5, Locations);
        statement.bindBlob(6, image);


        statement.executeInsert();
    }

    public Cursor getData(String sql) {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

//This code populates data into Incident table

    public void updateData(String Name, String Date, String Description, String IncidentClass, String Locations, byte[] image, int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE INCIDENT SET Name = ?, Date = ?, Description = ?,IncidentClass = ?,Locations = ?, image = ? WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, Name);
        statement.bindString(2, Date);
        statement.bindString(3, Description);
        statement.bindString(4, IncidentClass);
        statement.bindString(5, Locations);
        statement.bindBlob(6, image);
        statement.bindDouble(7, (double) id);

        statement.execute();
        database.close();
    }
    //This code Deletes data from Incident table

        public void deleteData(int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM INCIDENT WHERE id =?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double) id);

        statement.execute();
        database.close();
    }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

        public Cursor display() {
        SQLiteDatabase sqLiteHelper = this.getWritableDatabase();
        Cursor cursor = sqLiteHelper.rawQuery("SELECT Locations FROM INCIDENT",null);
       return cursor;
   }

}

