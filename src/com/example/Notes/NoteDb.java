package com.example.Notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import nl.qbusict.cupboard.QueryResultIterable;

import java.util.List;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class NoteDb{

    SQLiteDatabase db;

    public NoteDb(Context context){
        CupboardSQLiteOpenHelper dbHelper = new CupboardSQLiteOpenHelper(context);
        this.db = dbHelper.getWritableDatabase();
    }

    public long addNote(Note note){
        return cupboard().withDatabase(this.db).put(note);
    }

    public Note getNote(String selector, String key){
        return cupboard().withDatabase(this.db).query(Note.class).withSelection(selector, key).get();
    }

    public Note getNote(Class<Note> note, Long id){
        return cupboard().withDatabase(this.db).get(note, id);
    }

    public void clearNotes(){
        db.execSQL("DELETE FROM " + Note.class.getSimpleName());
    }

    public List<Note> getNotes() {
        List<Note> result;
        Cursor notes = cupboard().withDatabase(this.db).query(Note.class).getCursor();
        QueryResultIterable<Note> itr = null;
        try {
            itr = cupboard().withCursor(notes).iterate(Note.class);
            result = itr.list();
        } finally {
            itr.close();
        }
        return result;
    }

    public void updateNotes(Long id, ContentValues values){
        cupboard().withDatabase(this.db).update(Note.class, values, "_id = ?", String.valueOf(id));
    }
}