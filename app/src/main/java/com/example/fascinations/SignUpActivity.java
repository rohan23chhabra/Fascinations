package com.example.fascinations;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fascinations.core.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity
        extends AppCompatActivity {

    EditText username;
    EditText passwordText;
    Button signUpButton;
    String phoneNumber;
    EditText confirmPassword;
    EditText name;

    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;

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

        username = findViewById(R.id.input_username);
        passwordText = findViewById(R.id.input_password);
        signUpButton = findViewById(R.id.btn_login);
        name = findViewById(R.id.input_name);
        confirmPassword = findViewById(R.id.input_confirm_password);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        sharedPreferences = getSharedPreferences("Session", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!passwordText.getText().toString()
                        .equals(confirmPassword.getText().toString())) {
                    Toast.makeText(SignUpActivity.this, "Passwords don't " +
                                    "match.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String username = SignUpActivity.this.username.getText()
                        .toString();
                User user = new User(phoneNumber,
                        passwordText.getText().toString(),
                        name.getText().toString());
                databaseReference.child("users").child(phoneNumber)
                        .setValue(user);
                Log.i("signup", "Success.");
                editor.putString("phone", phoneNumber);
                editor.putString("password", passwordText.getText().toString());
                editor.apply();
                Intent intent = new Intent(SignUpActivity.this,
                        MainActivity.class);
                SignUpActivity.this.startActivity(intent);

            }
        });
    }
}
