package com.example.studyconnect.ui.auth;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.studyconnect.data.StudyRepository;
import com.example.studyconnect.data.response.LoginResponse;
import com.example.studyconnect.data.response.RegisterResponse;
import com.example.studyconnect.utils.Helper;
import com.example.studyconnect.utils.Result;
import com.example.studyconnect.utils.SharedPreferencesManager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import okhttp3.ResponseBody;

@HiltViewModel
public class AuthViewModel extends ViewModel {
    private final StudyRepository repository;

    @Inject
    @ApplicationContext
    Context context;

    @Inject
    SharedPreferencesManager sharedPreferencesManager;

    @Inject
    public AuthViewModel(StudyRepository repository) {
        this.repository = repository;
    }

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMsg = new MutableLiveData<>();

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getIsSuccess() {
        return isSuccess;
    }

    public LiveData<String> getErrorMsg() {
        return errorMsg;
    }

    public void signIn(String email, String password) {
        isLoading.postValue(true); // Indicate loading started
        isSuccess.postValue(false);

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // Perform the sign-in operation on a background thread
            LiveData<Result<LoginResponse>> signInLiveData = repository.signIn(email, password);

            // Observe the LiveData on the main thread
            new Handler(Looper.getMainLooper()).post(() -> {
                signInLiveData.observeForever(result -> {
                    if (result != null) {
                        if (result instanceof Result.Loading) {
                            isLoading.postValue(true);
                        } else if (result instanceof Result.Success) {
                            isLoading.postValue(false);
                            isSuccess.postValue(true);
                            Result.Success<LoginResponse> successResult = (Result.Success<LoginResponse>) result;
                            sharedPreferencesManager.saveData("token", successResult.getData().getToken());
                            sharedPreferencesManager.saveData("role", successResult.getData().getRole());
                            sharedPreferencesManager.saveData("teacherId", successResult.getData().getUser_id());
                            errorMsg.postValue("Login Berhasil");
                        } else if (result instanceof Result.Error<?>) {
                            isLoading.postValue(false);
                            Result.Error<?> errorResult = (Result.Error<?>) result;
                            errorMsg.postValue(errorResult.getError());
                        }
                    }
                });
            });
        });
    }

    public void signUp(String name, String email, String role, String password) {
        isLoading.postValue(true); // Indicate loading started
        isSuccess.postValue(false);

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // Perform the sign-in operation on a background thread
            LiveData<Result<RegisterResponse>> signUpLiveData = repository.signUp(name, email, role, password);

            // Observe the LiveData on the main thread
            new Handler(Looper.getMainLooper()).post(() -> {
                signUpLiveData.observeForever(result -> {
                    if (result != null) {
                        if (result instanceof Result.Loading) {
                            isLoading.postValue(true);
                        } else if (result instanceof Result.Success) {
                            isLoading.postValue(false);
                            isSuccess.postValue(true);
                            Result.Success<RegisterResponse> successResult = (Result.Success<RegisterResponse>) result;
                            sharedPreferencesManager.saveData("email", successResult.getData().getEmail());
                            errorMsg.postValue("Register Berhasil");
                        } else if (result instanceof Result.Error<?>) {
                            isLoading.postValue(false);
                            Result.Error<?> errorResult = (Result.Error<?>) result;
                            errorMsg.postValue(errorResult.getError());
                        }
                    }
                });
            });
        });
    }


    public void logout() {
        isLoading.postValue(true);
        isSuccess.postValue(false);

        LiveData<Result<ResponseBody>> logoutLiveData = repository.logout();
        logoutLiveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    isLoading.postValue(false);
                    isSuccess.postValue(true);
                    sharedPreferencesManager.removeData();
                    errorMsg.postValue("Logout Berhasil");
                    Helper.startFlagedActivity(SignInActivity.class, context);
                } else if (result instanceof Result.Error<?>) {
                    isLoading.postValue(false);
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue("Logout failed: " + errorResult.getError());
                }
            }
        });
    }

}
