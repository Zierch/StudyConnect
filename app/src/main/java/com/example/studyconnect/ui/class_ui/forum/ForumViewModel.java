package com.example.studyconnect.ui.class_ui.forum;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.studyconnect.data.StudyRepository;
import com.example.studyconnect.data.response.ClassResponse;
import com.example.studyconnect.data.response.RegisterResponse;
import com.example.studyconnect.utils.Result;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ForumViewModel extends ViewModel {
    private final StudyRepository repository;

//    @Inject
//    @ApplicationContext
//    Context context;

//    @Inject
//    SharedPreferencesManager sharedPreferencesManager;

    @Inject
    public ForumViewModel(StudyRepository repository) {
        this.repository = repository;
    }

    private final MutableLiveData<ClassResponse> classData = new MutableLiveData<>();
    private final MutableLiveData<RegisterResponse> user = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
//    private final MutableLiveData<Boolean> isSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMsg = new MutableLiveData<>();

//    public LiveData<Boolean> getIsSuccess() {
//        return isSuccess;
//    }

    public LiveData<ClassResponse> getClassData() {
        return classData;
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

    public void fetchClass(int classId) {
        isLoading.postValue(true);

        LiveData<Result<ClassResponse>> classLiveData = repository.getClass(classId);
        classLiveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    Result.Success<ClassResponse> successResult = (Result.Success<ClassResponse>) result;
                    classData.postValue(successResult.getData());

                    isLoading.postValue(false);
                } else if (result instanceof Result.Error<?>) {
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue(errorResult.getError());

                    isLoading.postValue(false);
                }
            }
        });
    }

    public void fetchUser(int userId) {
        isLoading.postValue(true);

        LiveData<Result<RegisterResponse>> userLiveData = repository.getUser(userId);
        userLiveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    isLoading.postValue(false);

                    Result.Success<RegisterResponse> successResult =
                            (Result.Success<RegisterResponse>) result;
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