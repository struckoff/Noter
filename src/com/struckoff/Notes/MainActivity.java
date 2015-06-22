package com.struckoff.Notes;

/**
 * Main Activity (Main screen if app) with notes list and add button
 */

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;


public class MainActivity extends AppCompatActivity {
    public NoteDb notedb = null;
    private SlidingMenu menu = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.addButton);
        final Button clearButton = (Button) findViewById(R.id.clearButton);
        final ObservableScrollView main_lay_paren = (ObservableScrollView) findViewById(R.id.main_lay_paren);
        addButton.attachToScrollView(main_lay_paren);

        this.menu = new SlidingMenu(this);
        final int slidingmenuWidth = getResources().getDimensionPixelSize(R.dimen.slidingmenu_width);
        menu.setMode(SlidingMenu.LEFT);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setMenu(R.layout.slider_menu);

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
                NoteCreateDialogView dialog = new NoteCreateDialogView();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
    }

    public NoteItemView addNoteToScreen(final Note note) {
        final NoteItemView noteItem = new NoteItemView(this);
        LinearLayout main_lay = (LinearLayout) findViewById(R.id.main_lay);
        main_lay.addView(noteItem);
        noteItem.setData(note.title, note.timestamp, note.text, note._id);
        return noteItem;
    }

    public void removeNote(Note note) {
        notedb.deleteNote(note._id);
    }
}
