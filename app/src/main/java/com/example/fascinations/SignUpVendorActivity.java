package com.example.fascinations;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fascinations.core.FoodCategory;
import com.example.fascinations.core.VendorOwner;
import com.example.fascinations.db.DB;
import com.example.fascinations.security.SecurePassword;
import com.example.fascinations.util.SessionDetails;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignUpVendorActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE =
            420;
    Spinner spinner;
    ImageView imageView;
    EditText nameText;
    EditText emailText;
    EditText phoneText;
    EditText passwordText;
    EditText addressText;
    ProgressBar progressBar;
    EditText upiIdText;
    Button locationButton;
    Button uploadButton;
    Button signUpButton;
    Button foodCatalogueButton;

    VendorOwner vendorOwner;
    String phoneNumber;
    private LatLng markedLocation;
    Bitmap bitmap;
    Bitmap qrCode;
    private Uri filePath;
    private Uri qrCodeUri;
    FirebaseStorage storage;
    StorageReference storageReference;
    SharedPreferences.Editor editor;
    private static final int PICK_IMAGE_REQUEST = 72;

    public static final String[] FOOD_CATEGORIES =
            {"BAKERY", "JUICE", "RESTAURANT"
                    , "COFFEE", "TEA", "FAST_FOOD", "CAFETERIA", "DESSERT",
                    "MILKSHAKES", "NONE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_vendor);
        spinner = findViewById(R.id.spinner_food_category);
        imageView = findViewById(R.id.vendor_image);
        nameText = findViewById(R.id.input_vendor_name);
        emailText = findViewById(R.id.input_vendor_email);
        phoneText = findViewById(R.id.input_vendor_phone);
        passwordText = findViewById(R.id.input_vendor_password);
        addressText = findViewById(R.id.input_vendor_address);
        progressBar = findViewById(R.id.vendor_progressbar);
        upiIdText = findViewById(R.id.input_vendor_upi_id);
        locationButton = findViewById(R.id.vendor_btn_location);
        uploadButton = findViewById(R.id.vendor_btn_upload_photo);
        signUpButton = findViewById(R.id.vendor_btn_signup);
        foodCatalogueButton = findViewById(R.id.vendor_btn_food_catalogue);

        Arrays.sort(FOOD_CATEGORIES);
        List<String> foodCategories =
                new ArrayList<>(Arrays.asList(FOOD_CATEGORIES));
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, foodCategories);
        spinner.setAdapter(spinnerArrayAdapter);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                phoneNumber = null;
            } else {
                phoneNumber = extras.getString("phone");
                fillEditTextsOnReturn(extras);
            }
        } else {
            phoneNumber = (String) savedInstanceState.getSerializable("phone");
        }

        editor = new SessionDetails(this).getEditor();
    }

    private void fillEditTextsOnReturn(Bundle extras) {
        vendorOwner = (VendorOwner) extras.getSerializable("vendor-owner");
        if (vendorOwner == null) {
            return;
        }

        emailText.setText(vendorOwner.getEmail());
        nameText.setText(vendorOwner.getName());
        passwordText.setText(vendorOwner.getPassword());
        upiIdText.setText(vendorOwner.getUpiId());
        phoneText.setText(vendorOwner.getPhoneNumber());
        this.phoneNumber = vendorOwner.getPhoneNumber();
        addressText.setText(vendorOwner.getAddress());
        spinner.setSelection(Arrays.binarySearch(FOOD_CATEGORIES,
                vendorOwner.getFoodCategory().toString()));
        this.markedLocation = extras.getParcelable("vendor-location");
        vendorOwner.setLocation(markedLocation);

        //qrCode = extras.getParcelable("qr-code");
    }

    private void makeVendorOwnerObject() {
        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String upiId = upiIdText.getText().toString();
        String phoneNumber = phoneText.getText().toString();
        String address = addressText.getText().toString();
        FoodCategory foodCategory =
                FoodCategory.valueOf(spinner.getSelectedItem().toString());
        String hash = SecurePassword.getHashedPassword(password, phoneNumber);
        vendorOwner = new VendorOwner(name, email, hash, phoneNumber, null, upiId, null, null
                , null, "true", address, foodCategory, null, 0.0, 0);

    }

    private Bitmap getQRCode(String text) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter
                    .encode(text, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void vendorUploadPhotoOnClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null
                && data.getData() != null) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                filePath = data.getData();
                Log.i("filemilgyi", filePath.toString());
                try {
                    bitmap =
                            MediaStore.Images.Media
                                    .getBitmap(this.getContentResolver(),
                                            filePath);
                    imageView.setImageBitmap(bitmap);
                    Log.i("bitmapmilgya", bitmap.toString());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setLocationOfVendor(View view) {
        Intent intent = new Intent(SignUpVendorActivity.this,
                SetLocationOfVendor.class);
        makeVendorOwnerObject();
        String openingTime = "0:0:0";
        String closingTime = "23:59:0";
        vendorOwner.setClosingTime(closingTime);
        vendorOwner.setOpeningTime(openingTime);
        intent.putExtra("vendor-owner", vendorOwner);
        //intent.putExtra("qr-code", qrCode);
        startActivity(intent);
    }

    private void uploadImage(Bitmap bitmap, String pathInFirebase) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if (bitmap != null) {
            Log.i("bitmapcheck", "kya hau");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            StorageReference ref = storageReference
                    .child(pathInFirebase + vendorOwner.getPhoneNumber());

            UploadTask uploadTask = ref.putBytes(data);
            uploadTask
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    Log.i("yusss", "picture upload.");
                                }
                            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpVendorActivity.this,
                                    "Failed " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot
                                            .getBytesTransferred() / taskSnapshot
                                            .getTotalByteCount());
                                }
                            });
        }
    }

    public void signUpVendorOnClick(View view) {

        qrCode = getQRCode(vendorOwner.getPhoneNumber());
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            qrCodeUri = getUriFromImage(qrCode);
            signUpVendorOnClickHelper();
        } else {
            ActivityCompat
                    .requestPermissions(SignUpVendorActivity.this,
                            new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    private void signUpVendorOnClickHelper() {
        signUpButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        spinner.setEnabled(false);
        nameText.setEnabled(false);
        emailText.setEnabled(false);
        passwordText.setEnabled(false);
        addressText.setEnabled(false);

        upiIdText.setEnabled(false);
        locationButton.setEnabled(false);
        uploadButton.setEnabled(false);
        foodCatalogueButton.setEnabled(false);

        uploadImage(bitmap, "vendor-images/");
        setDownloadURLforImage("vendor-images/", filePath, false);
        uploadImage(qrCode, "vendor-images/qr-codes/");
        setDownloadURLforImage("vendor-images/qr-codes/",
                qrCodeUri, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    qrCodeUri = getUriFromImage(qrCode);
                    signUpVendorOnClickHelper();
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

    private Uri getUriFromImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream =
                new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String path =
                MediaStore.Images.Media.insertImage(getContentResolver(),
                        bitmap, "QR-Code", null);
        return Uri.parse(path);
    }

    private void setDownloadURLforImage(String pathInFirebase,
                                        Uri filePath, final boolean isQR) {
        final StorageReference ref = storageReference
                .child(pathInFirebase + vendorOwner.getPhoneNumber());
        UploadTask uploadTask = ref.putFile(filePath);
        Task<Uri> urlTask =
                uploadTask.addOnProgressListener(
                        new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override public void onProgress(
                                    UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot
                                        .getBytesTransferred()) / taskSnapshot
                                        .getTotalByteCount();
                                progressBar.setProgress((int) progress);
                                Log.i("progress101", "lol");
                            }
                        })
                        .continueWithTask(
                                new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(
                                            @NonNull Task<UploadTask.TaskSnapshot> task)
                                            throws Exception {
                                        if (!task.isSuccessful()) {
                                            throw task.getException();
                                        }

                                        // Continue with the task to get the download URL
                                        return ref.getDownloadUrl();
                                    }
                                }).addOnCompleteListener(
                        new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Log.i("khatam", "mc");
                                    Uri downloadUri = task.getResult();
                                    if (isQR) {
                                        vendorOwner.setQrCodeURL(
                                                downloadUri.toString());
                                    } else {
                                        vendorOwner.setImageURL(
                                                downloadUri.toString());
                                    }
                                    DB.getDatabaseReference()
                                            .child("vendor-owner")
                                            .child(vendorOwner
                                                    .getPhoneNumber())
                                            .setValue(vendorOwner);
                                    Log.i("looopp", "paar ho gya loop");
                                    Log.i("signup", "Success.");
                                    //Log.i("imageURL", owner.getImageURL());

                                    Intent intent = new Intent(
                                            SignUpVendorActivity.this,
                                            ChooseBusinessActivity.class);
                                    Toast.makeText(SignUpVendorActivity.this, "Succesfully " +
                                            "crowdsourced business", Toast.LENGTH_SHORT).show();
                                    SignUpVendorActivity.this
                                            .startActivity(intent);
                                } else {
                                    // Handle failures
                                    // ...
                                }
                            }
                        });
    }

    public void foodCatalogueOnClick(View view) {
        Intent intent = new Intent(this, SetFoodCatalogue.class);
        vendorOwner.setLocation(null);
        intent.putExtra("vendor-owner", vendorOwner);
        intent.putExtra("marked-location", markedLocation);
        startActivity(intent);
    }
}
