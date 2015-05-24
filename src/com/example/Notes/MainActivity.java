package com.example.Notes;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Context self = this;
        final Button addButton = (Button) findViewById(R.id.addButton);
        final Button clearButton = (Button) findViewById(R.id.clearButton);
//        final TextView text = (TextView) findViewById(R.id.text);
        final LinearLayout main_lay = (LinearLayout) findViewById(R.id.main_lay);
        final NoteDb notedb = new NoteDb(this);
//        text.setText(notedb.getNote("title = ?", "hw").printfy());

        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view) {
                Note note = new Note();
                note.title = "hw";
                note.text = this.toString();
                notedb.addNote(note);
//                text.setText(notedb.getNotes());

                NoteItemView noteItem = new NoteItemView(self);
                note = notedb.getNote("title = ?", "hw");
                main_lay.addView(noteItem);
                noteItem.setData(note.title, note.timestamp, note.text, note._id.toString());

            }
        });
        clearButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                notedb.clearNotes();
//                text.setText(notedb.getNotes());

                return false;
            }
        });

    }
}

