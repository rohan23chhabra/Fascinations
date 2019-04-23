package com.example.fascinations;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fascinations.core.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.example.fascinations.db.DB.storageReference;

public class SignUpActivity
        extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 71;
    EditText emailText;
    EditText passwordText;
    Button signUpButton;
    Button uploadPhotoButton;
    String phoneNumber;
    EditText confirmPassword;
    EditText nameText;
    Bitmap bitmap;
    Uri filePath;
    ImageView imageView;
    User user;
    ProgressBar progressBar;

    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                phoneNumber = null;
            } else {
                phoneNumber = extras.getString("phone");
            }
        } else {
            phoneNumber = (String) savedInstanceState.getSerializable("phone");
        }

        emailText = findViewById(R.id.input_email);
        passwordText = findViewById(R.id.input_password);
        signUpButton = findViewById(R.id.btn_login);
        nameText = findViewById(R.id.input_name);
        confirmPassword = findViewById(R.id.input_confirm_password);
        uploadPhotoButton = findViewById(R.id.btn_photo);
        imageView = findViewById(R.id.sign_up_photo);
        progressBar = findViewById(R.id.progressbar);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        sharedPreferences = getSharedPreferences("Session", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
    }

    public void signUpOnClick(View view) {

        signUpButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        if (!passwordText.getText().toString()
                .equals(confirmPassword.getText().toString())) {
            Toast.makeText(SignUpActivity.this, "Passwords don't " +
                            "match.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String email = emailText.getText().toString();
        user = new User(phoneNumber,
                passwordText.getText().toString(),
                nameText.getText().toString(), email, null);

        uploadImage();
        final StorageReference ref = storageReference
                .child("users/" + user.getPhoneNumber());
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
                                    user.setImageURL(
                                            downloadUri.toString());
                                    databaseReference
                                            .child("users")
                                            .child(user.getPhoneNumber())
                                            .setValue(user);
                                    Log.i("looopp", "paar ho gya loop");
                                    Log.i("signup", "Success.");
                                    //Log.i("imageURL", owner.getImageURL());
                                    editor.putString("phone",
                                            user.getPhoneNumber());
                                    editor.putString("password",
                                            user.getPassword());
                                    editor.apply();
                                    Intent intent = new Intent(
                                            SignUpActivity.this,
                                            ChooseBusinessActivity.class);
                                    SignUpActivity.this
                                            .startActivity(intent);
                                    finish();
                                } else {
                                    // Handle failures
                                    // ...
                                }
                            }
                        });
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

    private void uploadImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if (bitmap != null) {
            Log.i("bitmapcheck", "kya hau");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            StorageReference ref = storageReference
                    .child("users/" + user.getPhoneNumber());

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
                            Toast.makeText(SignUpActivity.this,
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
}
