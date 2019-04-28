package com.example.fascinations;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fascinations.core.Bill;
import com.example.fascinations.core.VendorOwner;
import com.example.fascinations.db.DB;
import com.example.fascinations.serialize.MyGson;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Map;

public class ShowBillActivity extends AppCompatActivity {

    Integer transactionId;
    Bill bill;
    LinearLayout linearLayout;
    VendorOwner vendorOwner;
    Gson gson = MyGson.getGson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bill);

        linearLayout = findViewById(R.id.show_bill_linear_layout);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        transactionId = bundle.getInt("transaction-id");
        String ownerPhoneNumber = bundle.getString("owner-phone-number", "");
        bill = (Bill) bundle.getSerializable("bill");

        DB.getDatabaseReference().child("vendor-owner").child(ownerPhoneNumber)
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                vendorOwner = gson.fromJson(gson.toJson(dataSnapshot.getValue()),
                                        VendorOwner.class);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                populateBillDetails();
            }
        }, 2000);
    }

    private void populateBillDetails() {
        for (Map.Entry element : bill.getOrderMap().entrySet()) {

            TextView itemNameView = new TextView(this);
            TextView priceOneView = new TextView(this);
            TextView quantityView = new TextView(this);
            TextView amountView = new TextView(this);

            String itemName = (String) element.getKey();
            Integer totalPrice = (Integer) element.getValue();

            LinearLayout billLayout = new LinearLayout(this);

            int price = vendorOwner.getFoodMenu().get(itemName);
            int quantity = totalPrice / price;

            itemNameView.setText(itemName);
            priceOneView.setText(String.valueOf(price));
            quantityView.setText(String.valueOf(quantity));
            amountView.setText(String.valueOf(totalPrice));

            billLayout.addView(itemNameView);
            billLayout.addView(priceOneView);
            billLayout.addView(quantityView);
            billLayout.addView(amountView);

            linearLayout.addView(billLayout);

            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) itemNameView.getLayoutParams();
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            Resources r = getResources();
            int pixels = (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40.0f,
                            r.getDisplayMetrics());
            layoutParams.leftMargin = pixels;

            layoutParams = (LinearLayout.LayoutParams) priceOneView.getLayoutParams();
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            r = getResources();
            pixels = (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40.0f,
                            r.getDisplayMetrics());
            layoutParams.leftMargin = pixels;

            layoutParams = (LinearLayout.LayoutParams) quantityView.getLayoutParams();
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            r = getResources();
            pixels = (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70.0f,
                            r.getDisplayMetrics());
            layoutParams.leftMargin = pixels;

            layoutParams = (LinearLayout.LayoutParams) amountView.getLayoutParams();
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            r = getResources();
            pixels = (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60.0f,
                            r.getDisplayMetrics());
            layoutParams.leftMargin = pixels;
        }
    }
}
