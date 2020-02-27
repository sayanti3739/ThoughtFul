package com.kgec.dg.thoughtsapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lvthoughtdisp;

    private ArrayList<ThoughtDetails> list=new ArrayList<>();
    private ThoughtAdapter ta=null;

    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lvthoughtdisp=findViewById(R.id.lvthoughtdisp);
        ta=new ThoughtAdapter(this,list);
        lvthoughtdisp.setAdapter(ta);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=(new Intent(MainActivity.this,AddThoughtActivity.class));
                startActivityForResult(i,900);
                //startActivity(i);

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); //end of listener

        lvthoughtdisp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //ThoughtDetails t=new ThoughtDetails();
                //t.setId((int)id);
                /*TextView tvtid=view.findViewById(R.id.thoughtid);
                String tid=tvtid.getText().toString();
                Toast.makeText(MainActivity.this, "You clicked on "+tid, Toast.LENGTH_SHORT).show();*/


            }
        });

        registerForContextMenu(lvthoughtdisp);

        firestore.collection("thoughtlist").orderBy("cdate", Query.Direction.DESCENDING).addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot Snapshots, @Nullable FirebaseFirestoreException e) {

                if(e!=null){
                    Toast.makeText(MainActivity.this, "Some error"+e.getMessage(), Toast.LENGTH_LONG).show();
                }
                else{
                    list.clear();
                    List<DocumentSnapshot> documents= Snapshots.getDocuments();
                    for(DocumentSnapshot document:documents){
                        ThoughtDetails td=new ThoughtDetails();
                        td.setThought(document.getString("thoughttext"));
                        td.setDate(document.getString("cdate"));
                        list.add(td);
                    }
                    ta.notifyDataSetChanged();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==900){
            if(resultCode== Activity.RESULT_OK){
                ThoughtDetails t=(ThoughtDetails) data.getSerializableExtra("NEWTHOUGHT");
                list.add(t);
                ta.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
