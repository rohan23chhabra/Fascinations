package com.example.fascinations.core;

public class User {
    private String phoneNumber;
    private String password;
    private String email;
    private String name;
    private String imageURL;
    private int numberOfBags;

    public User(String phoneNumber, String password, String name, String email,
                String imageURL,
                int numberOfBags) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.name = name;
        this.email = email;
        this.imageURL = imageURL;
        this.numberOfBags = numberOfBags;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getNumberOfBags() {
        return numberOfBags;
    }

    public void setNumberOfBags(int numberOfBags) {
        this.numberOfBags = numberOfBags;
    }
}
