package com.example.Notes;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Date;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final FragmentManager fm = getFragmentManager();
        final Context self = this;
        final Button addButton = (Button) findViewById(R.id.addButton);
        final Button clearButton = (Button) findViewById(R.id.clearButton);
        final LinearLayout main_lay = (LinearLayout) findViewById(R.id.main_lay);
        final NoteDb notedb = new NoteDb(this);

        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view) {
                Note newNote = new Note();
                newNote.title = (new Date()).toString();
                newNote.text = this.toString();
                notedb.addNote(newNote);

                for (Note note:notedb.getNotes()) {
                    final NoteItemView noteItem = new NoteItemView(self);
//                    note = notedb.getNote("title = ?", "hw");
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
        });
        clearButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                notedb.clearNotes();
                return false;
            }
        });

    }
}

