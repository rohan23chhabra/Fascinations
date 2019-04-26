package com.example.fascinations;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ChooseBusinessActivity extends AppCompatActivity {

    EditText bagsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_business);
        bagsText = findViewById(R.id.input_bags_to_enter);
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
}
