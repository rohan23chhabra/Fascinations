package com.example.fascinations;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.fascinations.core.VendorOwner;
import com.example.fascinations.db.DB;
import com.example.fascinations.serialize.MyGson;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VendorsListActivity extends AppCompatActivity {

    Location currentLocation;
    ListView listView;
    List<VendorOwner> vendorOwnerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendors_list);
        listView = findViewById(R.id.vendor_list);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        currentLocation = (Location) bundle.get("current-location");
        getListOfVendors();
    }

    private void getListOfVendors() {
        DB.getDatabaseReference().child("vendor-owner").addValueEventListener(
                new ValueEventListener() {
                    @Override public void onDataChange(
                            @NonNull DataSnapshot dataSnapshot) {

                        Iterator<DataSnapshot> dataSnapshotIterator =
                                dataSnapshot.getChildren().iterator();
                        vendorOwnerList.clear();
                        while (dataSnapshotIterator.hasNext()) {
                            DataSnapshot dataSnapshotChild =
                                    dataSnapshotIterator.next();
                            Gson gson = MyGson.getGson();
                            VendorOwner vendorOwner =
                                    gson.fromJson(gson.toJson(
                                            dataSnapshotChild
                                                    .getValue()),
                                            VendorOwner.class);
                            vendorOwnerList.add(vendorOwner);
                        }

                        VendorListAdapter vendorListAdapter =
                                new VendorListAdapter(VendorsListActivity.this,
                                        vendorOwnerList, currentLocation);
                        listView.setAdapter(vendorListAdapter);
                    }

                    @Override public void onCancelled(
                            @NonNull DatabaseError databaseError) {

                    }
                });
    }


}
