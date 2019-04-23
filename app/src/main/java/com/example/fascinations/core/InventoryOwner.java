package com.example.fascinations.core;

import com.google.android.gms.maps.model.LatLng;

public class InventoryOwner extends Owner {
    private int capacity;

    public InventoryOwner(int capacity) {
        this.capacity = capacity;
    }

    public InventoryOwner() {
    }

    public InventoryOwner(String name, String email, String password,
                          String ifscCode, String phoneNumber,
                          String panNumber,
                          String imageURL, String accountNumber,
                          LatLng location,
                          String openingTime, String closingTime,
                          String open,
                          int capacity, String address) {
        super(name, email, password, ifscCode, phoneNumber,
                panNumber,
                imageURL, accountNumber, location, openingTime, closingTime,
                open, address);
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }


}
