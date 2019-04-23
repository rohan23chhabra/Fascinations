package com.example.fascinations;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fascinations.core.InventoryOwner;
import com.example.fascinations.db.DB;
import com.example.fascinations.serialize.MyGson;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InventoryListActivity extends AppCompatActivity {
    ListView listView;

    List<InventoryOwner> ownerList = new ArrayList<>();
    Location currentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_list);
        listView = findViewById(R.id.inventory_list);
        FusedLocationProviderClient fusedLocationClient = LocationServices
                .getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        currentLocation = (Location) bundle.get("current-location");

        getListOfInventories();

    }

    private void getListOfInventories() {
        DB.getDatabaseReference().child("inventory-owner").addValueEventListener(
                new ValueEventListener() {
                    @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> dataSnapshotIterator =
                                dataSnapshot.getChildren().iterator();
                        while (dataSnapshotIterator.hasNext()) {
                            DataSnapshot dataSnapshotChild = dataSnapshotIterator.next();
                            Gson gson = MyGson.getGson();
                            InventoryOwner owner =
                                    gson.fromJson(gson.toJson(dataSnapshotChild.getValue()),
                                            InventoryOwner.class);
                            ownerList.add(owner);
                        }
                        CustomAdapter customAdapter =
                                new CustomAdapter(InventoryListActivity.this, ownerList,
                                        currentLocation);
                        listView.setAdapter(customAdapter);
                    }

                    @Override public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }
}
