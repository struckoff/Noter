package com.example.Notes;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NoteItemView extends FrameLayout {
    private TextView title;
    private TextView text;
    private TextView date;

    private String title_raw;
    private String text_raw;
    private Date date_raw;
    private Long id_raw;

    public FrameLayout front = null;
    public FrameLayout back = null;
    public FrameLayout delete_lay = null;

    private ImageButton delete_btn = null;
    private ImageButton share_btn = null;
    private ImageButton restore_btn = null;

    public NoteItemView(Context context){
        super(context);
        init();
    }
    public NoteItemView(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }
    public NoteItemView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init();
    }
    private void init(){
        inflate(getContext(), R.layout.noteitemview, this);
        this.title = (TextView) findViewById(R.id.note_title);
        this.date = (TextView) findViewById(R.id.note_date);
        this.text = (TextView) findViewById(R.id.note_text);
        this.front = (FrameLayout) findViewById(R.id.ll_front);
        this.back = (FrameLayout) findViewById(R.id.ll_back);
        this.delete_lay = (FrameLayout) findViewById(R.id.ll_delete);
        this.delete_btn = (ImageButton) findViewById(R.id.delete_btn);
        this.share_btn = (ImageButton) findViewById(R.id.share_button);
        this.restore_btn = (ImageButton) findViewById(R.id.restore_btn);

        final NoteItemView self = this;

        this.delete_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                self.front.animate().translationX(0);
                self.setState("delete");
            }
        });

        this.restore_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                self.setState("active");
            }
        });

        this.share_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                self.front.animate().translationX(0);
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareTitle = self.getTitle();
                String shareBody = self.getText();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareTitle);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                ((MainActivity) getContext()).startActivity(Intent.createChooser(sharingIntent, "Share via ..."));
            }
        });
    }

    public void setData(String title, Date date, String text, Long id){
        this.setDate(date);
        this.setText(text);
        this.setTitle(title);
        this.id_raw = id;
    }

    public void setTitle(String title){
        this.title.setText(title);
        this.title_raw = title;
    }

    public void setText(String text){
        this.text.setText(text);
        this.text_raw = text;
    }

    public void setDate(Date date){
        this.date.setText(this.dataFormat(date));
        this.date_raw = date;
    }

    public void setState(String state) {
        ContentValues values = new ContentValues(1);
        values.put("state", state);
        ((MainActivity) getContext()).notedb.updateNotes(this.getItemId(), values);

        if (state.equals("delete")) {
            this.delete_lay.setVisibility(View.VISIBLE);
        } else if (state.equals("active")) {
            this.delete_lay.setVisibility(View.GONE);
        }

    }

    public String getTitle(){
        return this.title_raw;
    }

    public String getText(){
        return this.text_raw;
    }
    public Date getDate(){
        return this.date_raw;
    }

    public long getItemId(){
        return this.id_raw;
    }

    private String dataFormat(Date date){
        Calendar cal[] = {Calendar.getInstance(), Calendar.getInstance()};
        cal[0].setTime(new Date());
        cal[1].setTime(date);
        String formater;

        if (cal[0].get(Calendar.YEAR) != cal[1].get(Calendar.YEAR)){
            formater = "dd MMM yyyy";
        }

        else if (cal[0].get(Calendar.MONTH) != cal[1].get(Calendar.MONTH)){
            formater = "dd MMM";
        }
        else if (cal[0].get(Calendar.DAY_OF_MONTH) != cal[1].get(Calendar.DAY_OF_MONTH)){
            formater = "dd MMM";
        }
        else {
            formater = "HH:mm";
        }
        return (new SimpleDateFormat(formater).format(date));
    }
}
