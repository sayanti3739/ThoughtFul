package com.example.thoughtful;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText name, email, password, cnfpassword;
    Button regbtn;
    RadioButton male, female, nb;
    RadioGroup gender;

    FirebaseAuth firebaseobj;
    FirebaseUser user;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseobj =FirebaseAuth.getInstance();

        name = findViewById(R.id.regname);
        email = findViewById(R.id.regemail);
        password = findViewById(R.id.regpass);
        cnfpassword = findViewById(R.id.regcnfpass);
        regbtn = findViewById(R.id.regbtn);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        nb = findViewById(R.id.nonbinary);
        gender = findViewById(R.id.gender);

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(password.getText().toString().equals(cnfpassword.getText().toString()))){
                    Toast.makeText(RegisterActivity.this, "PASSWORDS DO NOT MATCH", Toast.LENGTH_SHORT).show();
                    Log.i("TRUTH", "PASSWORDS DO NOT MATCH");
                    password.setText("");
                    cnfpassword.setText("");
                }
                else{
                    final String emails = email.getText().toString();
                    final String passwords = password.getText().toString();
                    String sex2 = null;
                    int sid = gender.getCheckedRadioButtonId();
                    Log.i("TRUTH", "sid : "+sid);
                    if(sid == 2131165335)
                        sex2 = "Male";
                    else if(sid == 2131165305)
                        sex2= "Female";
                    else if(sid == 2131165343)
                        sex2 = "Non-Binary";
                    final String sex = sex2;
                    firebaseobj.createUserWithEmailAndPassword(emails,passwords)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Log.i("TRUTH", "Created");
                                    user = FirebaseAuth.getInstance().getCurrentUser();
                                    /*sid : 2131165335
                                      sid : 2131165305
                                      sid : 2131165343*/
                                    if(user!=null){
                                        //firebaseobj.sendSignInLinkToEmail()
                                        //Toast.makeText(RegisterActivity.this, "Registered Successfully!", Toast.LENGTH_LONG).show();
                                        CollectionReference collref =  firestore.collection("RegisteredUsers");
                                        Map<String, Object> map= new HashMap<>();
                                        map.put("Name",name.getText().toString());
                                        map.put("Email", emails);
                                        map.put("Gender", sex);

                                        Task<DocumentReference> t1 =  collref.add(map);
                                        Task<DocumentReference> t2 = t1.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.i("TRUTH", "DATA ADDED");
                                                Toast.makeText(RegisterActivity.this, "Registered Successfully!", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        t2.addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.i("TRUTH", "ERROR : "+e.getMessage());
                                                Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i("TRUTH", "Error : "+e.getMessage());
                                    Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                }
                //Intent i = new Intent(RegisterActivity.this, _.class);
            }
        });

    }
}
