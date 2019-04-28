package com.example.fascinations.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class VendorTransaction implements Serializable {
    private String userPhoneNumber;
    private String vendorPhoneNumber;
    private FoodCategory foodCategory;
    private int hours;
    private int minutes;
    private int seconds;
    private Map<String, Integer> orderMap = new HashMap<>();
    private int transactionId;
    private int amount;

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

    public Map<String, Integer> getOrderMap() {
        return orderMap;
    }

    public void setOrderMap(Map<String, Integer> orderMap) {
        this.orderMap = orderMap;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
