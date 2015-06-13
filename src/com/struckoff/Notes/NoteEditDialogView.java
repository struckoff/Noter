package com.struckoff.Notes;

/**
 * Note edit dialog
 */

import android.content.ContentValues;

public class NoteEditDialogView extends NoteDialogView {

    public NoteEditDialogView(NoteItemView self_noteitem) {
        super(self_noteitem);
    }

    public void Create(){
        title.setText(this.note.getTitle());
        text.setText(this.note.getText());
    }

    public void Positive(){
        note.setTitle(title.getText().toString());
        note.setText(text.getText().toString());
        ContentValues values = new ContentValues(2);
        values.put("title", title.getText().toString());
        values.put("text", text.getText().toString());
        ((MainActivity) getActivity()).notedb.updateNotes(note.getItemId(), values);
    }
}

