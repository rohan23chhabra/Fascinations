package com.example.fascinations;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fascinations.core.User;
import com.example.fascinations.security.SecurePassword;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity
        extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;

    EditText phoneNumber;
    EditText passwordText;
    Button loginButton;
    TextView signUpLink;
    ProgressBar progressBar;
    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phoneNumber = findViewById(R.id.phone_num);
        passwordText = findViewById(R.id.input_password);
        loginButton = findViewById(R.id.btn_login);
        signUpLink = findViewById(R.id.link_signup);
        progressBar = findViewById(R.id.progressbar);

        sharedPreferences = getSharedPreferences("Session", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        databaseReference = FirebaseDatabase.getInstance().getReference();


        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if((networkInfo!=null)&&(networkInfo.isConnected()))
                {
                    loginButton.setVisibility(View.GONE);
                    phoneNumber.setEnabled(false);
                    passwordText.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    signUpLink.setVisibility(View.GONE);
                    final String phone_number = phoneNumber.getText().toString();
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("users")
                            .child(phone_number)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(
                                        @NonNull DataSnapshot dataSnapshot) {
                                    user = dataSnapshot
                                            .getValue(User.class);
                                    String hash1 = user.getPassword();
                                    String hash2 =
                                            SecurePassword.getHashedPassword(
                                                    passwordText.getText().toString(), phone_number);
                                    if (hash1.equals(hash2)) {
                                        Log.i("login", "Successful login.");
                                        editor.putString("phone", phone_number);
                                        editor.putString("password",
                                                user.getPassword());
                                        editor.apply();
                                        Intent intent = new Intent(
                                                LoginActivity.this,
                                                ChooseBusinessActivity.class);
                                        LoginActivity.this.startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(
                                        @NonNull DatabaseError databaseError) {

                                }
                            });
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"No network connection",Toast.LENGTH_SHORT).show();
                }

            }
        });

        signUpLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(LoginActivity.this,
                        PhoneAuthActivity.class);
                startActivity(signUpIntent);
                finish();
            }
        });
    }
}
