package com.example.studyconnect.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studyconnect.data.response.ClassAssignmentResponse;
import com.example.studyconnect.data.response.ClassResponse;
import com.example.studyconnect.data.response.LoginResponse;
import com.example.studyconnect.data.response.RegisterResponse;
import com.example.studyconnect.utils.Result;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyRepository {
    private final ApiService apiService;

    @Inject
    public StudyRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    // method login
    public LiveData<Result<LoginResponse>> signIn(String email, String password) {
        MutableLiveData<Result<LoginResponse>> resultLiveData = new MutableLiveData<>();

        // Create the request object
        RequestBody body = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .build();

        Call<LoginResponse> call = apiService.signIn(body);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Success case
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    // Handle HTTP error responses (e.g., 400, 401)
                    String errorMessage = "Login gagal : Email atau Password anda salah!";
                    resultLiveData.postValue(new Result.Error<>(errorMessage));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Handle network-level failures (e.g., no internet connection)
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // method register
    public Call<RegisterResponse> signUp(String name, String email, String role, String password) {
        RequestBody body = new FormBody.Builder()
                .add("name", name)
                .add("email", email)
                .add("role", role)
                .add("password", password)
                .build();

        return apiService.signUp(body);
    }

    // method logout
    public Call<ResponseBody> logout() {
        return apiService.logout();
    }

    // method get user
    public Call<RegisterResponse> getUser(int id) {
        return apiService.getUser(id);
    }

    // method get class
    public Call<List<ClassResponse>> getClasses() {
        return apiService.getClasses();
    }

    // method get class w id
    public Call<ClassResponse> getClass(int id) {
        return apiService.getClass(id);
    }

    // method create class
    public Call<ClassResponse> createClass(RequestBody body) {
        return apiService.createClass(body);
    }

    // method update class
    public Call<ClassResponse> updateClass(int id, RequestBody body) {
        return apiService.updateClass(id, body);
    }

    // method delete class
    public Call<ResponseBody> deleteClass(int id) {
        return apiService.deleteClass(id);
    }

    // method get class students
    public Call<List<RegisterResponse>> getClassStudents(int id) {
        return apiService.getClassStudents(id);
    }

    // method join class
    public Call<ResponseBody> joinClass(int id) {
        return apiService.joinClass(id);
    }

    // method leave class
    public Call<ResponseBody> leaveClass(int id) {
        return apiService.leaveClass(id);
    }


    // method arsipkan class
    public Call<ResponseBody> archiveClass(int id) {
        return apiService.archiveClass(id);
    }

    // method pulihkan class
    public Call<ResponseBody> restoreClass(int id) {
        return apiService.restoreClass(id);
    }

    // method get class yang diarsipkan
    public Call<List<ClassResponse>> getArchivedClasses() {
        return apiService.getArchivedClasses();
    }

    // method get assignment by class id
    public Call<List<ClassAssignmentResponse>> getClassAssignment(int classId) {
        return apiService.getClassAssignment(classId);
    }

    // method hapus tugas
    public Call<ResponseBody> deleteAssignment(int id) {
        return apiService.deleteAssignment(id);
    }

}
