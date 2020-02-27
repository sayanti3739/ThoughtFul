package com.kgec.thoughtful;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentListActivity extends AppCompatActivity {

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private ArrayAdapter<String> aa;
    private CommentAdapter da;
    private ArrayList<Comment> clist = new ArrayList<>();
    private ListView lvcomment;
    private ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lvcomment = findViewById(R.id.lvcomment);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CommentListActivity.this,AddCommentActivity.class);
                startActivityForResult(i,400);
            }
        });
        firestore.collection("commentcollection").orderBy("cdate").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e!=null){
                    Toast.makeText(CommentListActivity.this, "Some Error Happened!", Toast.LENGTH_SHORT).show();
                }
                else {
                   // clist.clear();
                    List<DocumentSnapshot> documents =  queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot document:documents){
                        String commenttext =  document.getString("commenttext");
                        Date createdate = document.getDate("cdate");
                        Comment comment = new Comment(commenttext,"USERNAME",String.valueOf(createdate));
                        clist.add(comment);
                        //list.add(commentS);
                    }
                    da.notifyDataSetChanged();
                }
            }
        });
      //aa = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list);
        da = new CommentAdapter(this,clist);
        lvcomment.setAdapter(da);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==400){
            if(resultCode== AddCommentActivity.RESULT_OK){
                Comment comment = (Comment) data.getSerializableExtra("NEWCOMMENT");
                clist.add(comment);
                da.notifyDataSetChanged();
            };
        }
    }
}
