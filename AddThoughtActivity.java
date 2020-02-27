package com.kgec.dg.thoughtsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddThoughtActivity extends AppCompatActivity {

    TextView tvcurrentdate;
    EditText etthought;
    Button btnadd;


    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_thought);

        tvcurrentdate=findViewById(R.id.tvcurrentdate);
        etthought=findViewById(R.id.etenterthought);
        btnadd=findViewById(R.id.btnaddthought);



        ThoughtDetails t=new ThoughtDetails();
        tvcurrentdate.setText(t.getDate());

        /*firebase.auth().onAuthStateChanged(function(user) {
            if (user) {
                // User is signed in.
                var displayName = user.displayName;
                var email = user.email;
                var emailVerified = user.emailVerified;
                var photoURL = user.photoURL;
                var isAnonymous = user.isAnonymous;
                var uid = user.uid;
                var providerData = user.providerData;
                // ...
            } else {
                // User is signed out.
                // ...
            }
        });*/

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CollectionReference collectionReference=firestore.collection("thoughtlist");
                Map<String,Object> map=new HashMap<>();

                map.put("thoughttext",etthought.getText().toString());
                map.put("cdate",tvcurrentdate.getText().toString());
                //map.put("username",)

                firestore.collection("thoughtlist").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddThoughtActivity.this, "Data Added", Toast.LENGTH_LONG).show();
                        Intent ri=new Intent();
                        ThoughtDetails t=new ThoughtDetails();
                        tvcurrentdate.setText(t.getDate());
                        t.setThought(etthought.getText().toString());
                        ri.putExtra("NEWTHOUGHT", t);
                        setResult(Activity.RESULT_OK,ri);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddThoughtActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                });





            }
        }); //end of btnadd


    }
}
