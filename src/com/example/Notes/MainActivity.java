package com.example.Notes;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
    public NoteDb notedb = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Button addButton = (Button) findViewById(R.id.addButton);
        final Button clearButton = (Button) findViewById(R.id.clearButton);
        notedb = new NoteDb(this);

        for (Note note : notedb.getNotes()) {
            addNoteToScreen(note);
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

    public void addNoteToScreen(Note note){
        final NoteItemView noteItem = new NoteItemView(this);
        LinearLayout main_lay = (LinearLayout) findViewById(R.id.main_lay);
        main_lay.addView(noteItem);
        noteItem.setData(note.title, note.timestamp, note.text, note._id);
        noteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoteEditDialogView dialog = (new NoteEditDialogView()).newInstance(noteItem);
                dialog.show(getFragmentManager(), "editDialog");
            }
        });
    }

}

