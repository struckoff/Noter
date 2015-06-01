package com.example.Notes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.TextView;

public class NoteEditDialogView extends DialogFragment {

    private static NoteItemView note = null;

    public static NoteEditDialogView newInstance(NoteItemView noteItem) {
        NoteEditDialogView frag = new NoteEditDialogView();
        note = noteItem;
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View main_lay = getActivity().getLayoutInflater().inflate(R.layout.notedialog, null);
        final TextView title = (TextView) main_lay.findViewById(R.id.edit_note_title);
        final TextView text = (TextView) main_lay.findViewById(R.id.edit_note_text);

        title.setText(note.getTitle());
        text.setText(note.getText());

        return new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.NoteDialogStyle))
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                note.setTitle(title.getText().toString());
                                note.setText(text.getText().toString());
                                ContentValues values = new ContentValues(2);
                                values.put("title", title.getText().toString());
                                values.put("text", text.getText().toString());
                                ((MainActivity) getActivity()).notedb.updateNotes(note.getItemId(), values);
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }
                )
                .setView(main_lay)
                .create();
    }
}

