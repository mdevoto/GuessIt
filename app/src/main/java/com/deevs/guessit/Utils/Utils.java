package com.deevs.guessit.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Utils {
    /**
     * Removes spaces and tabs from the string
     **/
    public static String removeSpacesAndTabs(String str) {
        return str.replaceAll("\\s","");
    }
}
