package com.example.fascinations;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.example.fascinations.core.VendorOwner;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class SetLocationOfVendor extends FragmentActivity
        implements OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 100;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng markedLocation;
    private VendorOwner vendorOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location_of_vendor);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);
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

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        this.googleMap.addMarker(
//                new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            this.googleMap.setMyLocationEnabled(true);
            setCurrentLocationOnMap();
        } else {
            ActivityCompat
                    .requestPermissions(SetLocationOfVendor.this,
                            new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        }

//        this.googleMap.getUiSettings().setCompassEnabled(false);
//        this.googleMap.getUiSettings().setIndoorLevelPickerEnabled(false);
//        this.googleMap.getUiSettings().setMapToolbarEnabled(false);
//        this.googleMap.getUiSettings().setRotateGesturesEnabled(false);
//        this.googleMap.getUiSettings().setZoomControlsEnabled(false);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                // Clears the previously touched position
                googleMap.clear();

                // Animating to the touched position
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                googleMap.addMarker(markerOptions);

                Log.i("lodu", "mark ho gya.");
                Log.i("lodu", latLng.toString());

                markedLocation = latLng;
            }
        });

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

    public void setFinalLocationOfVendor(View view) {
        Log.i("sett", "Location is set.");
        Intent intent = new Intent(SetLocationOfVendor.this,
                SignUpVendorActivity.class);
        intent.putExtra("vendor-location", markedLocation);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        vendorOwner = (VendorOwner) bundle.getSerializable("vendor-owner");
        //Bitmap qrCode = bundle.getParcelable("qr-code");
        intent.putExtra("vendor-owner", vendorOwner);
        //intent.putExtra("qr-code", qrCode);
        startActivity(intent);
    }
}
