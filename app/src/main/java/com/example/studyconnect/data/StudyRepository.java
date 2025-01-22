package com.example.studyconnect.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studyconnect.R;
import com.example.studyconnect.data.response.AnnouncementResponse;
import com.example.studyconnect.data.response.ClassAssignmentResponse;
import com.example.studyconnect.data.response.ClassResponse;
import com.example.studyconnect.data.response.LoginResponse;
import com.example.studyconnect.data.response.MaterialResponse;
import com.example.studyconnect.data.response.RegisterResponse;
import com.example.studyconnect.data.response.SubmissionResponse;
import com.example.studyconnect.data.response.UserResponse;
import com.example.studyconnect.utils.Result;

import java.util.List;

import javax.inject.Inject;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyRepository {
    private final ApiService apiService;
    private final Context myApp;

    @Inject
    public StudyRepository(
            ApiService apiService,
            Context myApp
    ) {
        this.apiService = apiService;
        this.myApp = myApp;
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
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
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
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                // Handle network-level failures (e.g., no internet connection)
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // method register
    public LiveData<Result<RegisterResponse>> signUp(String name, String email, String role, String password) {
        MutableLiveData<Result<RegisterResponse>> resultLiveData = new MutableLiveData<>();

        RequestBody body = new FormBody.Builder()
                .add("name", name)
                .add("email", email)
                .add("role", role)
                .add("password", password)
                .build();

        Call<RegisterResponse> call = apiService.signUp(body);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegisterResponse> call, @NonNull Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Success case
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    // Handle HTTP error responses (e.g., 400, 401)
                    String errorMessage = "Register gagal : Email sudah Terdaftar!";
                    resultLiveData.postValue(new Result.Error<>(errorMessage));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                // Handle network-level failures (e.g., no internet connection)
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method logout
    public LiveData<Result<ResponseBody>> logout() {
        MutableLiveData<Result<ResponseBody>> resultLiveData = new MutableLiveData<>();

        Call<ResponseBody> call = apiService.logout();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Logout failed"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method get user
    public LiveData<Result<RegisterResponse>> getUser(int id) {
        MutableLiveData<Result<RegisterResponse>> resultLiveData = new MutableLiveData<>();

        Call<RegisterResponse> call = apiService.getUser(id);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegisterResponse> call, @NonNull Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to get user"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method get classes
    public LiveData<Result<List<ClassResponse>>> getClasses() {
        MutableLiveData<Result<List<ClassResponse>>> resultLiveData = new MutableLiveData<>();

        Call<List<ClassResponse>> call = apiService.getClasses();
        call.enqueue(new Callback<List<ClassResponse>>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<ClassResponse>> call,
                    @NonNull Response<List<ClassResponse>> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    if (response.code() == 500) {
                        resultLiveData.postValue(
                                new Result.Error<>(
                                        myApp.getString(R.string.unauthorized)
                                )
                        );
                    } else resultLiveData.postValue(new Result.Error<>("Failed to get classes"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ClassResponse>> call, @NonNull Throwable t) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method get class by id
    public LiveData<Result<ClassResponse>> getClass(int id) {
        MutableLiveData<Result<ClassResponse>> resultLiveData = new MutableLiveData<>();

        Call<ClassResponse> call = apiService.getClass(id);
        call.enqueue(new Callback<ClassResponse>() {
            @Override
            public void onResponse(@NonNull Call<ClassResponse> call, @NonNull Response<ClassResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to get class"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ClassResponse> call, @NonNull Throwable t) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method create class
    public LiveData<Result<ClassResponse>> createClass(RequestBody body) {
        MutableLiveData<Result<ClassResponse>> resultLiveData = new MutableLiveData<>();

        Call<ClassResponse> call = apiService.createClass(body);
        call.enqueue(new Callback<ClassResponse>() {
            @Override
            public void onResponse(@NonNull Call<ClassResponse> call, @NonNull Response<ClassResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to create class"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ClassResponse> call, @NonNull Throwable t) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method update class
    public LiveData<Result<ClassResponse>> updateClass(int id, RequestBody body) {
        MutableLiveData<Result<ClassResponse>> resultLiveData = new MutableLiveData<>();

        Call<ClassResponse> call = apiService.updateClass(id, body);
        call.enqueue(new Callback<ClassResponse>() {
            @Override
            public void onResponse(@NonNull Call<ClassResponse> call, @NonNull Response<ClassResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to update class"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ClassResponse> call, @NonNull Throwable t) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method delete class
    public LiveData<Result<ResponseBody>> deleteClass(int id) {
        MutableLiveData<Result<ResponseBody>> resultLiveData = new MutableLiveData<>();

        Call<ResponseBody> call = apiService.deleteClass(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to delete class"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method get class students
    public LiveData<Result<List<UserResponse>>> getClassStudents(int classId) {
        MutableLiveData<Result<List<UserResponse>>> resultLiveData = new MutableLiveData<>();

        Call<List<UserResponse>> call = apiService.getClassStudents(classId);
        call.enqueue(new Callback<List<UserResponse>>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<UserResponse>> call,
                    @NonNull Response<List<UserResponse>> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to get class students"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UserResponse>> call, @NonNull Throwable t) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method join class
    public LiveData<Result<ResponseBody>> joinClass(String code) {
        MutableLiveData<Result<ResponseBody>> resultLiveData = new MutableLiveData<>();

        Call<ResponseBody> call = apiService.joinClass(code);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to join class"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method leave class
    public LiveData<Result<ResponseBody>> leaveClass(String code) {
        MutableLiveData<Result<ResponseBody>> resultLiveData = new MutableLiveData<>();

        Call<ResponseBody> call = apiService.leaveClass(code);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to leave class"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method archive class
    public LiveData<Result<ResponseBody>> archiveClass(int id) {
        MutableLiveData<Result<ResponseBody>> resultLiveData = new MutableLiveData<>();

        Call<ResponseBody> call = apiService.archiveClass(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to archive class"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method restore class
    public LiveData<Result<ResponseBody>> restoreClass(int id) {
        MutableLiveData<Result<ResponseBody>> resultLiveData = new MutableLiveData<>();

        Call<ResponseBody> call = apiService.restoreClass(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to restore class"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method get archived classes
    public LiveData<Result<List<ClassResponse>>> getArchivedClasses() {
        MutableLiveData<Result<List<ClassResponse>>> resultLiveData = new MutableLiveData<>();

        Call<List<ClassResponse>> call = apiService.getArchivedClasses();
        call.enqueue(new Callback<List<ClassResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<ClassResponse>> call, @NonNull Response<List<ClassResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to get archived classes"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ClassResponse>> call, @NonNull Throwable t) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method get class assignment by class id
    public LiveData<Result<List<ClassAssignmentResponse>>> getClassAssignment(int classId) {
        MutableLiveData<Result<List<ClassAssignmentResponse>>> resultLiveData = new MutableLiveData<>();

        Call<List<ClassAssignmentResponse>> call = apiService.getClassAssignment(classId);
        call.enqueue(new Callback<List<ClassAssignmentResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<ClassAssignmentResponse>> call, @NonNull Response<List<ClassAssignmentResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to get class assignments"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ClassAssignmentResponse>> call, @NonNull Throwable t) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method get assignment by id
    public LiveData<Result<ClassAssignmentResponse>> getAssignmentById(int id) {
        MutableLiveData<Result<ClassAssignmentResponse>> resultLiveData = new MutableLiveData<>();

        Call<ClassAssignmentResponse> call = apiService.getAssignmentById(id);
        call.enqueue(new Callback<ClassAssignmentResponse>() {
            @Override
            public void onResponse(
                    @NonNull Call<ClassAssignmentResponse> call,
                    @NonNull Response<ClassAssignmentResponse> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to get assignments"));
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<ClassAssignmentResponse> call,
                    @NonNull Throwable t
            ) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method create assignment
    public LiveData<Result<ClassAssignmentResponse>> createAssignment(
            int classId, RequestBody title, RequestBody desc, MultipartBody.Part attachment,
            RequestBody link, RequestBody dueDate, RequestBody points, RequestBody typeAssignment,
            RequestBody options, RequestBody scheduleAt
    ) {
        MutableLiveData<Result<ClassAssignmentResponse>> resultLiveData = new MutableLiveData<>();

        Call<ClassAssignmentResponse> call = apiService.createAssignment(
                classId, title, desc, attachment, link, dueDate, points,
                typeAssignment, options, scheduleAt
        );
        call.enqueue(new Callback<ClassAssignmentResponse>() {
            @Override
            public void onResponse(
                    @NonNull Call<ClassAssignmentResponse> call,
                    @NonNull Response<ClassAssignmentResponse> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to create assignment"));
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<ClassAssignmentResponse> call,
                    @NonNull Throwable t
            ) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });

        return resultLiveData;
    }

    // Method update assignment
    public LiveData<Result<ClassAssignmentResponse>> updateAssignment(
            int id, RequestBody title, RequestBody desc, MultipartBody.Part attachment,
            RequestBody link, RequestBody dueDate, RequestBody points, RequestBody typeAssignment,
            RequestBody options
    ) {
        MutableLiveData<Result<ClassAssignmentResponse>> resultLiveData = new MutableLiveData<>();

        Call<ClassAssignmentResponse> call = apiService.updateAssignment(
                id, title, desc, attachment, link, dueDate, points, typeAssignment, options
        );
        call.enqueue(new Callback<ClassAssignmentResponse>() {
            @Override
            public void onResponse(
                    @NonNull Call<ClassAssignmentResponse> call,
                    @NonNull Response<ClassAssignmentResponse> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to update assignment"));
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<ClassAssignmentResponse> call,
                    @NonNull Throwable t
            ) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method delete assignment
    public LiveData<Result<ResponseBody>> deleteAssignment(int id) {
        MutableLiveData<Result<ResponseBody>> resultLiveData = new MutableLiveData<>();

        Call<ResponseBody> call = apiService.deleteAssignment(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to delete assignment"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method get class assignment by class id
    public LiveData<Result<List<MaterialResponse>>> getMaterialByClassId(int classId) {
        MutableLiveData<Result<List<MaterialResponse>>> resultLiveData = new MutableLiveData<>();

        Call<List<MaterialResponse>> call = apiService.getMaterialByClassId(classId);
        call.enqueue(new Callback<List<MaterialResponse>>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<MaterialResponse>> call,
                    @NonNull Response<List<MaterialResponse>> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to get materials"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MaterialResponse>> call, @NonNull Throwable t) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method get assignment by id
    public LiveData<Result<MaterialResponse>> getMaterialById(int id) {
        MutableLiveData<Result<MaterialResponse>> resultLiveData = new MutableLiveData<>();

        Call<MaterialResponse> call = apiService.getMaterialById(id);
        call.enqueue(new Callback<MaterialResponse>() {
            @Override
            public void onResponse(
                    @NonNull Call<MaterialResponse> call,
                    @NonNull Response<MaterialResponse> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to get material"));
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<MaterialResponse> call,
                    @NonNull Throwable t
            ) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method create assignment
    public LiveData<Result<MaterialResponse>> createMaterial(
            int classId, RequestBody title, RequestBody desc,
            MultipartBody.Part attachment, RequestBody link, RequestBody teacherId
    ) {
        MutableLiveData<Result<MaterialResponse>> resultLiveData = new MutableLiveData<>();

        Call<MaterialResponse> call = apiService.createMaterial(
                classId, title, desc, attachment, link, teacherId
        );
        call.enqueue(new Callback<MaterialResponse>() {
            @Override
            public void onResponse(
                    @NonNull Call<MaterialResponse> call,
                    @NonNull Response<MaterialResponse> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to create material"));
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<MaterialResponse> call,
                    @NonNull Throwable t
            ) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method update assignment
    public LiveData<Result<MaterialResponse>> updateMaterial(
            int id, RequestBody title, RequestBody desc,
            MultipartBody.Part attachment, RequestBody link, RequestBody teacherId
    ) {
        MutableLiveData<Result<MaterialResponse>> resultLiveData = new MutableLiveData<>();

        Call<MaterialResponse> call = apiService.updateMaterial(
                id, title, desc, attachment, link, teacherId
        );
        call.enqueue(new Callback<MaterialResponse>() {
            @Override
            public void onResponse(
                    @NonNull Call<MaterialResponse> call,
                    @NonNull Response<MaterialResponse> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to update material"));
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<MaterialResponse> call,
                    @NonNull Throwable t
            ) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method delete assignment
    public LiveData<Result<ResponseBody>> deleteMaterial(int id) {
        MutableLiveData<Result<ResponseBody>> resultLiveData = new MutableLiveData<>();

        Call<ResponseBody> call = apiService.deleteMaterialById(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(
                    @NonNull Call<ResponseBody> call,
                    @NonNull Response<ResponseBody> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to delete material"));
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<ResponseBody> call,
                    @NonNull Throwable t
            ) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method create announcement
    public LiveData<Result<ResponseBody>> createAnnouncement(
            int classId,
            RequestBody content,
            RequestBody scheduledAt,
            MultipartBody.Part attachment) {
        MutableLiveData<Result<ResponseBody>> resultLiveData = new MutableLiveData<>();

        Call<ResponseBody> call = apiService.createAnnouncement(classId, content, scheduledAt, attachment);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to create announcement"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method update announcement
    public LiveData<Result<ResponseBody>> updateAnnouncement(
            int id,
            RequestBody content,
            MultipartBody.Part attachment
    ) {
        MutableLiveData<Result<ResponseBody>> resultLiveData = new MutableLiveData<>();

        // Panggil API service
        Call<ResponseBody> call = apiService.updateAnnouncement(id, content, attachment);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to update announcement"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method delete announcement
    public LiveData<Result<ResponseBody>> deleteAnnouncement(int id) {
        MutableLiveData<Result<ResponseBody>> resultLiveData = new MutableLiveData<>();

        Call<ResponseBody> call = apiService.deleteAnnouncement(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to delete announcement"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method get announcement
    public LiveData<Result<List<AnnouncementResponse>>> getAnnouncements(int classId) {
        MutableLiveData<Result<List<AnnouncementResponse>>> resultLiveData = new MutableLiveData<>();

        Call<List<AnnouncementResponse>> call = apiService.getAnnouncements(classId);
        call.enqueue(new Callback<List<AnnouncementResponse>>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<AnnouncementResponse>> call,
                    @NonNull Response<List<AnnouncementResponse>> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to get announcement"));
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<List<AnnouncementResponse>> call,
                    @NonNull Throwable t
            ) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method get announcementById
    public LiveData<Result<AnnouncementResponse>> getAnnouncementById(int id) {
        MutableLiveData<Result<AnnouncementResponse>> resultLiveData = new MutableLiveData<>();

        Call<AnnouncementResponse> call = apiService.getAnnouncementById(id);
        call.enqueue(new Callback<AnnouncementResponse>() {
            @Override
            public void onResponse(@NonNull Call<AnnouncementResponse> call, @NonNull Response<AnnouncementResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to get announcement"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnnouncementResponse> call, @NonNull Throwable t) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method create submission
    public LiveData<Result<ResponseBody>> createSubmission(int assignmentId, RequestBody content, MultipartBody.Part attachment) {
        MutableLiveData<Result<ResponseBody>> resultLiveData = new MutableLiveData<>();

        Call<ResponseBody> call = apiService.createSubmission(assignmentId, content, attachment);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to create submission"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method get submissions
    public LiveData<Result<List<SubmissionResponse>>> getSubmissions(int assignmentId) {
        MutableLiveData<Result<List<SubmissionResponse>>> resultLiveData = new MutableLiveData<>();

        Call<List<SubmissionResponse>> call = apiService.getSubmissions(assignmentId);
        call.enqueue(new Callback<List<SubmissionResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<SubmissionResponse>> call, @NonNull Response<List<SubmissionResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to get submissions"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SubmissionResponse>> call, @NonNull Throwable t) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method get submission by ID
    public LiveData<Result<SubmissionResponse>> getSubmissionById(int id) {
        MutableLiveData<Result<SubmissionResponse>> resultLiveData = new MutableLiveData<>();

        Call<SubmissionResponse> call = apiService.getSubmissionById(id);
        call.enqueue(new Callback<SubmissionResponse>() {
            @Override
            public void onResponse(@NonNull Call<SubmissionResponse> call, @NonNull Response<SubmissionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to get submission"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<SubmissionResponse> call, @NonNull Throwable t) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    // Method score submission
    public LiveData<Result<ResponseBody>> scoreSubmission(int id, RequestBody body) {
        MutableLiveData<Result<ResponseBody>> resultLiveData = new MutableLiveData<>();

        Call<ResponseBody> call = apiService.scoreSubmission(id, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to score submission"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                resultLiveData.postValue(new Result.Error<>("Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }
}
