package com.example.studyconnect.data;

import android.content.Context;
import android.content.SharedPreferences;

/** Menyimpan token Bearer dan peran pengguna setelah login. */
public final class Session {

    private static final String PREFS = "studyconnect_session";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_ROLE = "role";
    private static final String KEY_NAME = "name";
    private static final String KEY_ID = "user_id";

    private Session() {
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static void save(Context context, String token, String role, String name, int userId) {
        prefs(context).edit()
                .putString(KEY_TOKEN, token)
                .putString(KEY_ROLE, role)
                .putString(KEY_NAME, name)
                .putInt(KEY_ID, userId)
                .apply();
    }

    public static int userId(Context context) {
        return prefs(context).getInt(KEY_ID, 0);
    }

    public static String token(Context context) {
        return prefs(context).getString(KEY_TOKEN, null);
    }

    public static String role(Context context) {
        return prefs(context).getString(KEY_ROLE, null);
    }

    public static String name(Context context) {
        return prefs(context).getString(KEY_NAME, "");
    }

    public static boolean isLoggedIn(Context context) {
        return token(context) != null;
    }

    public static void clear(Context context) {
        prefs(context).edit().clear().apply();
    }
}
