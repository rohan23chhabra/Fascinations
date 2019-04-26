package com.example.fascinations;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRCodeScannerActivity extends AppCompatActivity {

    String scanContent;
    String scanFormat;
    TextView textView;
    String phoneNumber;

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

            textView.setText(scanContent + "    type:" + scanFormat);
            phoneNumber = scanContent;


        } else {
            Toast.makeText(this, "Nothing scanned", Toast.LENGTH_SHORT).show();
        }
    }
}

