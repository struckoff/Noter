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
//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NoteItemWidget);
//        this.setBackgroundColor(a.getColor(R.styleable.NoteItemWidget_clr, 0));
        init();
    }
    private void init(){
        inflate(getContext(), R.layout.noteitemview, this);
        this.title = (TextView) findViewById(R.id.note_title);
        this.date = (TextView) findViewById(R.id.note_date);
        this.text = (TextView) findViewById(R.id.note_text);
    }

    public void setData(String title, Date date, String text, String id){
//        this.date.setText(Integer.toString(date.compareTo(new Date())));
        this.date.setText(this.dataFromat(date));
        this.title.setText(title);
        this.text.setText(text);
    }

    private String dataFromat(Date date){
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
