package com.example.studyconnect.ui.class_ui;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
    private final MutableLiveData<Boolean> createClassResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> updateClassResult = new MutableLiveData<>();

    @Inject
    @ApplicationContext
    Context context;

    @Inject
    SharedPreferencesManager sharedPreferencesManager;

    @Inject
    public ClassViewModel(StudyRepository repository) {
        this.repository = repository;
        fetchStudents();
    }

    public LiveData<List<RegisterResponse>> getStudents() {
        return students;
    }

    public LiveData<Boolean> getCreateClassResult() {
        return createClassResult;
    }

    public LiveData<Boolean> getUpdateClassResult() {
        return updateClassResult;
    }

    public LiveData<ClassResponse> getClass(int classId) {
        MutableLiveData<ClassResponse> classData = new MutableLiveData<>();

        repository.getClass(classId).enqueue(new Callback<ClassResponse>() {
            @Override
            public void onResponse(Call<ClassResponse> call, Response<ClassResponse> response) {
                if (response.isSuccessful()) {
                    classData.setValue(response.body());
                } else {
                    classData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ClassResponse> call, Throwable t) {
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
            public void onResponse(Call<ClassResponse> call, Response<ClassResponse> response) {
                sharedPreferencesManager.saveData("classId", response.body().getId());
                Toast.makeText(context, "Kelas berhasil dibuat", Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()) {
                    createClassResult.setValue(true);
                } else {
                    createClassResult.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<ClassResponse> call, Throwable t) {
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
            public void onResponse(Call<ClassResponse> call, Response<ClassResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Kelas berhasil diperbarui", Toast.LENGTH_SHORT).show();
                    if (response.isSuccessful()) {
                        createClassResult.setValue(true);
                    } else {
                        createClassResult.setValue(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<ClassResponse> call, Throwable t) {
                Log.d("updateClass", "onFailure: " + t.getMessage());
                Toast.makeText(context, "Failed to update class", Toast.LENGTH_SHORT).show();
                createClassResult.setValue(false);
            }
        });
    }

    public Call<List<RegisterResponse>> getClassStudents(int id) {
        return repository.getClassStudents(id);
    }

    private void fetchStudents() {
        repository.getClassStudents(1).enqueue(new Callback<List<RegisterResponse>>() {
            @Override
            public void onResponse(Call<List<RegisterResponse>> call, Response<List<RegisterResponse>> response) {
                if (response.isSuccessful()) {
                    students.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<RegisterResponse>> call, Throwable t) {
                Toast.makeText(context, "Failed to fetch students: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
