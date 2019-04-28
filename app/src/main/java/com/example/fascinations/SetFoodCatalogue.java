package com.example.fascinations;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.fascinations.core.VendorOwner;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class SetFoodCatalogue extends AppCompatActivity {

    LinearLayout linearLayout;
    List<EditText> itemPriceList = new ArrayList<>();
    StringTokenizer stringTokenizer;
    VendorOwner vendorOwner;
    LatLng markedLocation;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_food_catalogue);
        linearLayout = findViewById(R.id.dynamic_linear_layout);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        vendorOwner = (VendorOwner) bundle.getSerializable("vendor-owner");
        markedLocation = bundle.getParcelable("marked-location");
    }

    public void addFoodOnClick(View view) {
        EditText editText = new EditText(this);
        editText.setHint("Name of item: Price");
        editText.setId(id++);
        linearLayout.addView(editText);
        LinearLayout.LayoutParams layoutParams =
                (LinearLayout.LayoutParams) editText.getLayoutParams();
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        Resources r = getResources();
        int pixels = (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10.0f, r.getDisplayMetrics());
        layoutParams.topMargin = pixels;
        itemPriceList.add(editText);
    }

    public void doneOnClick(View view) {
        Map<String, Integer> map = new HashMap<>();
        for (EditText editText : itemPriceList) {
            stringTokenizer = new StringTokenizer(editText.getText().toString(), ":");
            String item = getItem();
            Integer price = getPrice();
            map.put(item, price);
        }
        vendorOwner.setFoodMenu(map);
        Intent intent = new Intent(this, SignUpVendorActivity.class);
        intent.putExtra("vendor-owner", vendorOwner);
        intent.putExtra("vendor-location", markedLocation);
        startActivity(intent);
    }

    private Integer getPrice() {
        return Integer.parseInt(stringTokenizer.nextToken());
    }

    private String getItem() {
        return stringTokenizer.nextToken();
    }


}
