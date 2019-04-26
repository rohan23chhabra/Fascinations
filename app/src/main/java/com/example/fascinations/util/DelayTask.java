package com.example.fascinations.util;

import android.os.Handler;

public class DelayTask {
    public static void executeAfterSomeTime(Runnable runnable,
                                            int milliseconds) {
        final Handler handler = new Handler();
        handler.postDelayed(runnable, milliseconds);
    }
}
