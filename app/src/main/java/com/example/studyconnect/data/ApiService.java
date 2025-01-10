package com.example.studyconnect.data;

import com.example.studyconnect.data.response.ClassAssignmentResponse;
import com.example.studyconnect.data.response.ClassResponse;
import com.example.studyconnect.data.response.LoginResponse;
import com.example.studyconnect.data.response.RegisterResponse;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

//    Class Assignment
    @GET("assignment/{classId}")
    Call<List<ClassAssignmentResponse>> getClassAssignment(@Path("classId") int classId);

    @GET("classes/{id}")
    Call<ClassResponse> getClass(@Path("id") int id);

    @POST("classes")
    Call<ClassResponse> createClass(@Body RequestBody body);

    @POST("classes/{id}?_method=PUT")
    Call<ClassResponse> updateClass(@Path("id") int id, @Body RequestBody body);

    @DELETE("classes/{id}")
    Call<ResponseBody> deleteClass(@Path("id") int id);

    @GET("classes/{id}/students")
    Call<List<RegisterResponse>> getClassStudents(@Path("id") int id);

    @POST("classes/{id}/join")
    Call<ResponseBody> joinClass(@Path("id") int id);

    @POST("classes/{id}/leave")
    Call<ResponseBody> leaveClass(@Path("id") int id);
}

