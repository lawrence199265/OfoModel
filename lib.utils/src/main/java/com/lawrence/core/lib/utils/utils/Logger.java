//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.lawrence.core.lib.utils.utils;

import android.util.Log;

public class Logger {
    private static final String TAG = "Logger";

    private static final String TOP = "╔═════════════════════════════════════════════════" +
            "══════════════════════════════════════════════════";

    private static final String MIDDLE = "╟─────────────────────────────────────────────────" +
            "──────────────────────────────────────────────────";

    private static final String END = "╚═════════════════════════════════════════════════" +
            "══════════════════════════════════════════════════";
    private static volatile boolean DEBUG = true;

    public Logger() {
    }

    public static void isDebug(Boolean isDebug) {
        DEBUG = isDebug;
    }

    public static void debug(String TAG, String message) {
        if (DEBUG) {

            StringBuilder msg = new StringBuilder();


            msg
                    .append(TOP)
                    .append("\n")
                    .append(MIDDLE)
                    .append("     ")
                    .append(TAG)
                    .append(" : ")
                    .append(message)
                    .append("     \n")
                    .append(END);
            Log.d("Logger", msg.toString());
        }
    }

    public static void info(String TAG, String message) {
        if (DEBUG) {
            Log.i("Logger", "------------------------------------------ " + TAG + " ------------------------------------------");
            Log.i("Logger", message);
        }
    }

    public static void error(String TAG, String message) {
        Log.e("Logger", message);
    }

    public static void warning(String TAG, String message) {
        Log.w("Logger", "------------------------------------------ " + TAG + " ------------------------------------------");
        Log.w("Logger", message);
    }
}
