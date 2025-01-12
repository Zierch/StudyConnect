package com.example.studyconnect.ui.auth;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyconnect.databinding.ActivitySignInBinding;
import com.example.studyconnect.ui.home.Drawer;
import com.example.studyconnect.utils.Helper;
import com.example.studyconnect.utils.SharedPreferencesManager;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignInActivity extends AppCompatActivity {
    protected ActivitySignInBinding binding;
    @Inject
    SharedPreferencesManager sharedPreferencesManager;
    private String email, password;
    private boolean backPressedOnce = false;
    private Toast backToast;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (sharedPreferencesManager.getData("token", null) != null) {
            Helper.startFlagedActivity(Drawer.class, this);
            finish();
        }

        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        viewModel.getIsLoading().observe(this, isLoading -> {
            Helper.showLoading(binding.progressBar.progressBar, isLoading);
        });

        viewModel.getIsSuccess().observe(this, isSuccess -> {
            if (isSuccess) {
                Helper.startFlagedActivity(Drawer.class, this);
                finish();
            }
        });

        viewModel.getErrorMsg().observe(this, errorMsg -> {
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
            binding.btnSign.setEnabled(true);
            binding.tvSign.setEnabled(true);
        });

        initView();
    }

    private void initView() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (backPressedOnce) {
                    if (backToast != null) backToast.cancel();
                    finish();
                } else {
                    backPressedOnce = true;
                    backToast = Toast.makeText(SignInActivity.this, "Press back again to exit", Toast.LENGTH_SHORT);
                    backToast.show();

                    // Reset flag after 2 seconds
                    new Handler().postDelayed(() -> backPressedOnce = false, 2000);
                }
            }
        });

        email = sharedPreferencesManager.getData("email", null);
        password = sharedPreferencesManager.getData("password", null);

        if (email != null && password != null) {
            binding.etSignupEmail.setText(email);
            binding.etSignupPassword.setText(password);
        }

        binding.btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btnSign.setEnabled(false);
                binding.tvSign.setEnabled(false);
                check();
            }
        });

        binding.tvSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.startActivity(SignUpActivity.class, SignInActivity.this);
            }
        });
    }

    private void check() {
        boolean check = true;

//        Mendapatkan variabel
        email = binding.etSignupEmail.getText().toString();
        password = binding.etSignupPassword.getText().toString();

        if (email.isEmpty()) {
            Toast.makeText(this, "Email Kosong !", Toast.LENGTH_SHORT).show();
            binding.etSignupEmail.setError("Isi Email !");
            check = false;
        } else if (password.length() <= 6) {
            Toast.makeText(this, "Password minimal 6 huruf !", Toast.LENGTH_SHORT).show();
            binding.etSignupPassword.setError("Password terlalu pendek !");
            check = false;
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Password Kosong !", Toast.LENGTH_SHORT).show();
            binding.etSignupPassword.setError("Isi Password !");
            check = false;
        }

        if (!check) {
            binding.btnSign.setEnabled(true);
            binding.tvSign.setEnabled(true);
        } else {
            viewModel.signIn(email, password);
        }
    }

}
