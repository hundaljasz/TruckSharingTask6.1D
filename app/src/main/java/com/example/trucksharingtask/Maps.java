package com.example.trucksharingtask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Maps extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    int UID;
    Intent intentReceive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        intentReceive = getIntent();
        UID = intentReceive.getIntExtra("UID",0);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng DeakinGeelong = new LatLng(-38.1439, 144.3603);
        mMap.addMarker(new MarkerOptions()
                .position(DeakinGeelong)
                .title("Marker in Deakin Geelong Campus"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(DeakinGeelong));
    }

    @Override
    public void onBackPressed() {
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_my_location:
                Intent intent = new Intent(Maps.this, Maps.class);
                intent.putExtra("UID", UID);
                startActivity(intent);
                finish();
                return true;
            case R.id.nav_home:
                Intent intentHome = new Intent(Maps.this, HomeActivity.class);
                intentHome.putExtra("UID", UID);
                startActivity(intentHome);
                finish();
                return true;
            case R.id.nav_account:
                Intent intentAccount = new Intent(Maps.this, accountActivity.class);
                intentAccount.putExtra("UID", UID);
                startActivity(intentAccount);
                finish();
                return true;
            case R.id.nav_my_orders:
                Intent intentorders = new Intent(Maps.this, HomeActivity.class);
                startActivity(intentorders);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

