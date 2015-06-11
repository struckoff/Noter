package com.example.Notes;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;



public class MainActivity extends Activity {
    public NoteDb notedb = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        final ImageButton addButton = (ImageButton) findViewById(R.id.addButton);
        final Button clearButton = (Button) findViewById(R.id.clearButton);
        notedb = new NoteDb(this);

        for (Note note : notedb.getNotes()) {
            if (note.state.equals("delete")) {
                removeNote(note);
            } else {
                addNoteToScreen(note);
            }
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                NoteCreateDialogView dialog = (new NoteCreateDialogView()).newInstance();
                dialog.show(getFragmentManager(), "createDialog");
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                notedb.clearNotes();
                LinearLayout main_lay = (LinearLayout) findViewById(R.id.main_lay);
                main_lay.removeAllViews();
            }
        });
    }

    public void addNoteToScreen(final Note note) {
        final NoteItemView noteItem = new NoteItemView(this);
        final Context self = this;
        LinearLayout main_lay = (LinearLayout) findViewById(R.id.main_lay);
        main_lay.addView(noteItem);
        noteItem.setData(note.title, note.timestamp, note.text, note._id);
    }

    public void removeNote(Note note) {
        notedb.deleteNote(note._id);
    }

}