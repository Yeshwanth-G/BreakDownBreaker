package com.example.breakdownbreaker;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
public class seeker extends AppCompatActivity implements clickinterface{
FirebaseDatabase f;
DatabaseReference d;
RecyclerView rc;
adapter myadapter;
helperclass h;
ArrayList<helperclass>a;
ArrayList<helperclass>p;
Button sout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker);
        rc=(RecyclerView)findViewById(R.id.recyclerview);
        sout=(Button)findViewById(R.id.signout_btn);
        rc.setHasFixedSize(true);
        f=FirebaseDatabase.getInstance();
        d=f.getReference("Jobs");
        a=new ArrayList<>();
        rc.setLayoutManager(new LinearLayoutManager(this));
        myadapter=new adapter(this,a,this);
        rc.setAdapter(myadapter);
        sout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent it=new Intent(seeker.this,MainActivity.class);
                startActivity(it);
            }
        });
        d.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
             for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                 helperclass h=dataSnapshot.getValue(helperclass.class);
                 a.add(h);
             }
                Collections.reverse(a);
                myadapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
    @Override
    public void onItemClick(int position) {
        Intent it=new Intent(seeker.this, shop_details.class);
        startActivity(it);
    }
}