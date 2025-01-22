package com.example.studyconnect.data;

import com.example.studyconnect.data.response.AnnouncementResponse;
import com.example.studyconnect.data.response.ClassAssignmentResponse;
import com.example.studyconnect.data.response.ClassResponse;
import com.example.studyconnect.data.response.LoginResponse;
import com.example.studyconnect.data.response.MaterialResponse;
import com.example.studyconnect.data.response.RegisterResponse;
import com.example.studyconnect.data.response.SubmissionResponse;
import com.example.studyconnect.data.response.UserResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @GET("classes/{id}")
    Call<ClassResponse> getClass(@Path("id") int id);

    @POST("classes")
    Call<ClassResponse> createClass(@Body RequestBody body);

    @POST("classes/{id}?_method=PUT")
    Call<ClassResponse> updateClass(@Path("id") int id, @Body RequestBody body);

    @DELETE("classes/{id}")
    Call<ResponseBody> deleteClass(@Path("id") int id);

    @GET("classes/{classId}/students")
    Call<List<UserResponse>> getClassStudents(@Path("classId") int classId);

    @POST("classes/{code}/join")
    Call<ResponseBody> joinClass(@Path("code") String code);

    @POST("classes/{code}/leave")
    Call<ResponseBody> leaveClass(@Path("code") String code);

    @POST("archive/{id}")
    Call<ResponseBody> archiveClass(@Path("id") int id);

    @POST("restore/{id}")
    Call<ResponseBody> restoreClass(@Path("id") int id);

    @GET("archived-classes")
    Call<List<ClassResponse>> getArchivedClasses();

    //    Class Assignment
    @GET("assignment/{classId}")
    Call<List<ClassAssignmentResponse>> getClassAssignment(@Path("classId") int classId);

    @GET("assignment/{id}/get")
    Call<ClassAssignmentResponse> getAssignmentById(@Path("id") int id);

    @DELETE("assignment/{id}")
    Call<ResponseBody> deleteAssignment(@Path("id") int id);

    @Multipart
    @POST("assignment/{classId}")
    Call<ClassAssignmentResponse> createAssignment(
            @Path("classId") int classId,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part attachment,
            @Part("link_url") RequestBody link_url,
            @Part("due_date") RequestBody dueDate,
            @Part("max_score") RequestBody maxScore,
            @Part("type") RequestBody typeAssignment,
            @Part("options") RequestBody options,
            @Part("scheduled_at") RequestBody scheduledAt
    );

    @Multipart
    @POST("assignment/{id}?_method=PUT")
    Call<ClassAssignmentResponse> updateAssignment(
            @Path("id") int id,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part attachment,
            @Part("link_url") RequestBody link_url,
            @Part("due_date") RequestBody dueDate,
            @Part("max_score") RequestBody maxScore,
            @Part("type") RequestBody typeAssignment,
            @Part("options") RequestBody options
    );

    //    Materi
    @GET("material/{classId}")
    Call<List<MaterialResponse>> getMaterialByClassId(@Path("classId") int classId);

    @GET("material/{id}/get")
    Call<MaterialResponse> getMaterialById(@Path("id") int id);

    @DELETE("material/{classId}")
    Call<ResponseBody> deleteMaterialById(@Path("classId") int classId);

    @Multipart
    @POST("material/{classId}")
    Call<MaterialResponse> createMaterial(
            @Path("classId") int classId,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part attachment,
            @Part("link_url") RequestBody link_url,
            @Part("teacher_id") RequestBody teacherId
    );

    @Multipart
    @POST("material/{id}?_method=PUT")
    Call<MaterialResponse> updateMaterial(
            @Path("id") int id,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part attachment,
            @Part("link_url") RequestBody link_url,
            @Part("teacher_id") RequestBody teacherId
    );

    //    Announcement
    @Multipart
    @POST("announcement/{classId}")
    Call<ResponseBody> createAnnouncement(
            @Path("classId") int classId,
            @Part("content") RequestBody content,
            @Part("scheduled_at") RequestBody scheduledAt,
            @Part MultipartBody.Part attachment
    );

    @Multipart
    @POST("announcement/{id}?_method=PUT")
    Call<ResponseBody> updateAnnouncement(
            @Path("id") int id,
            @Part("content") RequestBody content,
            @Part MultipartBody.Part attachment
    );

    @DELETE("announcement/{id}")
    Call<ResponseBody> deleteAnnouncement(@Path("id") int id);

    @GET("announcement/{classId}")
    Call<List<AnnouncementResponse>> getAnnouncements(@Path("classId") int classId);

    @GET("announcement/{id}/get")
    Call<AnnouncementResponse> getAnnouncementById(@Path("id") int id);


    //    Submission
    @Multipart
    @POST("assignment/{assignmentId}/submission")
    Call<ResponseBody> createSubmission(
            @Path("assignmentId") int assignmentId,
            @Part("answer") RequestBody content,
            @Part MultipartBody.Part attachment
    );

    @GET("assignment/{assignmentId}/submission")
    Call<List<SubmissionResponse>> getSubmissions(@Path("assignmentId") int assignmentId);

    @GET("submission/{id}/get")
    Call<SubmissionResponse> getSubmissionById(@Path("id") int id);

    @POST("submission/{id}?_method=PUT")
    Call<ResponseBody> scoreSubmission(@Path("id") int id, @Body RequestBody body);

}

