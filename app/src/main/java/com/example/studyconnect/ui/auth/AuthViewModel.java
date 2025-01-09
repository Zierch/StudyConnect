package com.example.studyconnect.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.studyconnect.data.StudyRepository;
import com.example.studyconnect.data.response.LoginResponse;
import com.example.studyconnect.data.response.RegisterResponse;
import com.example.studyconnect.ui.home.Drawer;
import com.example.studyconnect.utils.Helper;
import com.example.studyconnect.utils.SharedPreferencesManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    public LiveData<LoginResponse> getSignInResponse() {
        return signInResponse;
    }

    public LiveData<RegisterResponse> getSignUpResponse() {
        return signUpResponse;
    }

    public void signIn(String email, String password) {
        repository.signIn(email, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    signInResponse.setValue(response.body());
                } else {
                    signInResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                signInResponse.setValue(null);
            }
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
                Toast.makeText(context, "Logout failed : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
