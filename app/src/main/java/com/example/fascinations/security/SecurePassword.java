package com.example.fascinations.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurePassword {
    public static String getHashedPassword(String input, String phoneNumber) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest((input + phoneNumber).getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return input;
    }
}
