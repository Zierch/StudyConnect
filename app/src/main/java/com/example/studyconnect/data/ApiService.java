package com.example.studyconnect.data;

import com.example.studyconnect.data.response.ClassResponse;
import com.example.studyconnect.data.response.LoginResponse;
import com.example.studyconnect.data.response.RegisterResponse;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
//    Auth
    @POST("login")
    Call<LoginResponse> signIn(@Body RequestBody body);

    @POST("register")
    Call<RegisterResponse> signUp(@Body RequestBody body);

    @POST("logout")
    Call<ResponseBody> logout();

    @GET("user/{id}")
    Call<RegisterResponse> getUser(@Path("id") int id);

//    Class
    @GET("classes")
    Call<List<ClassResponse>> getClasses();
}

