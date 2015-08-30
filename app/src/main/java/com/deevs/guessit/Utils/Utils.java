package com.deevs.guessit.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class Utils {
    /**
     * Removes spaces and tabs from the string
     **/
    public static String removeSpacesAndTabs(String str) {
        return str.replaceAll("\\s","");
    }

    public static void showToast(final Context context, final String message, final int duration) {
        final Toast newToast = Toast.makeText(context.getApplicationContext(), message, duration);
        newToast.show();
    }
}
