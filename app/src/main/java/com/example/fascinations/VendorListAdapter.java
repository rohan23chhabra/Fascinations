package com.example.fascinations;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fascinations.core.VendorOwner;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VendorListAdapter extends ArrayAdapter<VendorOwner> {

    private Activity context;
    private List<VendorOwner> vendorOwnerList;
    private Location currentLocation;

    public VendorListAdapter(Activity context,
                             List<VendorOwner> vendorOwnerList,
                             Location currentLocation) {
        super(context, R.layout.vendor_view);
        this.context = context;
        this.vendorOwnerList = vendorOwnerList;
        this.currentLocation = currentLocation;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(R.layout.vendor_view, null, true);

        ImageView imageView =
                convertView.findViewById(R.id.vendor_owner_photo);
        TextView distanceView =
                convertView.findViewById(R.id.show_vendor_distance);
        TextView nameView = convertView.findViewById(R.id.show_vendor_name);
        TextView categoryView =
                convertView.findViewById(R.id.show_vendor_category);
        TextView phoneView = convertView.findViewById(R.id.show_vendor_phone);
        TextView addressView =
                convertView.findViewById(R.id.show_vendor_address);
        Button scanQRCode = convertView.findViewById(R.id.scan_qr_code);
        Button rateButton = convertView.findViewById(R.id.rate_vendor);

        final VendorOwner vendorOwner = vendorOwnerList.get(position);
        Picasso.get().load(vendorOwner.getImageURL())
                .into(imageView);
        final Location vendorLocation = new Location("Vendor Location");
        vendorLocation.setLatitude(vendorOwner.getLocation().latitude);
        vendorLocation.setLongitude(vendorOwner.getLocation().longitude);
        double distance = currentLocation.distanceTo(vendorLocation);
        distanceView.setText(
                "Distance: " + String.valueOf(Math.round(distance)) + "metre");
        nameView.setText("Owner Name: " + vendorOwner.getName());
        categoryView.setText(
                "Category: " + vendorOwner.getFoodCategory().toString());
        phoneView.setText("Phone: " + vendorOwner.getPhoneNumber());
        addressView.setText("Address: " + vendorOwner.getAddress());

        scanQRCode.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(context,
                        QRCodeScannerActivity.class);
                context.startActivity(intent);
            }
        });

        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

            }
        });
        return convertView;
    }

    @Override public int getCount() {
        return vendorOwnerList.size();
    }

}
