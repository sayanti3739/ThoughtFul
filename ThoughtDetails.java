package com.kgec.dg.thoughtsapp;




import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ThoughtDetails implements Serializable {

    private int id;
    private String name;
    private String date;
    private String thought;
    private int likes;
    private int comments;


    public ThoughtDetails() {

        name= "Paul";
        SimpleDateFormat sdf=new SimpleDateFormat("MMM d , yyyy", Locale.getDefault());
        date=sdf.format(new java.util.Date());
        likes=0;
        comments=0;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {

        return date;

    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThought() {
        return thought;
    }

    public void setThought(String thought) {
        this.thought = thought;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }
}


