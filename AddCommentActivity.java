package com.kgec.thoughtful;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddCommentActivity extends AppCompatActivity {

    private TextView editcommenttext;
    private Button btnaddcomment;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        editcommenttext = findViewById(R.id.editcommenttext);
        btnaddcomment = findViewById(R.id.btnaddcomment);

        btnaddcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionReference collref =  firestore.collection("commentcollection");
                Map<String, Object> map= new HashMap<>();
                map.put("commenttext", editcommenttext.getText().toString());
                map.put("cdate", new Timestamp(new java.util.Date()));

                firestore.collection("commentcollection").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddCommentActivity.this, "Your Comment is added!", Toast.LENGTH_SHORT).show();
                        Comment comment = new Comment(editcommenttext.getText().toString(),"USERNAME", String.valueOf(new Timestamp(new java.util.Date())));
                        Intent i = new Intent();
                        i.putExtra("NEWCOMMENT", comment);
                        setResult(Activity.RESULT_OK,i);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddCommentActivity.this, "SOMETHING WENT WRONG!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}
