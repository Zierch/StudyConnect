package com.example.studyconnect.ui.class_ui.assignment;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.studyconnect.data.StudyRepository;
import com.example.studyconnect.data.response.ClassAssignmentResponse;
import com.example.studyconnect.data.response.RegisterResponse;
import com.example.studyconnect.utils.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import okhttp3.ResponseBody;

@HiltViewModel
public class ClassAssignmentViewModel extends ViewModel {
    private final StudyRepository repository;

    @Inject
    @ApplicationContext
    Context context;

    @Inject
    public ClassAssignmentViewModel(StudyRepository repository) {
        this.repository = repository;
    }

    private final MutableLiveData<List<ClassAssignmentResponse>> classAssignment = new MutableLiveData<>();
    private final MutableLiveData<RegisterResponse> user = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMsg = new MutableLiveData<>();

    public LiveData<List<ClassAssignmentResponse>> getClassAssignment() {
        return classAssignment;
    }

    public LiveData<RegisterResponse> getUser() {
        return user;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMsg() {
        return errorMsg;
    }

    public void fetchClassAssignment(int classId) {
        isLoading.postValue(true);

        LiveData<Result<List<ClassAssignmentResponse>>> classAssignmentLiveData = repository.getClassAssignment(classId);
        classAssignmentLiveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    Result.Success<List<ClassAssignmentResponse>> successResult = (Result.Success<List<ClassAssignmentResponse>>) result;
                    classAssignment.postValue(successResult.getData());

                    isLoading.postValue(false);
                } else if (result instanceof Result.Error<?>) {
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue(errorResult.getError());

                    isLoading.postValue(false);
                }
            }
        });
    }

    public void fetchUser(int id) {
        isLoading.postValue(true);

        LiveData<Result<RegisterResponse>> userLiveData = repository.getUser(id);
        userLiveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    Result.Success<RegisterResponse> successResult = (Result.Success<RegisterResponse>) result;
                    user.postValue(successResult.getData());

                    isLoading.postValue(false);
                } else if (result instanceof Result.Error<?>) {
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue(errorResult.getError());

                    isLoading.postValue(false);
                }
            }
        });
    }

    public void deleteAssignment(int id, int classId) {
        isLoading.postValue(true);

        LiveData<Result<ResponseBody>> deleteAssignmentLiveData = repository.deleteAssignment(id);
        deleteAssignmentLiveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    errorMsg.postValue("Tugas berhasil dihapus");
                    fetchClassAssignment(classId);

                    isLoading.postValue(false);
                } else if (result instanceof Result.Error<?>) {
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue("Gagal menghapus tugas: " + errorResult.getError());

                    isLoading.postValue(false);
                }
            }
        });
    }
}
