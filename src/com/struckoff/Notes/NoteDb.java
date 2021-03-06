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

    public Note getNote(Long id){
        return this.cup_withDB.get(Note.class, id);
    }

    public void clearNotes(){
        db.execSQL("DELETE FROM " + Note.class.getSimpleName());
    }

    public void clearTags(){
        db.execSQL("DELETE FROM " + Tag.class.getSimpleName());
    }

    public List<Note> getNotes() {
        List<Note> result;
        Cursor notes = this.cup_withDB.query(Note.class).getCursor();
        result = cupboard().withCursor(notes).list(Note.class);
        return result;
    }

    public List<Note> getNotesByTagId(Long tag_id){
        List<Note> result = new ArrayList<>();
        List<TagsToNotes> noteids;
        Cursor tagids_cursor = this.cup_withDB
                .query(TagsToNotes.class)
                .withSelection("tag_id = ?", tag_id.toString())
                .getCursor();
        noteids = cupboard().withCursor(tagids_cursor).list(TagsToNotes.class);
        for (TagsToNotes tagtonote : noteids){
            result.add(
                    this.cup_withDB
                            .query(Note.class)
                            .withSelection("_id = ?", tagtonote.note_id.toString())
                            .get()
            );
        }
        return result;
    }

    public List<Long> getNotesIdsByTagId(Long tag_id){
        List<Long> result = new ArrayList<>();
        for (Note note : getNotesByTagId(tag_id)){
            result.add(note._id);
        }
        return result;
    }

    public void updateNotes(Long id, ContentValues values){
        this.cup_withDB.update(Note.class, values, "_id = ?", String.valueOf(id));
    }

    public void deleteNote(Long id) {
        this.cup_withDB.delete(Note.class, id);
        Cursor ttn_cursor = this.cup_withDB
                .query(TagsToNotes.class)
                .withSelection("note_id = ?", String.valueOf(id))
                .getCursor();

        List<TagsToNotes> tagsToNotes = cupboard().withCursor(ttn_cursor).list(TagsToNotes.class);
        this.cup_withDB.delete(TagsToNotes.class, "note_id = ?", String.valueOf(id));

        for (TagsToNotes ttn: tagsToNotes){
            TagsToNotes ttn0 = this.cup_withDB
                    .query(TagsToNotes.class)
                    .withSelection("tag_id = ?", String.valueOf(ttn.tag_id))
                    .get();

            if (ttn0 == null){
                this.cup_withDB.delete(Tag.class, ttn.tag_id);
            }
        }
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

    public Tag getTag(String selector, String key){
        return this.cup_withDB.query(Tag.class).withSelection(selector, key).get();
    }

    public Tag getTag(Long  tag_id){
        return this.cup_withDB.query(Tag.class).byId(tag_id).get();
    }

    public List<Tag> getTags(Long note_id){
        List<Tag> result = new ArrayList<>();
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

    public List<Tag> getTags(){
        List<Tag> result;
        Cursor tags = this.cup_withDB.query(Tag.class).getCursor();
        result = cupboard().withCursor(tags).list(Tag.class);
        return result;
    }

    public void deleteTag(Long note_id, String tag_body){
        Tag tag = this.cup_withDB.query(Tag.class).withSelection("text = ?", tag_body).get();
        if (tag != null){
            this.cup_withDB.delete(TagsToNotes.class, "note_id = ? AND tag_id = ?", note_id.toString(), tag._id.toString());
            if (this.cup_withDB.query(TagsToNotes.class).withSelection("tag_id = ?", tag._id.toString()).get() == null){
                this.cup_withDB.delete(Tag.class, tag._id);
            }
        }
    }

    public void globalTagDelete(Long tag_id){
        this.cup_withDB.delete(Tag.class, "_id = ?", String.valueOf(tag_id));
        this.cup_withDB.delete(TagsToNotes.class, "tag_id = ?", String.valueOf(tag_id));
    }

    public void globalTagEdit(Long tag_id, String new_tag_body){
        ContentValues values = new ContentValues(1);
        values.put("text", new_tag_body);
        this.cup_withDB.update(Tag.class, values, "_id = ?", String.valueOf(tag_id));
    }

    public List<Note> getNotesByTagIds(List<Long> tag_ids) {
        List<Note> result = new ArrayList<>();
        Boolean f;
        if (!tag_ids.isEmpty()){
            for (Long note_id : getNotesIdsByTagId(tag_ids.get(0))){
                f = true;
                for (Long tag_id : tag_ids){
                    if (!getNotesIdsByTagId(tag_id).contains(note_id)){
                        f = false;
                        break;
                    }
                }
                if (f){
                    result.add(getNote(note_id));
                }
            }
        }
        return result;
    }
}