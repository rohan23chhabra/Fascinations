package com.example.fascinations.serialize;

import com.google.gson.Gson;

public class MyGson {
    public static final Gson gson = new Gson();

    public static Gson getGson() {
        return gson;
    }
}
