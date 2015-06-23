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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
    Long ActiveTag = null;
    Context self_main = null;
    String appName = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.addButton);
        final Button clearButton = (Button) findViewById(R.id.clearButton);
        final ObservableScrollView main_lay_paren = (ObservableScrollView) findViewById(R.id.main_lay_paren);

        appName = getResources().getString(R.string.app_name);
        self_main = this;
        noteItemViews = new ArrayList<>();
        addButton.attachToScrollView(main_lay_paren);
        notedb = new NoteDb(this);
        getSupportActionBar().setTitle(appName + " :ALL");

        for (Note note : notedb.getNotes()) {
            if (note.state.equals("delete")) {
                removeNote(note);
            } else {
                noteItemViews.add(addNoteToScreen(note));
            }
        }
        this.SlidingMenuSpawn();


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
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.show_all_notes_button:
                NoteItemsShow(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    public void NoteItemsShow(){
        LinearLayout main_lay = (LinearLayout) findViewById(R.id.main_lay);
        List<Note> notes = null;
        main_lay.removeAllViews();
        if (ActiveTag == null){
            notes = notedb.getNotes();
            getSupportActionBar().setTitle(appName + " :ALL");
        }
        else {
            notes = notedb.getNotesByTagId(ActiveTag);
            this.noteItemViews.clear();
            getSupportActionBar().setTitle(appName + " :" + notedb.getTag(ActiveTag).text);
        }
        for (Note note : notes) {
            this.noteItemViews.add(addNoteToScreen(note));
        }
    }

    public void NoteItemsShow(Long tag_id){
        ActiveTag = tag_id;
        this.NoteItemsShow();
    }

    private void NoteItemsRefresh(){
        for (NoteItemView note : this.noteItemViews){
            note.tagRefresh();
        }
    }

    public void globalTagListRefresh(){
        final LinearLayout sliderLay = (LinearLayout) this.menu.findViewById(R.id.sliderLay);
        sliderLay.removeAllViews();

        for (final Tag tag : this.notedb.getTags()){
            final FrameLayout tagItem = new FrameLayout(this);
            sliderLay.addView(tagItem);
            tagItem.inflate(this, R.layout.tag_onslider, tagItem);
            final TextView tagText = (TextView) tagItem.findViewById(R.id.globalTagText);
            tagText.setText(tag.text);

            ImageButton deleteTag = (ImageButton) tagItem.findViewById(R.id.globalDeleteTagButton);
            ImageButton editTag = (ImageButton) tagItem.findViewById(R.id.globalEditTagButton);

            tagText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NoteItemsShow(tag._id);
                }
            });

            deleteTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog ad = new AlertDialog.Builder(self_main)
                            .setMessage("Remove this tag from all notes ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    notedb.globalTagDelete(tag._id);
                                    sliderLay.removeView(tagItem);
                                    NoteItemsShow();
                                }
                            })
                            .setNegativeButton("No", null)
                            .create();
                    ad.show();
                }
            });

            editTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout editLay = new LinearLayout(self_main);
                    editLay.inflate(self_main, R.layout.global_tag_edit, editLay);
                    final TextView editText = (TextView)editLay.findViewById(R.id.globalTagEditField);
                    editText.setText(tag.text);

                    AlertDialog ad = new AlertDialog.Builder(self_main)
                            .setView(editLay)
                            .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    notedb.globalTagEdit(tag._id, editText.getText().toString());
                                    tagText.setText(editText.getText().toString());
                                    NoteItemsRefresh();
                                }
                            })
                            .setNegativeButton("Cancel", null)
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
        noteItem.setData(note.title, note.timestamp, note.text, note._id, note.state);
        return noteItem;
    }

    public void removeNote(Note note) {
        notedb.deleteNote(note._id);
    }

}
