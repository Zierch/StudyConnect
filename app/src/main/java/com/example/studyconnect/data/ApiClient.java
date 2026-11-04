package com.example.studyconnect.data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit tunggal untuk seluruh aplikasi.
 *
 * <p>{@code 10.0.2.2} adalah alamat mesin host bila dilihat dari emulator Android;
 * ganti dengan IP LAN mesin saat memakai perangkat fisik.
 */
public final class ApiClient {

    private static final String BASE_URL = "http://10.0.2.2:8000/api/";

    private static Retrofit retrofit;
    private static ApiService service;

    private ApiClient() {
    }

    public static synchronized ApiService service() {
        if (service == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = retrofit.create(ApiService.class);
        }
        return service;
    }
}
