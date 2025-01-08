package com.example.studyconnect.ui.auth;

import android.content.Intent;
import android.os.Looper;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.example.studyconnect.MainActivity;
import com.example.studyconnect.data.StudyRepository;
import com.example.studyconnect.data.response.LoginResponse;
import com.example.studyconnect.ui.home.Drawer;
import com.example.studyconnect.utils.SharedPreferencesManager;

import java.util.logging.Handler;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class AuthViewModel extends ViewModel {
    private final StudyRepository repository;

    @Inject
    SharedPreferencesManager sharedPreferencesManager;

    @Inject
    public AuthViewModel(StudyRepository repository) {
        this.repository = repository;
    }

    public void signIn(String email, String password) {
        repository.signIn(email, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                sharedPreferencesManager.saveData("token", response.body().getToken());

//                Disini Handler delay mbek intent nang Home Drawer
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(this, Drawer.class);
                    startActivity(intent);
                    finish();
                }, 1500);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
