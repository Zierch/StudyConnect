package com.example.studyconnect.ui.class_ui;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.studyconnect.data.StudyRepository;
import com.example.studyconnect.data.response.ClassResponse;
import com.example.studyconnect.data.response.RegisterResponse;
import com.example.studyconnect.utils.SharedPreferencesManager;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class ClassViewModel extends ViewModel {
    private final StudyRepository repository;
    private final MutableLiveData<List<RegisterResponse>> students = new MutableLiveData<>();
    private final MutableLiveData<ClassResponse> classes = new MutableLiveData<>();
    private final MutableLiveData<RegisterResponse> user = new MutableLiveData<>();
    private final MutableLiveData<Boolean> createClassResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> updateClassResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> restoreClassResult = new MutableLiveData<>();

    @Inject
    @ApplicationContext
    Context context;

    @Inject
    SharedPreferencesManager sharedPreferencesManager;

    @Inject
    public ClassViewModel(StudyRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<RegisterResponse>> getStudents() {
        return students;
    }

    public LiveData<Boolean> getCreateClassResult() {
        return createClassResult;
    }

    public LiveData<ClassResponse> getClassResult() {
        return classes;
    }

    public LiveData<RegisterResponse> getTeacher(int id) {
        fetchUser(id);
        return user;
    }

    public LiveData<Boolean> getUpdateClassResult() {
        return updateClassResult;
    }

    public LiveData<Boolean> getRestoreClassResult() {
        return restoreClassResult;
    }

    public LiveData<ClassResponse> getClass(int classId) {
        MutableLiveData<ClassResponse> classData = new MutableLiveData<>();

        repository.getClass(classId).enqueue(new Callback<ClassResponse>() {
            @Override
            public void onResponse(
                    @NonNull Call<ClassResponse> call,
                    @NonNull Response<ClassResponse> response
            ) {
                if (response.isSuccessful()) {
                    classData.setValue(response.body());
                } else {
                    classData.setValue(null);
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<ClassResponse> call,
                    @NonNull Throwable t
            ) {
                Log.d("getClass", "onFailure: " + t.getMessage());
                classData.setValue(null);
            }
        });

        return classData;
    }

    public void createClass(String name, String subject, String description, int teacherId) {
        RequestBody body = new FormBody.Builder()
                .add("name", name)
                .add("subject", subject)
                .add("description", description)
                .add("teacher_id", String.valueOf(teacherId))
                .build();

        repository.createClass(body).enqueue(new Callback<ClassResponse>() {
            @Override
            public void onResponse(
                    @NonNull Call<ClassResponse> call,
                    @NonNull Response<ClassResponse> response
            ) {
                assert response.body() != null;
                sharedPreferencesManager.saveData("classId", response.body().getId());
                Toast.makeText(context, "Kelas berhasil dibuat", Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()) {
                    createClassResult.setValue(true);
                } else {
                    createClassResult.setValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ClassResponse> call, @NonNull Throwable t) {
                Log.d("createClass", "onFailure: " + t.getMessage());
                Toast.makeText(context, "Failed to create class", Toast.LENGTH_SHORT).show();
                createClassResult.setValue(false);
            }
        });
    }

    public void updateClass(int id, String name, String subject, String description, String code) {
        RequestBody body = new FormBody.Builder()
                .add("name", name)
                .add("subject", subject)
                .add("description", description)
                .add("code", code)
                .build();

        repository.updateClass(id, body).enqueue(new Callback<ClassResponse>() {
            @Override
            public void onResponse(
                    @NonNull Call<ClassResponse> call,
                    @NonNull Response<ClassResponse> response
            ) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Kelas berhasil diperbarui", Toast.LENGTH_SHORT).show();
                    if (response.isSuccessful()) {
                        updateClassResult.setValue(true);
                    } else {
                        updateClassResult.setValue(false);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ClassResponse> call, @NonNull Throwable t) {
                Log.d("updateClass", "onFailure: " + t.getMessage());
                Toast.makeText(context, "Failed to update class", Toast.LENGTH_SHORT).show();
                updateClassResult.setValue(false);
            }
        });
    }

    public void fetchClass(int classId){
        repository.getClass(classId).enqueue(new Callback<ClassResponse>() {
            @Override
            public void onResponse(
                    @NonNull Call<ClassResponse> call,
                    @NonNull Response<ClassResponse> response
            ) {
                if (response.isSuccessful()) {
                    classes.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ClassResponse> call, @NonNull Throwable t) {
                Toast.makeText(context, "Failed to fetch students: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchStudents(int classId) {
        repository.getClassStudents(classId).enqueue(new Callback<List<RegisterResponse>>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<RegisterResponse>> call,
                    @NonNull Response<List<RegisterResponse>> response
            ) {
                if (response.isSuccessful()) {
                    students.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<RegisterResponse>> call, @NonNull Throwable t) {
                Toast.makeText(context, "Failed to fetch students: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void restoreClass(int id) {
        repository.restoreClass(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(
                    @NonNull Call<ResponseBody> call,
                    @NonNull Response<ResponseBody> response
            ) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Kelas berhasil dipulihkan", Toast.LENGTH_SHORT).show();
                    if (response.isSuccessful()) {
                        restoreClassResult.setValue(true);
                    } else {
                        restoreClassResult.setValue(false);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.d("restoreClass", "onFailure: " + t.getMessage());
                Toast.makeText(context, "Gagal memulihkan kelas", Toast.LENGTH_SHORT).show();
                restoreClassResult.setValue(false);
            }
        });
    }

    private void fetchUser(int id) {
        repository.getUser(id).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(
                    @NonNull Call<RegisterResponse> call,
                    @NonNull Response<RegisterResponse> response
            ) {
                if (response.isSuccessful()) {
                    user.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                Toast.makeText(context, "Failed to fetch user: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
