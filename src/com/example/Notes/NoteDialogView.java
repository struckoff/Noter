package com.example.Notes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.TextView;

public class NoteDialogView extends DialogFragment{

    public NoteItemView note = null;
    protected TextView title = null;
    protected TextView text = null;

    private NoteDialogView self_notedialogview = this;

    public NoteDialogView () {}

    public NoteDialogView (NoteItemView noteItem) {
        note = noteItem;
    }

    public void Create() {

    }

    public void Positive() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View main_lay = getActivity().getLayoutInflater().inflate(R.layout.notedialog, null);
        this.title = (TextView) main_lay.findViewById(R.id.edit_note_title);
        this.text = (TextView) main_lay.findViewById(R.id.edit_note_text);

        self_notedialogview.Create();

        return new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.NoteDialogStyle))
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Log.d("dialog", "Positive");
                                self_notedialogview.Positive();
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Log.d("dialog", "Negative");
                            }
                        }
                )
                .setView(main_lay)
                .create();

    }
}

