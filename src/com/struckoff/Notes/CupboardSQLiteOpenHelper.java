package com.struckoff.Notes;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class CupboardSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Notes.db";
    private static final int DATABASE_VERSION = 4;

    static {
        cupboard().register(Note.class);
        cupboard().register(Tag.class);
        cupboard().register(TagsToNotes.class);
    }

    public CupboardSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        cupboard().withDatabase(db).createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        cupboard().withDatabase(db).upgradeTables();
    }
}
