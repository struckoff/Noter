package com.example.Notes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class NoteDialogView extends DialogFragment {

    private static NoteItemView note = null;

    public static NoteDialogView newInstance() {
        NoteDialogView frag = new NoteDialogView();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    public static NoteDialogView newInstance(NoteItemView noteItem) {
        NoteDialogView frag = new NoteDialogView();
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

        return new AlertDialog.Builder(getActivity())
                .setPositiveButton("Yep",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Log.d("CB", title.getText().toString());
                                doPositiveClick();
                            }
                        }
                )
                .setNegativeButton("Nope",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                doNegativeClick();
                            }
                        }
                )
                .setView(main_lay)
                .create();
    }

    private void doNegativeClick() {
        Log.d("dialog", "Neg");
    }

    private void doPositiveClick() {
        Log.d("dialog", "Pos");
    }

}
