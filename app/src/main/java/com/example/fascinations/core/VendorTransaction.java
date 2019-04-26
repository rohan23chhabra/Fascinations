package com.example.fascinations.core;

public class VendorTransaction {
    private String userPhoneNumber;
    private String vendorPhoneNumber;
    private FoodCategory foodCategory;
    private int hours;
    private int minutes;
    private int seconds;

    public VendorTransaction(String userPhoneNumber,
                             String vendorPhoneNumber,
                             FoodCategory foodCategory, int hours, int minutes,
                             int seconds) {
        this.userPhoneNumber = userPhoneNumber;
        this.vendorPhoneNumber = vendorPhoneNumber;
        this.foodCategory = foodCategory;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getVendorPhoneNumber() {
        return vendorPhoneNumber;
    }

    public void setVendorPhoneNumber(String vendorPhoneNumber) {
        this.vendorPhoneNumber = vendorPhoneNumber;
    }

    public FoodCategory getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(FoodCategory foodCategory) {
        this.foodCategory = foodCategory;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    @Override public String toString() {
        return "VendorTransaction{" +
                "userPhoneNumber='" + userPhoneNumber + '\'' +
                ", vendorPhoneNumber='" + vendorPhoneNumber + '\'' +
                ", foodCategory=" + foodCategory +
                ", hours=" + hours +
                ", minutes=" + minutes +
                ", seconds=" + seconds +
                '}';
    }
}
