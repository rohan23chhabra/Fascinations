package com.example.fascinations.core;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Owner implements Serializable {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String imageURL;
    private String upiId;
    private LatLng location;
    private String openingTime;
    private String closingTime;
    private String open = "true";
    private String verified = "false";
    private String address;

    public Owner() {
    }

    public Owner(String name, String email, String password,
                 String phoneNumber,
                 String imageURL, String upiId,
                 LatLng location, String openingTime, String closingTime,
                 String isOpen, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.imageURL = imageURL;
        this.upiId = upiId;
        this.location = location;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.open = isOpen;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    @Override public String toString() {
        return "Owner{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", upiId='" + upiId + '\'' +
                ", location=" + location +
                ", openingTime='" + openingTime + '\'' +
                ", closingTime='" + closingTime + '\'' +
                ", open='" + open + '\'' +
                ", verified='" + verified + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
