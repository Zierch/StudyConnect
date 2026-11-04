package com.example.studyconnect.data;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Pembungkus {@link Call} yang menerjemahkan kegagalan jaringan menjadi pesan di layar,
 * supaya setiap layar tidak perlu menulis try/catch sendiri.
 */
public final class Api {

    private Api() {
    }

    public interface OnSuccess<T> {
        void onData(T data);
    }

    public interface OnFailure {
        void onError(String message);
    }

    public static <T> void enqueue(Call<T> call, OnSuccess<T> onSuccess, OnFailure onFailure) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful() && response.body() != null) {
                    onSuccess.onData(response.body());
                } else if (response.code() == 401) {
                    onFailure.onError("Sesi berakhir, silakan login lagi");
                } else {
                    onFailure.onError("Server menolak permintaan (" + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onFailure.onError("Tidak bisa menghubungi server: " + t.getClass().getSimpleName());
            }
        });
    }

    /** Menampilkan pesan kesalahan di bagian bawah layar. */
    public static void showError(View anchor, String message) {
        Snackbar.make(anchor, message, Snackbar.LENGTH_LONG).show();
    }

    /** Menampilkan keadaan kosong pada sebuah TextView lalu menyembunyikannya saat ada data. */
    public static void showEmpty(TextView target, String message) {
        target.setVisibility(View.VISIBLE);
        target.setText(message);
    }
}
