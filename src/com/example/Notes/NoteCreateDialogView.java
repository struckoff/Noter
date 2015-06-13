package com.example.Notes;


public class NoteCreateDialogView extends NoteDialogView {

    public void Positive() {
        Note note = new Note();
        note.title = title.getText().toString();
        note.text = text.getText().toString();
        if (!note.text.isEmpty() || !note.title.isEmpty()) {
            note._id = ((MainActivity) getActivity()).notedb.addNote(note);
            ((MainActivity) getActivity()).addNoteToScreen(note);
        }
    }
}
