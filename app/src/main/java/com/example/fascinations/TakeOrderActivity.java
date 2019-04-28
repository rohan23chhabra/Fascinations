package com.example.fascinations;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fascinations.core.Bill;
import com.example.fascinations.core.VendorOwner;
import com.example.fascinations.core.VendorTransaction;
import com.example.fascinations.db.DB;
import com.example.fascinations.serialize.MyGson;
import com.example.fascinations.util.SessionDetails;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.paytm.intentupi.PaytmIntentUpiSdk;
import com.paytm.intentupi.callbacks.PaytmResponseCode;
import com.paytm.intentupi.callbacks.SetPaytmUpiSdkListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class TakeOrderActivity extends AppCompatActivity {

    private static final int TRANSACTION_ID_LIMIT = 1000000;
    VendorOwner vendorOwner;
    LinearLayout linearLayout;
    TextView orderAmountView;
    Map<TextView, CheckBox> itemMap = new HashMap<>();
    Map<TextView, EditText> priceMap = new HashMap<>();
    String userPhoneNumber;
    String ownerPhoneNumber;
    Map<String, Integer> orderMap = new HashMap<>();
    Integer transactionId;
    SessionDetails sessionDetails;
    Set<String> transactionIdSet;
    Button showBillButton;
    VendorTransaction vendorTransaction;
    Bill bill;
    private boolean isBillPrepared = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_order);

        linearLayout = findViewById(R.id.menu_linear_layout);
        orderAmountView = findViewById(R.id.show_order_amount);
        showBillButton = findViewById(R.id.show_bill_button);

        sessionDetails = new SessionDetails(this);
        userPhoneNumber = sessionDetails.getSharedPreferences()
                .getString("phone"
                        , "");

        Log.i("transaction-id-set-lol", "ho gya");

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        ownerPhoneNumber = bundle.getString("qr-phone");
        int delay;
        if (ownerPhoneNumber != null) {
            getVendorOwnerFromPhoneNumber(ownerPhoneNumber);
            delay = 1000;
        } else {
            vendorOwner = (VendorOwner) bundle.getSerializable("vendor-owner");
            LatLng location = bundle.getParcelable("vendor-location");
            vendorOwner.setLocation(location);
            delay = 0;
        }
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                populateMenu();
            }
        }, delay);
    }

    private void getVendorOwnerFromPhoneNumber(String ownerPhoneNumber) {
        DB.getDatabaseReference().child("vendor-owner").child(ownerPhoneNumber)
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override public void onDataChange(
                                    @NonNull DataSnapshot dataSnapshot) {
                                Gson gson = MyGson.getGson();
                                vendorOwner =
                                        gson.fromJson(gson.toJson(
                                                dataSnapshot.getValue()),
                                                VendorOwner.class);
                            }

                            @Override public void onCancelled(
                                    @NonNull DatabaseError databaseError) {
                            }
                        });
    }

    private void populateMenu() {

        for (Map.Entry element : vendorOwner.getFoodMenu().entrySet()) {
            String itemName = (String) element.getKey();
            Integer price = (Integer) element.getValue();

            TextView itemNameView = new TextView(this);
            EditText quantityText = new EditText(this);
            final CheckBox checkBox = new CheckBox(this);
            TextView priceView = new TextView(this);
            quantityText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override public void afterTextChanged(Editable s) {
                    if (s.toString().equalsIgnoreCase("")) {
                        checkBox.setChecked(false);
                        return;
                    }
                    if (Integer.parseInt(s.toString()) > 0) {
                        checkBox.setChecked(true);
                    } else {
                        checkBox.setChecked(false);
                    }
                }
            });

            LinearLayout itemLayout = new LinearLayout(this);
            itemNameView.setText(itemName);
            priceView.setText("Rs. " + price);
            quantityText.setHint("Quantity");
            quantityText.setInputType(InputType.TYPE_CLASS_NUMBER);
            itemLayout.addView(itemNameView);
            itemLayout.addView(quantityText);
            itemLayout.addView(checkBox);
            itemLayout.addView(priceView);
            linearLayout.addView(itemLayout);
            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) itemLayout.getLayoutParams();
            layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;

            layoutParams =
                    (LinearLayout.LayoutParams) itemNameView.getLayoutParams();
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            Resources r = getResources();
            int pixels = (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70.0f,
                            r.getDisplayMetrics());
            layoutParams.leftMargin = pixels;

            layoutParams = (LinearLayout.LayoutParams) quantityText.getLayoutParams();
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            r = getResources();
            pixels = (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10.0f,
                            r.getDisplayMetrics());
            layoutParams.leftMargin = pixels;

            layoutParams =
                    (LinearLayout.LayoutParams) checkBox.getLayoutParams();
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            r = getResources();
            pixels = (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10.0f,
                            r.getDisplayMetrics());
            layoutParams.leftMargin = pixels;

            layoutParams =
                    (LinearLayout.LayoutParams) priceView.getLayoutParams();
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;

            itemMap.put(itemNameView, checkBox);
            priceMap.put(itemNameView, quantityText);
        }
    }

    public void orderAndPayOnClick(View view) {

        setTransactionId();

        int totalAmount = 0;
        Date now = Calendar.getInstance().getTime();
        vendorTransaction =
                new VendorTransaction(userPhoneNumber,
                        vendorOwner.getPhoneNumber(),
                        vendorOwner.getFoodCategory(), now.getHours(),
                        now.getMinutes(), now.getSeconds());
        for (Map.Entry element : itemMap.entrySet()) {
            TextView textView = (TextView) element.getKey();
            CheckBox checkBox = (CheckBox) element.getValue();

            int p = vendorOwner.getFoodMenu()
                    .get(textView.getText().toString());
            int quantity = Integer.parseInt((priceMap.get(textView)).getText().toString());

            totalAmount += p * quantity;
            orderMap.put(textView.getText().toString(), p * quantity);
        }
        orderAmountView.setVisibility(View.VISIBLE);
        orderAmountView.setText("Total amount to pay: " + totalAmount);
        vendorTransaction.setOrderMap(orderMap);
        vendorTransaction.setTransactionId(transactionId);
        vendorTransaction.setAmount(totalAmount);
        DB.getDatabaseReference().child("vendor-transaction")
                .child(userPhoneNumber).setValue(vendorTransaction);
        initiatePayment(totalAmount);
    }

    private void setTransactionId() {
        if (sessionDetails.getSharedPreferences()
                .getStringSet("transaction-id-set", null) == null) {
            transactionId = new Random().nextInt(TRANSACTION_ID_LIMIT);
            transactionIdSet = new TreeSet<>();
            transactionIdSet.add(transactionId.toString());
        } else {
            transactionIdSet = sessionDetails.getSharedPreferences().getStringSet("transaction-id" +
                    "-set", null);
            assert transactionIdSet != null;
            do {
                transactionId = new Random().nextInt(TRANSACTION_ID_LIMIT);
            } while (transactionIdSet.contains(transactionId.toString()));
            transactionIdSet.add(transactionId.toString());
        }
        sessionDetails.getEditor().putStringSet("transaction-id-set", transactionIdSet);
    }

    private void initiatePayment(int totalAmount) {
        try {
            String pspid = "";
            String notes = "";
            String minAmount = "";
            String currency = "";
            String txnUrl = "";
            String deepLink = "";
            PaytmIntentUpiSdk paytmIntentUpiSdk =
                    new PaytmIntentUpiSdk.PaytmIntentUpiSdkBuilder(vendorOwner.getUpiId(),
                            vendorOwner.getName(),
                            vendorOwner.getPhoneNumber(),
                            String.valueOf(transactionId), String.valueOf(totalAmount) + ".00",
                            new SetPaytmUpiSdkListener() {
                                @Override
                                public void onTransactionComplete() {
                                    Toast.makeText(TakeOrderActivity.this, "Transaction initiated.",
                                            Toast.LENGTH_SHORT)
                                            .show();
                                }

                                @Override
                                public void onError(PaytmResponseCode paytmResponseCode,
                                                    String errorMessage) {
                                    Toast.makeText(TakeOrderActivity.this, "Some error occurred.",
                                            Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }).setPspGeneratedId(pspid).setTransactionNote(notes)
                            .setMinimumAmount(minAmount).setCurrency(currency).setTxnRefUrl(txnUrl)
                            .setGenericDeepLink(deepLink).build();


            paytmIntentUpiSdk.startTransaction(TakeOrderActivity.this);
            new Handler().postDelayed(new Runnable() {
                @Override public void run() {
                    showBillButton.setVisibility(View.VISIBLE);
                }
            }, 2000);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void showBillOnClick(View view) {
        final Intent intent = new Intent(TakeOrderActivity.this, ShowBillActivity.class);
        intent.putExtra("transaction-id", vendorTransaction.getTransactionId());
        DB.getDatabaseReference().child("bills").child(transactionId.toString())
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Gson gson = MyGson.getGson();
                                bill = gson.fromJson(gson.toJson(dataSnapshot.getValue()),
                                        Bill.class);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                if (bill == null) {
                    isBillPrepared = false;
                    Toast.makeText(TakeOrderActivity.this, "Bill hasn't been prepared from the " +
                            "vendor owner yet. Wait for a few minutes.", Toast.LENGTH_SHORT).show();
                } else {
                    isBillPrepared = true;
                    intent.putExtra("bill", bill);
                    if (!isBillPrepared) {
                        return;
                    }
                    intent.putExtra("owner-phone-number", vendorOwner.getPhoneNumber());
                    startActivity(intent);
                }
            }
        }, 2000);

    }
}

