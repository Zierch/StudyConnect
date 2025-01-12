package com.example.studyconnect.ui.auth;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

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
import java.util.concurrent.ScheduledExecutorService;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class AuthViewModel extends ViewModel {
    private final StudyRepository repository;
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Inject
    @ApplicationContext
    Context context;

    @Inject
    SharedPreferencesManager sharedPreferencesManager;

    @Inject
    public AuthViewModel(StudyRepository repository) {
        this.repository = repository;
    }

    private final MutableLiveData<LoginResponse> signInResponse = new MutableLiveData<>();
    private final MutableLiveData<RegisterResponse> signUpResponse = new MutableLiveData<>();

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

    public LiveData<LoginResponse> getSignInResponse() {
        return signInResponse;
    }

    public LiveData<RegisterResponse> getSignUpResponse() {
        return signUpResponse;
    }

    public void signIn(String email, String password) {
        isLoading.postValue(true); // Indicate loading started

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
                            isSuccess.postValue(false);
                        } else if (result instanceof Result.Success) {
                            isLoading.postValue(false);
                            isSuccess.postValue(true);
                            Result.Success<LoginResponse> successResult = (Result.Success<LoginResponse>) result;
                            sharedPreferencesManager.saveData("token", successResult.getData().getToken());
                            sharedPreferencesManager.saveData("role", successResult.getData().getRole());
                            sharedPreferencesManager.saveData("teacherId", successResult.getData().getUser_id());
                            errorMsg.postValue("Login Berhasil");
                        } else if (result instanceof Result.Error) {
                            isLoading.postValue(false);
                            isSuccess.postValue(false);
                            Result.Error errorResult = (Result.Error) result;
                            errorMsg.postValue(errorResult.getError());
                        }
                    }
                });
            });
        });
    }

    public void signUp(String name, String email, String role, String password) {
        repository.signUp(name, email, role, password).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    signUpResponse.setValue(response.body());
                } else {
                    signUpResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                signUpResponse.setValue(null);
            }
        });
    }


    public void logout() {
        repository.logout().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    sharedPreferencesManager.removeData();

                    Toast.makeText(context, "Logout Berhasil", Toast.LENGTH_SHORT).show();
                    Helper.startFlagedActivity(SignInActivity.class, context);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Logout failed : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
