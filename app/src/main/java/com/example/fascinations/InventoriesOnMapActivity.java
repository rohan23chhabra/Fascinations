package com.example.fascinations;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.fascinations.core.InventoryOwner;
import com.example.fascinations.db.DB;
import com.example.fascinations.serialize.MyGson;
import com.example.fascinations.util.SessionDetails;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class InventoriesOnMapActivity extends FragmentActivity
        implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    private GoogleMap googleMap;
    private SharedPreferences.Editor editor;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION =
            100;
    List<InventoryOwner> ownerList = new ArrayList<>();
    Location currentLocation;
    int userCapacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventories_on_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        editor = new SessionDetails(this).getEditor();
        mapFragment.getMapAsync(this);
        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);
        currentLocation = new Location("");
        currentLocation.setLongitude(81.8639);
        currentLocation.setLatitude(25.4920);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            currentLocation = location;
                        } else {

                        }
                    }
                });
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        userCapacity = bundle.getInt("number-of-bags");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat
                    .requestPermissions(InventoriesOnMapActivity.this,
                            new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        } else {
            setCurrentLocationOnMap();
        }

        DB.getDatabaseReference().child("inventory-owner")
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override public void onDataChange(
                                    @NonNull DataSnapshot dataSnapshot) {
                                Iterator<DataSnapshot> dataSnapshotIterator =
                                        dataSnapshot.getChildren().iterator();

                                ownerList.clear();
                                while (dataSnapshotIterator.hasNext()) {
                                    DataSnapshot dataSnapshotChild =
                                            dataSnapshotIterator.next();
                                    Gson gson = MyGson.getGson();
                                    Log.i("mera-owner", gson.toJson(
                                            dataSnapshotChild.getValue()));
                                    InventoryOwner owner =
                                            gson.fromJson(gson.toJson(
                                                    dataSnapshotChild
                                                            .getValue()),
                                                    InventoryOwner.class);
                                    ownerList.add(owner);
                                    LatLng latLng = owner.getLocation();
                                    Log.i("owner-mc", owner.toString());
                                    Date now = Calendar.getInstance().getTime();
                                    Time openingTime =
                                            getTime(owner.getOpeningTime());
                                    Time closingTime =
                                            getTime(owner.getClosingTime());
                                    boolean isAuthentic =
                                            owner.getVerified().equals("true")
                                                    && owner.getOpen()
                                                    .equals("true")
                                                    && checkTime(openingTime,
                                                    now, closingTime)
                                                    && (userCapacity <= owner
                                                    .getCapacity());
                                    if (isAuthentic) {
                                        Log.i("marker-bc", "bc");
                                        googleMap.addMarker(new MarkerOptions()
                                                .position(latLng)
                                                .title(owner.getName())
                                                .snippet("Inventory")
                                                .icon(BitmapDescriptorFactory
                                                        .defaultMarker(
                                                                BitmapDescriptorFactory.HUE_AZURE)));
                                        googleMap.animateCamera(
                                                CameraUpdateFactory
                                                        .newLatLng(latLng));
                                    }
                                }

                            }

                            @Override public void onCancelled(
                                    @NonNull DatabaseError databaseError) {

                            }
                        });
    }

    private boolean checkTime(Time openingTime, Date now, Time closingTime) {
        Time nowTime = new Time(now.getHours(), now.getMinutes(), 0);
        long now_time = nowTime.getTime();
        long opening_time = openingTime.getTime();
        long closing_time = closingTime.getTime();

        if (now_time >= opening_time && now_time <= closing_time) {
            return true;
        }
        return false;
    }

    private Time getTime(String time) {
        StringTokenizer stringTokenizer = new StringTokenizer(time, ":");
        int hours = Integer.parseInt(stringTokenizer.nextToken());
        int minutes = Integer.parseInt(stringTokenizer.nextToken());
        Time hms = new Time(hours, minutes, 0);
        return hms;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setCurrentLocationOnMap();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void setCurrentLocationOnMap() {
        googleMap.setMyLocationEnabled(true);
        Log.i("permission111", "Permission granted");
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(
                        this,
                        new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(
                                    Location location) {
                                if (location != null) {
                                    Log.i("location111",
                                            "Current location.." +
                                                    ".yay");
                                    LatLng currentLocation =
                                            new LatLng(
                                                    location.getLatitude(),
                                                    location.getLongitude());
                                    googleMap.addMarker(
                                            new MarkerOptions()
                                                    .position(
                                                            currentLocation)
                                                    .title("Current Location"));
                                    googleMap.moveCamera(
                                            CameraUpdateFactory
                                                    .newLatLng(
                                                            currentLocation));

                                    googleMap.animateCamera(
                                            CameraUpdateFactory
                                                    .newLatLngZoom(
                                                            new LatLng(
                                                                    location.getLatitude(),
                                                                    location.getLongitude()),
                                                            13));

                                    CameraPosition cameraPosition =
                                            new CameraPosition.Builder()
                                                    .target(new LatLng(
                                                            location.getLatitude(),
                                                            location.getLongitude()))
                                                    .zoom(17)
                                                    .bearing(90)
                                                    .tilt(40)
                                                    .build();

                                    googleMap.animateCamera(
                                            CameraUpdateFactory
                                                    .newCameraPosition(
                                                            cameraPosition));
                                }
                            }
                        });
    }

    @Override public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    public void seeListOnClick(View view) {
        Intent intent = new Intent(InventoriesOnMapActivity.this,
                InventoryListActivity.class);
        intent.putExtra("current-location", currentLocation);
        intent.putExtra("number-of-bags", userCapacity);
        startActivity(intent);
    }



}
