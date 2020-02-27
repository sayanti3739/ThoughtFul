package com.kgec.thoughtful;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CommentAdapter extends ArrayAdapter<Comment> {
    private Context context;
    private ArrayList<Comment> list;

    public CommentAdapter(@NonNull Context context, ArrayList<Comment> list) {
        super(context, R.layout.listview_commentlist_row, list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater =  LayoutInflater.from(context) ;
            convertView = inflater.inflate(R.layout.listview_commentlist_row,parent,false);
        }
        Comment c = list.get(position);
        ImageView img = convertView.findViewById(R.id.ivprofile);
        TextView user = convertView.findViewById(R.id.tvuser);
        TextView date = convertView.findViewById(R.id.tvdate);
        TextView ctext = convertView.findViewById(R.id.tvcommenttext);
        user.setText(c.getUsername());
        date.setText(c.getCreateDate());
        ctext.setText(c.getCommenttext());

        return convertView;
    }
}
