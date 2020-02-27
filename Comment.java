package com.kgec.thoughtful;

import android.widget.ImageView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Comment implements Serializable {
    private String commenttext;
    private String username;
    private String createDate;
    private ImageView profile;

    public Comment() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        createDate = sdf.format(new java.util.Date());
    }

    public Comment(String commenttext, String username, String createDate) {
        this.commenttext = commenttext;
        this.username = username;
        this.createDate = createDate;
    }

    public Comment(String commenttext, String username, ImageView profile) {
        this.commenttext = commenttext;
        this.username = username;
        this.profile = profile;
        SimpleDateFormat sdf  = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        createDate = sdf.format(new java.util.Date());
    }


    public String getCommenttext() {
        return commenttext;
    }

    public void setCommenttext(String commenttext) {
        this.commenttext = commenttext;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public ImageView getProfile() {
        return profile;
    }

    public void setProfile(ImageView profile) {
        this.profile = profile;
    }
}
