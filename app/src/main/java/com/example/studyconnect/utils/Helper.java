package com.example.studyconnect.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Helper {

    public static void showLoading(FrameLayout progressBar, Boolean isLoading) {
        if (isLoading){
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    public static String formatDate(String inputDate) {
        // Format tanggal input (ISO 8601 dengan timezone Z)
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Set timezone ke UTC

        // Format tanggal output
        SimpleDateFormat outputFormat = new SimpleDateFormat("'Diposting ' dd MMM 'jam' HH:mm", new Locale("id", "ID"));

        try {
            // Parse tanggal input ke dalam objek Date
            Date date = inputFormat.parse(inputDate);

            // Format tanggal ke dalam string yang diinginkan
            assert date != null;
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Format tanggal tidak valid";
        }
    }

    public static boolean hasNetworkConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static void startActivity(Class<?> targetActivity, Context context) {
        Intent intent = new Intent(context, targetActivity);
        context.startActivity(intent);
    }

    public static void startFlagedActivity(Class<?> targetActivity, Context context) {
        Intent intent = new Intent(context, targetActivity);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
