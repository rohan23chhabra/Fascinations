package com.example.fascinations;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ChooseBusinessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_business);
    }

    public void inventoryOnClick(View view) {

    }

    public void foodVendorOnClick(View view) {
        Intent intent = new Intent(ChooseBusinessActivity.this, InventoriesOnMapActivity.class);
        startActivity(intent);
    }
}
