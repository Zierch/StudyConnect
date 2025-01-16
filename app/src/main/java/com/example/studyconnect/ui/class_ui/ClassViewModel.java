package com.example.studyconnect.ui.class_ui;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.studyconnect.data.StudyRepository;
import com.example.studyconnect.data.response.ClassResponse;
import com.example.studyconnect.data.response.RegisterResponse;
import com.example.studyconnect.utils.Result;
import com.example.studyconnect.utils.SharedPreferencesManager;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

@HiltViewModel
public class ClassViewModel extends ViewModel {
    private final StudyRepository repository;

    @Inject
    @ApplicationContext
    Context context;

    @Inject
    SharedPreferencesManager sharedPreferencesManager;

    @Inject
    public ClassViewModel(StudyRepository repository) {
        this.repository = repository;
    }

    private final MutableLiveData<ClassResponse> classData = new MutableLiveData<>();
    private final MutableLiveData<RegisterResponse> user = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMsg = new MutableLiveData<>();

    public LiveData<Boolean> getIsSuccess() {
        return isSuccess;
    }

    public LiveData<ClassResponse> getClassData(int classId) {
        fetchClass(classId);
        return classData;
    }

    public LiveData<RegisterResponse> getUser(int id) {
        fetchUser(id);
        return user;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMsg() {
        return errorMsg;
    }

    public void fetchClass(int classId) {
        isLoading.postValue(true);
        LiveData<Result<ClassResponse>> classLiveData = repository.getClass(classId);
        classLiveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    isLoading.postValue(false);
                    Result.Success<ClassResponse> successResult = (Result.Success<ClassResponse>) result;
                    classData.postValue(successResult.getData());
                } else if (result instanceof Result.Error<?>) {
                    isLoading.postValue(false);
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue(errorResult.getError());
                }
            }
        });
    }

    public void createClass(String name, String subject, String description, int teacherId) {
        isLoading.postValue(true);
        isSuccess.postValue(false);
        RequestBody body = new FormBody.Builder()
                .add("name", name)
                .add("subject", subject)
                .add("description", description)
                .add("teacher_id", String.valueOf(teacherId))
                .build();

        LiveData<Result<ClassResponse>> createClassLiveData = repository.createClass(body);
        createClassLiveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    isLoading.postValue(false);
                    isSuccess.postValue(true);
                    Result.Success<ClassResponse> successResult = (Result.Success<ClassResponse>) result;
                    sharedPreferencesManager.saveData("classId", successResult.getData().getId());
                    errorMsg.postValue("Kelas berhasil dibuat");
                } else if (result instanceof Result.Error<?>) {
                    isLoading.postValue(false);
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue("Gagal membuat kelas: " + errorResult.getError());
                }
            }
        });
    }

    public void updateClass(int id, String name, String subject, String description, String code) {
        isLoading.postValue(true);
        isSuccess.postValue(false);
        RequestBody body = new FormBody.Builder()
                .add("name", name)
                .add("subject", subject)
                .add("description", description)
                .add("code", code)
                .build();

        LiveData<Result<ClassResponse>> updateClassLiveData = repository.updateClass(id, body);
        updateClassLiveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    isLoading.postValue(false);
                    isSuccess.postValue(true);
                    errorMsg.postValue("Kelas berhasil diperbarui");
                } else if (result instanceof Result.Error<?>) {
                    isLoading.postValue(false);
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue("Gagal memperbarui kelas: " + errorResult.getError());
                }
            }
        });
    }

    public void joinClass(String code) {
        isLoading.postValue(true);
        isSuccess.postValue(false);

        LiveData<Result<ResponseBody>> joinClassLiveData = repository.joinClass(code);
        joinClassLiveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    errorMsg.postValue("Berhasil bergabung ke kelas");
                    isSuccess.postValue(true);
                    isLoading.postValue(false);
                } else if (result instanceof Result.Error<?>) {
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue("Gagal bergabung ke kelas: " + errorResult.getError());

                    isLoading.postValue(false);
                }
            }
        });
    }

    public void restoreClass(int id) {
        isSuccess.postValue(false);
        LiveData<Result<ResponseBody>> restoreClassLiveData = repository.restoreClass(id);
        restoreClassLiveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    isLoading.postValue(false);
                    isSuccess.postValue(true);

                    errorMsg.postValue("Kelas berhasil dipulihkan");
                } else if (result instanceof Result.Error<?>) {
                    isLoading.postValue(false);

                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue("Gagal memulihkan kelas: " + errorResult.getError());
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
                    isLoading.postValue(false);
                    Result.Success<RegisterResponse> successResult = (Result.Success<RegisterResponse>) result;
                    user.postValue(successResult.getData());
                } else if (result instanceof Result.Error<?>) {
                    isLoading.postValue(false);
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue(errorResult.getError());
                }
            }
        });
    }
}