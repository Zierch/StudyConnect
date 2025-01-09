package com.example.studyconnect.utils;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SharedPreferencesManager {

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    @Inject
    public SharedPreferencesManager(SharedPreferences sharedPreferences, SharedPreferences.Editor editor) {
        this.sharedPreferences = sharedPreferences;
        this.editor = editor;
    }

    public void saveData(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public void saveData(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    public String getData(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public int getData(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public void removeData() {
        editor.clear();
        editor.apply();
    }
}

