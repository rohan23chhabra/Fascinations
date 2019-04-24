package com.example.fascinations.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionDetails {

    private Context context;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    public SessionDetails(Context context) {
        this.context = context;
        sharedPreferences = context
                .getSharedPreferences("Session", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public void setEditor(SharedPreferences.Editor editor) {
        this.editor = editor;
    }
}
