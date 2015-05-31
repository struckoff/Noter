package com.example.Notes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

public class NoteDialogView extends DialogFragment {

    public static NoteDialogView newInstance() {
        NoteDialogView frag = new NoteDialogView();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setPositiveButton("Yep",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
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
                .create();
    }

    private void doNegativeClick() {
        Log.d("dialog", "Neg");
    }

    private void doPositiveClick() {
        Log.d("dialog", "Pos");
    }

}
