package com.example.breakdownbreaker;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class JobDetails extends AppCompatActivity {
    EditText name,location,contact,working_hours,services;
    String name1,contact1,location1,woking1,services1,count,posted_by;
FirebaseAuth auth= FirebaseAuth.getInstance();
TextView txt;
FirebaseUser user=auth.getCurrentUser();
    Button b;
    FirebaseDatabase f;
    DatabaseReference d,temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        name=(EditText)findViewById(R.id.name);
        contact=(EditText)findViewById(R.id.contact);
        location=(EditText)findViewById(R.id.location);
        working_hours=(EditText)findViewById(R.id.working_hours);
        services=(EditText)findViewById(R.id.services);
        b=(Button)findViewById(R.id.materialButton);
        txt=(TextView)findViewById(R.id.messege);
        txt.setVisibility(View.INVISIBLE);
        f=FirebaseDatabase.getInstance();
        d=f.getReference("shops");
        temp=f.getReference("count");
        temp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                count=snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setfields();
                container h=new container(name1,contact1,location1,services1,woking1,user.getEmail());
                d.child(count).setValue(h);
                int c=Integer.parseInt(count);
                c++;
                temp.setValue(c);
                txt.setText("Adding...");
                txt.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        txt.setText("Added Go Back and check...");
                    }
                }, 4000);
                Toast.makeText(JobDetails.this,"Successfully Added GO back and Check",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setfields() {
        name1=name.getText().toString();
        location1=location.getText().toString();
        contact1=contact.getText().toString();
        woking1=working_hours.getText().toString();
        services1=services.getText().toString();
    }
}