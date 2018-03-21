package com.tucker.nat20.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by eli on 3/20/2018.
 */

public class SpellDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "savedSpells.db";
    private static int DATABASE_VERSION = 1;

    public SpellDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String SQL_CREATE_SAVED_SPELLS_TABLE =
                "CREATE TABLE " + SpellContract.SavedSpells.TABLE_NAME + "(" +
                        SpellContract.SavedSpells._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        SpellContract.SavedSpells.COLUMN_NAME + " TEXT NOT NULL, " +
                        SpellContract.SavedSpells.COLUMN_URL + " TEXT NOT NULL, " +
                        SpellContract.SavedSpells.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";
        db.execSQL(SQL_CREATE_SAVED_SPELLS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + SpellContract.SavedSpells.TABLE_NAME + ";");
        onCreate(db);
    }
}
