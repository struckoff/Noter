package com.example.Notes;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class tagItemView  extends TextView {

    public tagItemView(Context context) {
        super(context);
    }

    public tagItemView(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }
    public tagItemView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init();
    }

    private void init(){

    }
}
