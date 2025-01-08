package com.example.studyconnect.data;

import com.example.studyconnect.data.response.LoginResponse;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class StudyRepository {
    private final ApiService apiService;

    @Inject
    public StudyRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    // method login
    public Call<LoginResponse> signIn(String email, String password) {
        RequestBody body = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .build();

        return apiService.signIn(body);
    }

    // method register
    public Call<ResponseBody> signUp(String name, String email, String password) {
        RequestBody body = new FormBody.Builder()
                .add("name", name)
                .add("email", email)
                .add("password", password)
                .build();

        return apiService.signUp(body);
    }
}
