package com.struckoff.Notes;

/**
 * Interface for main DB
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import nl.qbusict.cupboard.DatabaseCompartment;

import java.util.ArrayList;
import java.util.List;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class NoteDb{

    SQLiteDatabase db;
    DatabaseCompartment cup_withDB = null;

    public NoteDb(Context context){
        CupboardSQLiteOpenHelper dbHelper = new CupboardSQLiteOpenHelper(context);
        this.db = dbHelper.getWritableDatabase();
        this.cup_withDB = cupboard().withDatabase(this.db);
    }

    public long addNote(Note note){
        return this.cup_withDB.put(note);
    }

    public Note getNote(String selector, String key){
        return this.cup_withDB.query(Note.class).withSelection(selector, key).get();
    }

    public Note getNote(Class<Note> note, Long id){
        return this.cup_withDB.get(note, id);
    }

    public void clearNotes(){
        db.execSQL("DELETE FROM " + Note.class.getSimpleName());
    }

    public List<Note> getNotes() {
        List<Note> result;
        Cursor notes = this.cup_withDB.query(Note.class).getCursor();
        result = cupboard().withCursor(notes).list(Note.class);
        return result;
    }

    public void updateNotes(Long id, ContentValues values){
        this.cup_withDB.update(Note.class, values, "_id = ?", String.valueOf(id));
    }

    public void deleteNote(Long id) {
        this.cup_withDB.delete(Note.class, "_id = ?", String.valueOf(id));
    }

    public long addTag(Long note_id, String tag_text){
        Tag tag = this.cup_withDB.query(Tag.class).withSelection("text = ?", tag_text).get();
        Long tag_id = null;

        if (tag != null){
            tag_id = tag._id;
        }
        else {
            tag_id = this.cup_withDB.put(new Tag(tag_text));
        }

        TagsToNotes ttn = new TagsToNotes();
        ttn.note_id = note_id;
        ttn.tag_id = tag_id;

        this.cup_withDB.put(ttn);

        return tag_id;
    }

    public List<Tag> getTags(Long note_id){
        List<Tag> result = new ArrayList<Tag>();
        List<TagsToNotes> tagids;
        Cursor tagids_cursor = this.cup_withDB
                .query(TagsToNotes.class)
                .withSelection("note_id = ?", note_id.toString())
                .getCursor();
        tagids = cupboard().withCursor(tagids_cursor).list(TagsToNotes.class);
        for (TagsToNotes tagtonote : tagids){
            result.add(
                    this.cup_withDB
                    .query(Tag.class)
                    .withSelection("_id = ?", tagtonote.tag_id.toString())
                    .get()
            );
        }
        return result;
    }
}