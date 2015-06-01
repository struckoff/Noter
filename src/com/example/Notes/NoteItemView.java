package com.example.Notes;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NoteItemView extends FrameLayout {
    private TextView title;
    private TextView text;
    private TextView date;
    private TextView id;
    private Note note;

    private String title_raw;
    private String text_raw;
    private Date date_raw;
    private int id_raw;

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
    }

    public void setData(String title, Date date, String text, String id){
        this.setDate(date);
        this.setText(text);
        this.setTitle(title);
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

    public String getTitle(){
        return this.title_raw;
    }

    public String getText(){
        return this.text_raw;
    }
    public Date getDate(){
        return this.date_raw;
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
