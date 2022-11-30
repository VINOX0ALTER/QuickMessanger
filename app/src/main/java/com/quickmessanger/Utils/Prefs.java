package com.quickmessanger.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    public static SharedPreferences preferences;

    public static void setPreferanceString(Context context, String key, String value) {
        preferences = context.getSharedPreferences("MP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
        editor.commit();
    }



    public static String getPreferanceString(Context context, String key, String defvalue) {
        preferences = context.getSharedPreferences("MP", Context.MODE_PRIVATE);
        String value = preferences.getString(key, defvalue);
        return value;
    }

    public static void setPreferanceboolean(Context context, String key, boolean value) {
        preferences = context.getSharedPreferences("MP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
        editor.commit();
    }
    public static boolean getPreferanceBoolean(Context context, String key, boolean defvalue) {
        preferences = context.getSharedPreferences("MP", Context.MODE_PRIVATE);
        boolean value = preferences.getBoolean(key, defvalue);
        return value;
    }

    public static void setPreferanceInt(Context context, String key, int value) {
        preferences = context.getSharedPreferences("MP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
        editor.commit();
    }
    public static int getPreferanceInt(Context context, String key, int defvalue) {
        preferences = context.getSharedPreferences("MP", Context.MODE_PRIVATE);
        int value = preferences.getInt(key, defvalue);
        return value;
    }
}
