package com.example.breakdownbreaker;
//>-----------------Android studio imports required libraries----------<

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//<------------------------------------------------------------>
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "entra nee bhaadha";
    Dialog dialog;
    //>--------------required variables declaration-----------<
    private boolean mLocationPermissionGranted;
    int REQUESTCODE = 100;
    LatLng currentlatlag;
    private FusedLocationProviderClient mFusedLocationclient;
    private GoogleMap mMap;
    FloatingActionButton addbtn, logoutbtn;
    HashMap<LatLng, container> hashMap;
    HashMap<LatLng, String> clicks;
    Marker marker;
    ArrayList<container> arrayList;
    LocationManager locationm;
    HashMap<Marker, container> helper;
    FirebaseDatabase f;
    DatabaseReference d;
    MapFragment mapFragment;

    private Address ad;

    //<------------------------------------------------------->
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);//linking to our xml file
        addbtn = (FloatingActionButton) findViewById(R.id.addbtn);
        logoutbtn = (FloatingActionButton) findViewById(R.id.logoutbtn);
        f = FirebaseDatabase.getInstance();
        d = f.getReference("shops");
        clicks = new HashMap<>();
        hashMap = new HashMap<>();
        helper = new HashMap<>();
        arrayList = new ArrayList<>();
        dialog = new Dialog(MapActivity.this);
        dialog.setContentView(R.layout.progress);
        dialog.setCanceledOnTouchOutside(false);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);//intialising support mapfragment to contain our map
        mapFragment.getMapAsync(this);//syncing map,once its ready onMapReadyCallback is called()
//>--------checking whther location permissions are granted and gps is enabled else direct user to enable them-----------<
        mFusedLocationclient = LocationServices.getFusedLocationProviderClient(this);
        if (mLocationPermissionGranted) {

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1009);
                }
            }
        }
        if (isGPSEnabled()) ;
        locationm = (LocationManager) getSystemService(LOCATION_SERVICE);
//<------------------------------------------------------------------------------------------------------------------------>
//>------------------------------Checking and updating location of user----------------------------------------------------<
        locationm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, new LocationListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if (marker != null)
                    marker.remove();
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                if (currentlatlag != latLng) {
                    currentlatlag = latLng;
                    marker = mMap.addMarker(new MarkerOptions().position(currentlatlag).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(currentlatlag));
                }
                update_distances();
            }


        });
//<------------------------------------------------------------------------------------------------------------------------>
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MapActivity.this, JobDetails.class);
                startActivity(it);
            }
        });
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent it = new Intent(MapActivity.this, MainActivity.class);
                startActivity(it);
                finish();
            }
        });
        d.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!arrayList.isEmpty()) arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    container h = dataSnapshot.getValue(container.class);
                    arrayList.add(h);
                }
                loadshops(mMap, arrayList);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        d.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!arrayList.isEmpty()) arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    container h = dataSnapshot.getValue(container.class);
                    arrayList.add(h);
                }
                loadshops(mMap, arrayList);
                Toast.makeText(MapActivity.this, "You can press the gear icon to add your shop", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void update_distances() {
        float[] result = new float[10];
        for (Map.Entry m : helper.entrySet()) {
            container c1 = (container) m.getValue();
            Marker a = (Marker) m.getKey();
            Location.distanceBetween(currentlatlag.latitude, currentlatlag.longitude, a.getPosition().latitude, a.getPosition().longitude, result);
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            result[0] = Float.valueOf(decimalFormat.format(result[0] / 1000.0));
            a.setTitle(c1.getName() + "(" + result[0] + "kms away)");
        }

    }

    private boolean isGPSEnabled() {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean checker = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (checker) {
            return true;
        } else {
            AlertDialog ad = new AlertDialog.Builder(this).setTitle("Permissions").setMessage("Please enable GPS").
                    setPositiveButton("Yes", ((dialoginterfae, i) -> {
                        Intent it = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(it);
                    })).setCancelable(false).show();
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUESTCODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        dialog.show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onMarkerClick(Marker marker) {
                container c1 = hashMap.get(marker.getPosition());
                String sp = clicks.get(marker.getPosition());
                if (sp != null && sp.equals("0")) {
                    clicks.forEach((a, b) -> clicks.put(a, "0"));
                    clicks.put(marker.getPosition(), "1");
                } else {
                    if (c1 != null) {
                        c1.setDistance(marker.getTitle());
                        Intent it = new Intent(MapActivity.this, shop_details.class);
                        it.putExtra("mycontainer", (container) c1);
                        startActivity(it);
                    }
                }

                return false;
            }
        });
        mFusedLocationclient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentlatlag = new LatLng(location.getLatitude(), location.getLongitude());
                    marker = mMap.addMarker(new MarkerOptions().position(currentlatlag).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentlatlag, 7f));
                }
            }
        });
        mMap.setMyLocationEnabled(true);
    }

    private void loadshops(GoogleMap mMap, ArrayList<container> arr) {

        for (container c : arr) {
            if (c != null) {
                Log.w(TAG, c.getLocation());
                geolocate(c);
            }
        }
        dialog.dismiss();
    }

    public void geolocate(container c) {

        String adress = c.getLocation();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> adresslist = geocoder.getFromLocationName(adress, 1);
            if (adresslist.size() > 0) {
                ad = adresslist.get(0);
                ad.setCountryName("India");
                LatLng gang = new LatLng(ad.getLatitude(), ad.getLongitude());
                //         showmarker(gang);
                float[] result = new float[10];
                if (currentlatlag == null) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mFusedLocationclient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(Task<Location> task) {
                            Location.distanceBetween(currentlatlag.latitude, currentlatlag.longitude, gang.latitude, gang.longitude, result);
                            DecimalFormat decimalFormat = new DecimalFormat("#.##");
                            result[0] = Float.valueOf(decimalFormat.format(result[0] / 1000.0));
                            Marker mkr = mMap.addMarker(new MarkerOptions().position(gang).title(c.getName() + "(" + result[0] + "kms away)"));
                            hashMap.put(mkr.getPosition(), c);
                            clicks.put(mkr.getPosition(), "0");
                            helper.put(mkr, c);
                            c.setDistance("" + result[0]);
                        }
                    });
                }
                if(currentlatlag!=null){
                    Log.w(TAG,"ochindhandi vayyari");
                    Location.distanceBetween(currentlatlag.latitude, currentlatlag.longitude, gang.latitude, gang.longitude, result);
                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    result[0] = Float.valueOf(decimalFormat.format(result[0] / 1000.0));
                    Marker mkr = mMap.addMarker(new MarkerOptions().position(gang).title(c.getName() + "(" + result[0] + "kms away)"));
                    hashMap.put(mkr.getPosition(), c);
                    clicks.put(mkr.getPosition(), "0");
                    helper.put(mkr, c);
                    c.setDistance("" + result[0]);
                }
            }
        } catch (IOException e) {

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}