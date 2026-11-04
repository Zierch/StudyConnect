package com.example.studyconnect.data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/** Endpoint StudyConnect-API yang dipakai aplikasi. */
public interface ApiService {

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> login(@Field("email") String email, @Field("password") String password);

    @GET("user/{id}")
    Call<UserItem> getUser(@Header("Authorization") String token, @Path("id") int userId);

    @GET("classes")
    Call<List<ClassItem>> getClasses(@Header("Authorization") String token);

    @GET("classes/{id}/students")
    Call<List<UserItem>> getStudents(@Header("Authorization") String token, @Path("id") int classId);

    @GET("assignment/{classId}")
    Call<List<AssignmentItem>> getAssignments(@Header("Authorization") String token,
                                              @Path("classId") int classId);

    @GET("announcement/{classId}")
    Call<List<AnnouncementItem>> getAnnouncements(@Header("Authorization") String token,
                                                  @Path("classId") int classId);
}
