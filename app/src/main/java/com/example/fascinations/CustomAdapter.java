package com.example.fascinations;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fascinations.core.InventoryOwner;
import com.example.fascinations.core.InventoryRequest;
import com.example.fascinations.db.DB;
import com.example.fascinations.util.SessionDetails;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

class CustomAdapter extends ArrayAdapter<InventoryOwner> {

    private ImageView imageView;
    private TextView distanceView;
    private TextView nameView;
    private TextView capacityView;
    private TextView phoneView;
    private TextView addressView;
    private TextView priceView;
    private Button bookInventory;
    private Activity context;
    private List<InventoryOwner> ownerList;
    private Location currentLocation;
    private int userCapacity;

    public CustomAdapter(Activity context, List<InventoryOwner> ownerList,
                         Location currentLocation, int userCapacity) {
        super(context, R.layout.inventory_view);
        this.context = context;
        this.ownerList = ownerList;
        this.currentLocation = currentLocation;
        this.userCapacity = userCapacity;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(R.layout.inventory_view, null, true);

        imageView = convertView.findViewById(R.id.inventory_owner_photo);
        distanceView = convertView.findViewById(R.id.show_owner_distance);
        nameView = convertView.findViewById(R.id.show_owner_name);
        capacityView = convertView.findViewById(R.id.show_owner_capacity);
        phoneView = convertView.findViewById(R.id.show_owner_phone);
        addressView = convertView.findViewById(R.id.show_owner_address);
        priceView = convertView.findViewById(R.id.show_owner_price);
        bookInventory = convertView.findViewById(R.id.book_inventory);

        final InventoryOwner owner = ownerList.get(position);
        Picasso.get().load(owner.getImageURL())
                .into(imageView);
        final Location inventoryLocation = new Location("Inventory Location");
        inventoryLocation.setLatitude(owner.getLocation().latitude);
        inventoryLocation.setLongitude(owner.getLocation().longitude);
        double distance = currentLocation.distanceTo(inventoryLocation);
        distanceView.setText("Distance: " + String.valueOf(Math.round(distance)) + "metre");
        nameView.setText("Owner Name: " + owner.getName());
        capacityView.setText("Capacity: " + String.valueOf(owner.getCapacity()) + " bags");
        phoneView.setText("Phone: " + owner.getPhoneNumber());
        addressView.setText("Address: " + owner.getAddress());
        priceView.setText("Price per Bag: " + String.valueOf(owner.getPrice()));

        bookInventory.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                String userPhoneNumber =
                        new SessionDetails(context).getSharedPreferences().getString("phone",
                                "8601444918");
                long currentTimeMillis = System.currentTimeMillis();
                Date now = Calendar.getInstance().getTime();

                InventoryRequest inventoryRequest = new InventoryRequest(userPhoneNumber,
                        owner.getPhoneNumber(),
                        now.getHours(), now.getMinutes(), now.getSeconds(), currentTimeMillis,
                        userCapacity);
                owner.setCapacity(owner.getCapacity() - userCapacity);
                DB.getDatabaseReference().child("inventory-owner").child(owner.getPhoneNumber())
                        .child("capacity").setValue(owner.getCapacity());
                DB.getDatabaseReference().child("pending-inventory-requests")
                        .child(userPhoneNumber).setValue(inventoryRequest);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "http://maps.google.com/maps?saddr=" + currentLocation.getLatitude() +
                                "," + currentLocation.getLongitude() + "&daddr=" + owner
                                .getLocation().latitude + "," + owner.getLocation().longitude));

                context.startActivity(intent);
            }
        });

        return convertView;
    }

    @Override public int getCount() {
        return ownerList.size();
    }
}
