package com.struckoff.Notes;

/**
 * Main Activity (Main screen if app) with notes list and add button
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.*;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public NoteDb notedb = null;
    private SlidingMenu menu = null;
    List<NoteItemView> noteItemViews = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.addButton);
        final Button clearButton = (Button) findViewById(R.id.clearButton);
        final ObservableScrollView main_lay_paren = (ObservableScrollView) findViewById(R.id.main_lay_paren);
        noteItemViews = new ArrayList<>();
        addButton.attachToScrollView(main_lay_paren);
        notedb = new NoteDb(this);

        this.SlidingMenuSpawn();


        for (Note note : notedb.getNotes()) {
            if (note.state.equals("delete")) {
                removeNote(note);
            } else {
                noteItemViews.add(addNoteToScreen(note));
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
                notedb.clearTags();
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

    private void SlidingMenuSpawn(){
        this.menu = new SlidingMenu(this);
        this.menu.setMode(SlidingMenu.LEFT);
        this.menu.setFadeDegree(0.35f);
        this.menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        this.menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        this.menu.setMenu(R.layout.slider_menu);

        this.globalTagListRefresh();
    }

    private void NoteItemsRefresh(){
        for (NoteItemView note : this.noteItemViews){
            note.tagRefresh();
        }
    }

    public void globalTagListRefresh(){
        LinearLayout sliderLay = (LinearLayout) this.menu.findViewById(R.id.sliderLay);
        sliderLay.removeAllViews();

        for (final Tag tag : this.notedb.getTags()){
            FrameLayout tagItem = new FrameLayout(this);
            sliderLay.addView(tagItem);
            tagItem.inflate(this, R.layout.tag_onslider, tagItem);
            TextView tagText = (TextView) tagItem.findViewById(R.id.globalTagText);
            tagText.setText(tag.text);

            ImageButton deleteTag = (ImageButton) tagItem.findViewById(R.id.globalDeleteTagButton);
            ImageButton editTag = (ImageButton) tagItem.findViewById(R.id.globalEditTagButton);
            final Context self_main = this;

            deleteTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog ad = new AlertDialog.Builder(self_main)
                            .setMessage("Remove this tag from all notes ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    notedb.globalTagDelete(tag._id);
                                    globalTagListRefresh();
                                    NoteItemsRefresh();
                                }
                            })
                            .setNegativeButton("No", null)
                            .create();
                    ad.show();

                }
            });
        }


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
