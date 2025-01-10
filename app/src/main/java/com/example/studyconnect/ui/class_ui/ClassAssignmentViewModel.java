package com.example.studyconnect.ui.class_ui;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.studyconnect.data.StudyRepository;
import com.example.studyconnect.data.response.ClassAssignmentResponse;
import com.example.studyconnect.data.response.RegisterResponse;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class ClassAssignmentViewModel extends ViewModel {
    private final StudyRepository repository;
    private final MutableLiveData<List<ClassAssignmentResponse>> classAssignment = new MutableLiveData<>();
    private final MutableLiveData<RegisterResponse> user = new MutableLiveData<>();

    @Inject
    @ApplicationContext
    Context context;

    @Inject
    public ClassAssignmentViewModel(StudyRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<ClassAssignmentResponse>> getClassAssignment(int classId) {
        fetchClassAssignment(classId);
        return classAssignment;
    }

    public LiveData<RegisterResponse> getUser(int id) {
        fetchUser(id);
        return user;
    }

    private void fetchClassAssignment(int classId) {
        repository.getClassAssignment(classId).enqueue(new Callback<List<ClassAssignmentResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<ClassAssignmentResponse>> call, Response<List<ClassAssignmentResponse>> response) {
                if (response.isSuccessful()) {
                    classAssignment.postValue(response.body());
                    Log.d("KELAS_ASSIGNMENT", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ClassAssignmentResponse>> call, Throwable t) {
                Toast.makeText(context, "Failed to fetch classAssignment: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUser(int id) {
        repository.getUser(id).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    user.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(context, "Failed to fetch user: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
