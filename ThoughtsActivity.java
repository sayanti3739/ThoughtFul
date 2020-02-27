package com.example.thoughtful;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class ThoughtsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thoughts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lvthoughtdisp = findViewById(R.id.lvthoughtdisp);
        ta = new ThoughtAdapter(this, list);
        lvthoughtdisp.setAdapter(ta);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = (new Intent(MainActivity.this, AddThoughtActivity.class));
                startActivityForResult(i, 900);
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

                if (e != null) {
                    Toast.makeText(MainActivity.this, "Some error" + e.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    list.clear();
                    List<DocumentSnapshot> documents = Snapshots.getDocuments();
                    for (DocumentSnapshot document : documents) {
                        ThoughtDetails td = new ThoughtDetails();
                        td.setThought(document.getString("thoughttext"));
                        td.setDate(document.getString("cdate"));
                        list.add(td);
                    }
                    ta.notifyDataSetChanged();
                }

            }
        });
    }

}
