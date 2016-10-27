package com.viztushar.osumwalls.others;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Preferences {

    private static final String
            PREFERENCES_NAME = "APP_PREFERENCES",
            FAVORITES = "Favorite";
    public static final String KEY_NAVBAR = "coloredNavbar";
    SharedPreferences prefs;
    private final Context context;

    public Preferences(Context context) {
        this.context = context;
    }

    public SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public SharedPreferences getSharedPreferencess() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        prefs = context.getSharedPreferences(PREFERENCES_NAME, 0);
        return prefs.getBoolean(name, defaultValue);
    }

    public boolean isNavbarColored() {
        return getSharedPreferencess().getBoolean(KEY_NAVBAR, false);
    }

    public void setNavbar (boolean bool) {
        getSharedPreferencess().edit().putBoolean(KEY_NAVBAR, bool).apply();
    }

    public int getInteger(String name, int DefaultValue) {
        prefs = context.getSharedPreferences(PREFERENCES_NAME, 0);
        return prefs.getInt(name, DefaultValue);
    }

    public String getString(String name, String defaultValue) {
        prefs = context.getSharedPreferences(PREFERENCES_NAME, 0);
        return prefs.getString(name, defaultValue);
    }

    public void saveBoolean(String name, boolean value) {
        prefs = context.getSharedPreferences(PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(name, value);
        editor.apply();
    }

    public void saveInteger(String name, int value) {
        prefs = context.getSharedPreferences(PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor = prefs.edit();
        editor.putInt(name, value);
        editor.apply();
    }

    public void saveString(String name, String value) {
        prefs = context.getSharedPreferences(PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor = prefs.edit();
        editor.putString(name, value);
        editor.apply();
    }
}