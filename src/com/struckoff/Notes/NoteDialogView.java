package com.struckoff.Notes;

/**
 * Super class for create and edit dialogs
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NoteDialogView extends DialogFragment{

    public NoteItemView note = null;
    protected TextView title = null;
    protected TextView text = null;
    protected TextView tag_text = null;
    protected LinearLayout tagLay = null;
    protected Button addTagButton = null;
    protected Button deleteTagButton = null;
    protected List<String> tags = new ArrayList<>();
    protected List<FrameLayout> tagsToDelete = new ArrayList<>();
    protected View main_lay;
    protected NoteDb notedb = null;

    private NoteDialogView self_notedialogview = this;

    public NoteDialogView () {}

    public NoteDialogView (NoteItemView noteItem) {
        note = noteItem;
    }

    public void Create() {}

    public void Positive() {}

    public void addTag(String tag_body){
        TextUtils.SimpleStringSplitter tag_split = new TextUtils.SimpleStringSplitter(',');
        tag_split.setString(tag_body);

        for (String tag : TextUtils.split(tag_body, "( )*,( )*")){
            if (!tag.isEmpty()) {
                addTagToView(tag);
                self_notedialogview.tags.add(tag);
            }
        }
    }

    public void addTagToView(String tag_body){
        final FrameLayout lay = new FrameLayout(main_lay.getContext());
        lay.inflate(self_notedialogview.main_lay.getContext(), R.layout.tag_ondialog, lay);
        final TextView tagItem = (TextView) lay.findViewById(R.id.tagItem);
        tagLay.addView(lay);
        tagItem.setText(tag_body);
        tagItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (self_notedialogview.tagsToDelete.contains(lay)){
                    view.setBackgroundResource(R.drawable.rounded_corner_dialog);
                    self_notedialogview.tagsToDelete.remove(lay);
                    if (self_notedialogview.tagsToDelete.isEmpty()){
                        self_notedialogview.addTagButton.setVisibility(View.VISIBLE);
                        self_notedialogview.deleteTagButton.setVisibility(View.GONE);
                    }
                }
                else{
                    view.setBackgroundResource(R.drawable.rounded_corner_dialog_red);
                    self_notedialogview.addTagButton.setVisibility(View.GONE);
                    self_notedialogview.deleteTagButton.setVisibility(View.VISIBLE);
                    self_notedialogview.tagsToDelete.add(lay);
                }
            }
        });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        self_notedialogview.main_lay = getActivity().getLayoutInflater().inflate(R.layout.notedialog, null);
        self_notedialogview.title = (TextView) main_lay.findViewById(R.id.edit_note_title);
        self_notedialogview.text = (TextView) main_lay.findViewById(R.id.edit_note_text);
        self_notedialogview.tag_text = (TextView) main_lay.findViewById(R.id.add_tag_text);
        self_notedialogview.tagLay = (LinearLayout)main_lay.findViewById(R.id.tagLay);
        self_notedialogview.addTagButton = (Button)main_lay.findViewById(R.id.addTagButton);
        self_notedialogview.deleteTagButton = (Button)main_lay.findViewById(R.id.deleteTagButton);
        self_notedialogview.notedb = ((MainActivity)getActivity()).notedb;


        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                self_notedialogview.tag_text.setVisibility(View.VISIBLE);
                self_notedialogview.tag_text.setFocusableInTouchMode(true);
                self_notedialogview.tag_text.requestFocus();
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(self_notedialogview.tag_text, 0);
            }
        });

        deleteTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (FrameLayout tag_lay : self_notedialogview.tagsToDelete){
                    self_notedialogview.tagLay.removeView(tag_lay);
                    view.setVisibility(View.GONE);
                    self_notedialogview.addTagButton.setVisibility(View.VISIBLE);
                }
            }
        });

        self_notedialogview.tag_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                addTag(textView.getText().toString());
                textView.setVisibility(View.GONE);
                textView.setText("");
                return false;
            }
        });

        self_notedialogview.Create();

        AlertDialog ad = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.NoteDialogStyle))
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                self_notedialogview.Positive();
                                ((MainActivity)getActivity()).globalTagListRefresh();
                            }
                        }
                )
                .setNegativeButton("Cancel", null)
                .setView(main_lay)
                .create();

        ad.show();
        ad.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        return ad;
    }
}
