package com.example.breakdownbreaker;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
public class shop_details extends AppCompatActivity {
    TextView name,location,services,working_hours,contact;
    Button btn;
    FloatingActionButton fbtn;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseDatabase f;
    DatabaseReference d;
    DataSnapshot ds;
    boolean bl;
    Toast t;

    FirebaseUser user=auth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);
        Intent it = getIntent();
        container c1=(container) it.getExtras().getSerializable("mycontainer");
        name=(TextView)findViewById(R.id.name);
        location=(TextView)findViewById(R.id.location);
        services=(TextView)findViewById(R.id.services);
        fbtn=(FloatingActionButton)findViewById(R.id.deletebtn);
        if(user.getEmail().equals(c1.posted_by)){
            fbtn.setVisibility(View.VISIBLE);
        }
        else fbtn.setVisibility(View.INVISIBLE);
        f=FirebaseDatabase.getInstance();
        d=f.getReference("shops");
        d.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    container h=dataSnapshot.getValue(container.class);
                if(h.getName().equals(c1.getName())&&h.getLocation().equals(c1.getLocation())){
                    ds=dataSnapshot;

                }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        fbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bl==false){
                    t= Toast.makeText(shop_details.this,"Press Again to delete",Toast.LENGTH_SHORT);
                   t.show();
                    bl=true;
                }
                else{
               if(ds!=null){
                   if(t!=null)t.cancel();
                   Toast.makeText(shop_details.this,"Removed Sucessfully please wait..",Toast.LENGTH_SHORT).show();

                   d.child(ds.getKey()).removeValue();

                   Handler handler = new Handler();
                   handler.postDelayed(new Runnable() {
                       public void run() {
                           Intent it=new Intent(shop_details.this,MapActivity.class);
                           startActivity(it);
                       }
                   }, 5000);

               }
               else
               Toast.makeText(shop_details.this,"Cannot find you",Toast.LENGTH_SHORT).show();}
            }
        });
        working_hours=(TextView)findViewById(R.id.working_hours);
        btn=(Button)findViewById(R.id.button);
        String st=c1.getContact();
        btn.setText("Call("+c1.getDistance()+")");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+c1.getContact()));
                if (ActivityCompat.checkSelfPermission(shop_details.this,android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(shop_details.this,
                            android.Manifest.permission.CALL_PHONE)) {
                    } else {
                        ActivityCompat.requestPermissions(shop_details.this,
                                new String[]{android.Manifest.permission.CALL_PHONE},
                                1);
                    }
                }
                startActivity(callIntent);
            }
        });

        contact=(TextView)findViewById(R.id.contact);
        name.setText(c1.getName());
        location.setText(c1.getLocation());
        services.setText(c1.getServices());
        working_hours.setText(c1.getWorking_hours());
    }
}