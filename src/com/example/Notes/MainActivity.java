package com.example.Notes;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

    public void addNoteToScreen(final Note note) {
        final NoteItemView noteItem = new NoteItemView(this);
        final Context self = this;
        LinearLayout main_lay = (LinearLayout) findViewById(R.id.main_lay);
        main_lay.addView(noteItem);
        noteItem.setData(note.title, note.timestamp, note.text, note._id);
        noteItem.setOnTouchListener(new OnSwipeTouchListener(self) {
            public void onSwipeLeft() {
                super.onSwipeLeft();
                Log.d("swipe", "left " + noteItem.front.getX());
                noteItem.front.animate().translationX(-300);
            }

            public void onSwipeRight() {
                super.onSwipeRight();
                noteItem.front.animate().translationX(0);
                Log.d("swipe", "right " + noteItem.front.getX());

            }

            public void onClick() {
                super.onClick();
                NoteEditDialogView dialog = (new NoteEditDialogView()).newInstance(noteItem);
                dialog.show(getFragmentManager(), "editDialog");
            }
        });
    }

    public void removeNote(NoteItemView noteItem) {
        LinearLayout main_lay = (LinearLayout) findViewById(R.id.main_lay);
        main_lay.removeView(noteItem);
        notedb.deleteNote(noteItem.getItemId());
    }

}