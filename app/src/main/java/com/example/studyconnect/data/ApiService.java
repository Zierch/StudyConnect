package com.example.studyconnect.data;

import com.example.studyconnect.data.response.LoginResponse;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
//    Auth
    @POST("login")
    Call<LoginResponse> signIn(@Body RequestBody body);

    @POST("register")
    Call<ResponseBody> signUp(@Body RequestBody body);

//    Class

}

