package com.struckoff.Notes;

/**
 * Create (add) Note dialog
 */

public class NoteCreateDialogView extends NoteDialogView {

    public void Positive() {
        Note note = new Note();
        note.title = title.getText().toString();
        note.text = text.getText().toString();
        if (!note.text.isEmpty() || !note.title.isEmpty()) {
            note._id = notedb.addNote(note);
            NoteItemView note_view = ((MainActivity) getActivity()).addNoteToScreen(note);
            addTag(tag_text.getText().toString());
            for (String tag_body : this.tags){
                notedb.addTag(note._id, tag_body);
                note_view.addTag(tag_body);
            }
        }
    }
}
