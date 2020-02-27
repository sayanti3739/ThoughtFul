package com.kgec.dg.thoughtsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ThoughtAdapter extends ArrayAdapter<ThoughtDetails> {

    private Context context;
    private ArrayList<ThoughtDetails> list;
    CheckBox cb;

    public ThoughtAdapter(@NonNull android.content.Context context, ArrayList<ThoughtDetails> list) {
        super(context, R.layout.thought_row,list);
        this.context=context;
        this.list=list;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView=inflater.inflate(R.layout.thought_row, parent,false);
        }
        ThoughtDetails t=list.get(position);

        TextView name=convertView.findViewById(R.id.tvname);
        TextView date=convertView.findViewById(R.id.tvdate);
        TextView thought=convertView.findViewById(R.id.tvthought);
        TextView id=convertView.findViewById(R.id.thoughtid);
        TextView likes=convertView.findViewById(R.id.tvlikecount);
        TextView comments=convertView.findViewById(R.id.tvcommentcount);
        cb = convertView.findViewById(R.id.cblike);
        
        if(cb.isChecked())
        {
            cb.setChecked(true);
            x = Integer.parseInt(likes.getText().toString());
            x = x+1;

        }



        name.setText(t.getName());
        date.setText(t.getDate());
        thought.setText(t.getThought());
        id.setText(String.valueOf(t.getId()));
        likes.setText(String.valueOf(x));
        comments.setText(String.valueOf(t.getComments()));



        return convertView;
    }
}
