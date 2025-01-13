package com.example.studyconnect.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studyconnect.data.response.ClassAssignmentResponse;
import com.example.studyconnect.data.response.ClassResponse;
import com.example.studyconnect.data.response.LoginResponse;
import com.example.studyconnect.data.response.RegisterResponse;
import com.example.studyconnect.utils.Result;

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
            public void onResponse(@NonNull Call<List<ClassResponse>> call, @NonNull Response<List<ClassResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to get classes"));
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
    public LiveData<Result<List<RegisterResponse>>> getClassStudents(int id) {
        MutableLiveData<Result<List<RegisterResponse>>> resultLiveData = new MutableLiveData<>();

        Call<List<RegisterResponse>> call = apiService.getClassStudents(id);
        call.enqueue(new Callback<List<RegisterResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<RegisterResponse>> call, @NonNull Response<List<RegisterResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result.Success<>(response.body()));
                } else {
                    resultLiveData.postValue(new Result.Error<>("Failed to get class students"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<RegisterResponse>> call, @NonNull Throwable t) {
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
}
