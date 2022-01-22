package com.example.googlesignin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class chooser extends AppCompatActivity {
CardView recruiter,seeker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooser);
        recruiter=(CardView)findViewById(R.id.recruiter_card);
        seeker=(CardView)findViewById(R.id.seeker_card);
        recruiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(chooser.this,Recruiter.class);
                startActivity(it);
            }
        });
seeker.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent it=new Intent(chooser.this,seeker.class);
        startActivity(it);
    }
});
    }
}