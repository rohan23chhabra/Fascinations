package com.example.fascinations;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fascinations.core.VendorOwner;
import com.example.fascinations.core.VendorTransaction;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRCodeScannerActivity extends AppCompatActivity {

    String scanContent;
    String scanFormat;
    TextView textView;
    String phoneNumber;
    VendorOwner vendorOwner;
    VendorTransaction vendorTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);
        textView = findViewById(R.id.scanning_result);
    }

    public void scanQRCodeOnClick(View view) {
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.setPrompt("Scan");
        scanIntegrator.setBeepEnabled(true);
        //The following line if you want QR code
        scanIntegrator.setDesiredBarcodeFormats(
                IntentIntegrator.QR_CODE_TYPES);
        scanIntegrator.setCaptureActivity(
                CaptureActivityAnyOrientation.class);
        scanIntegrator.setOrientationLocked(true);
        scanIntegrator.setBarcodeImageEnabled(true);
        scanIntegrator.initiateScan();
    }

    @Override protected void onActivityResult(int requestCode, int resultCode,
                                              @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanningResult = IntentIntegrator
                .parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null) {
            if (scanningResult.getContents() != null) {
                scanContent = scanningResult.getContents();
                scanFormat = scanningResult.getFormatName();
            }

            Toast.makeText(this, scanContent + "   type:" + scanFormat,
                    Toast.LENGTH_SHORT).show();

            phoneNumber = scanContent;
            textView.setText("You interacted with a local food vendor " +
                    "business whose phone number is " + phoneNumber);

            Intent intent = new Intent(QRCodeScannerActivity.this,
                    TakeOrderActivity.class);
            intent.putExtra("qr-phone", phoneNumber);
            startActivity(intent);

//            final String userPhoneNumber =
//                    new SessionDetails(this).getSharedPreferences()
//                            .getString("phone", "");
//            DB.getDatabaseReference().child("vendor-owner").child(phoneNumber)
//                    .addValueEventListener(
//                            new ValueEventListener() {
//                                @Override public void onDataChange(
//                                        @NonNull DataSnapshot dataSnapshot) {
//                                    Gson gson = MyGson.getGson();
//                                    QRCodeScannerActivity.this.vendorOwner =
//                                            gson.fromJson(gson.toJson(
//                                                    dataSnapshot.getValue()),
//                                                    VendorOwner.class);
//                                    Log.i("mc-interact",
//                                            vendorOwner.toString());
//                                }
//
//                                @Override public void onCancelled(
//                                        @NonNull DatabaseError databaseError) {
//
//                                }
//                            });
//            new Handler().postDelayed(new Runnable() {
//                @Override public void run() {
//                    Date now = Calendar.getInstance().getTime();
//                    QRCodeScannerActivity.this.vendorTransaction =
//                            new VendorTransaction(userPhoneNumber,
//                                    phoneNumber,
//                                    vendorOwner.getFoodCategory(),
//                                    now.getHours(), now.getMinutes(),
//                                    now.getSeconds());
//                    DB.getDatabaseReference().child("vendor-transaction")
//                            .child(userPhoneNumber)
//                            .setValue(vendorTransaction);
//                    Log.i("bc-transaction", vendorTransaction.toString());
//                }
//            }, 2000);

        } else {
            Toast.makeText(this, "Nothing scanned", Toast.LENGTH_SHORT).show();
        }
    }

    public void rateOnClick(View view) {

    }
}

