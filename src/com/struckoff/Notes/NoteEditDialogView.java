package com.struckoff.Notes;

/**
 * Note edit dialog
 */

import android.content.ContentValues;
import android.widget.FrameLayout;
import android.widget.TextView;

public class NoteEditDialogView extends NoteDialogView {

    public NoteEditDialogView(NoteItemView self_noteitem) {
        super(self_noteitem);
    }

    public void Create(){
        title.setText(this.note.getTitle());
        text.setText(this.note.getText());
        for (Tag tag : notedb.getTags(note.getItemId())){
            this.addTagToView(tag.text);
        }
    }

    public void Positive(){
        note.setTitle(title.getText().toString());
        note.setText(text.getText().toString());
        ContentValues values = new ContentValues(2);
        values.put("title", title.getText().toString());
        values.put("text", text.getText().toString());
        ((MainActivity) getActivity()).notedb.updateNotes(note.getItemId(), values);
        addTag(tag_text.getText().toString());
        for (String tag_body : this.tags){
            notedb.addTag(note.getItemId(), tag_body);
            note.addTag(tag_body);
        }
        for (FrameLayout tag_lay : tagsToDelete) {
            TextView tag_view = (TextView) tag_lay.findViewById(R.id.tagItem);
            notedb.deleteTag(note.getItemId(), tag_view.getText().toString());
            note.tagRefresh();
        }
    }
}

