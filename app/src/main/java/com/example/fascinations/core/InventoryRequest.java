package com.example.fascinations.core;

public class InventoryRequest {
    String userPhoneNumber;
    String ownerPhoneNumber;
    int hours;
    int minutes;
    int seconds;
    long currentTimeMillis;
    int numberOfBags;


    public InventoryRequest(String userPhoneNumber, String ownerPhoneNumber, int hours, int minutes,
                            int seconds, long currentTimeMillis, int numberOfBags) {
        this.userPhoneNumber = userPhoneNumber;
        this.ownerPhoneNumber = ownerPhoneNumber;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.numberOfBags = numberOfBags;
        this.currentTimeMillis = currentTimeMillis;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getOwnerPhoneNumber() {
        return ownerPhoneNumber;
    }

    public void setOwnerPhoneNumber(String ownerPhoneNumber) {
        this.ownerPhoneNumber = ownerPhoneNumber;
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

    public int getNumberOfBags() {
        return numberOfBags;
    }

    public void setNumberOfBags(int numberOfBags) {
        this.numberOfBags = numberOfBags;
    }

    public long getCurrentTimeMillis() {
        return currentTimeMillis;
    }

    public void setCurrentTimeMillis(long currentTimeMillis) {
        this.currentTimeMillis = currentTimeMillis;
    }

    public InventoryRequest() {
    }
}
