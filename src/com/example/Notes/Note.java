package com.example.Notes;

import java.util.Date;

public class Note {
    public Long _id;
    public String title;
    public String text;
    public String state = "active";
    public Date timestamp = new Date();
}
