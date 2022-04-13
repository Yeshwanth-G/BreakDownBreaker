package com.example.breakdownbreaker;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class JobDetails extends AppCompatActivity {
    EditText company,title,location,salary,duration,gender,link;
    String company1,title1,location1,salary1,duration1,gender1,link1,count,posted_by;
FirebaseAuth auth= FirebaseAuth.getInstance();
FirebaseUser user=auth.getCurrentUser();
    Button b;
    FirebaseDatabase f;
    DatabaseReference d,temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jobdetails);
        company=(EditText)findViewById(R.id.company);
        title=(EditText)findViewById(R.id.jobtitle);
        location=(EditText)findViewById(R.id.location);
        salary=(EditText)findViewById(R.id.salary);
        duration=(EditText)findViewById(R.id.duration);
        gender=(EditText)findViewById(R.id.gender);
        link=(EditText)findViewById(R.id.link);
        b=(Button)findViewById(R.id.post_button);
        f=FirebaseDatabase.getInstance();
        d=f.getReference("Jobs");
        temp=f.getReference("count");
        temp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
               count= snapshot.getValue().toString();
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setfields();
                helperclass h=new helperclass(company1,title1,location1,salary1,duration1,gender1,link1,posted_by);
                d.child(count).setValue(h);
                int c=Integer.parseInt(count);
                c++;
                temp.setValue(c);
            }
        });
    }

    private void setfields() {
        company1=company.getText().toString();
        title1=title.getText().toString();
        location1=location.getText().toString();
        salary1=salary.getText().toString();
        duration1=duration.getText().toString();
        gender1=gender.getText().toString();
        link1=link.getText().toString();
        posted_by=user.getEmail().toString();
    }
}