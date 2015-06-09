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

public class NoteCreateDialogView extends DialogFragment {

    public static NoteCreateDialogView newInstance() {
        NoteCreateDialogView frag = new NoteCreateDialogView();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View main_lay = getActivity().getLayoutInflater().inflate(R.layout.notedialog, null);
        final TextView title = (TextView) main_lay.findViewById(R.id.edit_note_title);
        final TextView text = (TextView) main_lay.findViewById(R.id.edit_note_text);

        return new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.NoteDialogStyle))
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Note note = new Note();
                                note.title = title.getText().toString();
                                note.text = text.getText().toString();
                                if (!note.text.isEmpty() || !note.title.isEmpty()){
                                    note._id = ((MainActivity) getActivity()).notedb.addNote(note);
                                    ((MainActivity) getActivity()).addNoteToScreen(note);
                                }
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
