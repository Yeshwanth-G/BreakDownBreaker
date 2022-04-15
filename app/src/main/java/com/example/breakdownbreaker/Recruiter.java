package com.example.breakdownbreaker;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class Recruiter extends AppCompatActivity implements clickinterface{
EditText et;
FloatingActionButton b;
ArrayList<helperclass>posted_jobs;
FirebaseDatabase f;
adapter myadapter;
RecyclerView rc;
FirebaseAuth auth=FirebaseAuth.getInstance();
FirebaseUser user=auth.getCurrentUser();
DatabaseReference d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruiter);
        b=(FloatingActionButton)findViewById(R.id.postbutton);
        rc=(RecyclerView)findViewById(R.id.recyclerview);
        rc.setHasFixedSize(true);
        posted_jobs=new ArrayList<>();
        myadapter=new adapter(this,posted_jobs,this);
        rc.setLayoutManager(new LinearLayoutManager(this));
        rc.setAdapter(myadapter);
        f=FirebaseDatabase.getInstance();
        d=f.getReference("Jobs");
        d.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(!posted_jobs.isEmpty())posted_jobs.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    helperclass h=dataSnapshot.getValue(helperclass.class);
                    if(h.getPosted_by().equals(user.getEmail()))
                    posted_jobs.add(h);
                }
                Collections.reverse(posted_jobs);
                myadapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(Recruiter.this,JobDetails.class);
                startActivity(it);

            }
        });

    }

    @Override
    public void onItemClick(int position) {
        Intent it=new Intent(Recruiter.this, shop_details.class);
        startActivity(it);
    }
}