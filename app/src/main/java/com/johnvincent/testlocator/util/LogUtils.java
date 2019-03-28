package com.johnvincent.testlocator.util;

import android.util.Log;


public class LogUtils {

    public static void d(String tag, String log) {
            Log.d(tag, log);
    }

    public static void e(String tag, String log) {
            Log.e(tag, log);
    }
}
