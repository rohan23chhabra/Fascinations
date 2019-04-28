package com.example.fascinations;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fascinations.util.SessionDetails;

public class ChooseBusinessActivity extends AppCompatActivity {

    EditText bagsText;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_business);
        bagsText = findViewById(R.id.input_bags_to_enter);
        editor = new SessionDetails(this).getEditor();
//        sharedPreferences = getSharedPreferences("Session", MODE_PRIVATE);
//        final SharedPreferences.Editor editor = sharedPreferences.edit();
    }

    public void inventoryOnClick(View view) {
        if (bagsText.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter number of bags.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        int numberOfBags = Integer.parseInt(bagsText.getText().toString());
        Intent intent = new Intent(ChooseBusinessActivity.this,
                InventoriesOnMapActivity.class);
        intent.putExtra("number-of-bags", numberOfBags);
        startActivity(intent);
    }

    public void foodVendorOnClick(View view) {
        Intent intent = new Intent(ChooseBusinessActivity.this,
                VendorsOnMapActivity.class);
        startActivity(intent);
    }

    public void scanOnClick(View view) {
        Intent intent = new Intent(this, QRCodeScannerActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                editor.remove("phone");
                editor.remove("password");
                editor.commit();
                Intent intent = new Intent(
                        ChooseBusinessActivity.this,
                        LoginActivity.class);
                ChooseBusinessActivity.this.startActivity(intent);
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void crowdsource(View view) {
            Intent intent = new Intent(ChooseBusinessActivity.this,SignUpVendorActivity.class);
            startActivity(intent);

    }
}
