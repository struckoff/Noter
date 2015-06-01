package com.example.Notes;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Date;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Button addButton = (Button) findViewById(R.id.addButton);
        final Button clearButton = (Button) findViewById(R.id.clearButton);
        final NoteDb notedb = new NoteDb(this);

        for (Note note : notedb.getNotes()) {
            addNoteToScreen(note);
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Note note = new Note();
                note.title = (new Date()).toString();
                note.text = this.toString();
                notedb.addNote(note);
                addNoteToScreen(note);
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

    private void addNoteToScreen(Note note){
        final NoteItemView noteItem = new NoteItemView(this);
        LinearLayout main_lay = (LinearLayout) findViewById(R.id.main_lay);
        main_lay.addView(noteItem);
        noteItem.setData(note.title, note.timestamp, note.text, note._id.toString());
        noteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoteDialogView dialog = (new NoteDialogView()).newInstance(noteItem);
                dialog.show(getFragmentManager(), "dialog");
            }
        });
    }

}

