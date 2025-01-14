package com.example.studyconnect.ui.auth;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyconnect.databinding.ActivitySignUpBinding;
import com.example.studyconnect.utils.Helper;
import com.example.studyconnect.utils.SharedPreferencesManager;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private String username, email, role, password, confirmPassword;
    private boolean backPressedOnce = false;
    private Toast backToast;
    private AuthViewModel viewModel;

    @Inject
    SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        viewModel.getIsLoading().observe(this, isLoading -> {
            Helper.showLoading(binding.progressBar.progressBar, isLoading);
        });

        viewModel.getIsSuccess().observe(this, isSuccess -> {
            if (isSuccess) {
                Helper.startFlagedActivity(SignInActivity.class, this);
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
                    backToast = Toast.makeText(SignUpActivity.this, "Press back again to exit", Toast.LENGTH_SHORT);
                    backToast.show();

                    // Reset flag after 2 seconds
                    new Handler().postDelayed(() -> backPressedOnce = false, 2000);
                }
            }
        });

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
                Helper.startActivity(SignInActivity.class, SignUpActivity.this);
            }
        });
    }

    private void selectRole(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Pilih Peran Anda");
        alertDialogBuilder
                .setMessage("Anda sebagai?")
                .setCancelable(false)
                .setPositiveButton("Guru", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        role = "teacher";
                        viewModel.signUp(username, email, role, password);
                    }
                })
                .setNegativeButton("Siswa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        role = "student";
                        viewModel.signUp(username, email, role, password);
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void check() {
        boolean check = true;

//        Mendapatkan variabel
        username = binding.etSignupUsername.getText().toString();
        email = binding.etSignupEmail.getText().toString();
        password = binding.etSignupPassword.getText().toString();
        confirmPassword = binding.etConfirmPassword.getText().toString();

        if (username.isEmpty()) {
            Toast.makeText(this, "Username Kosong !", Toast.LENGTH_SHORT).show();
            binding.etSignupUsername.setError("Isi Username !");
            check = false;
        } else if (email.isEmpty()) {
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
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Confirm Password tidak sesuai !", Toast.LENGTH_SHORT).show();
            binding.etConfirmPassword.setError("Tidak sesuai !");
            check = false;
        }

        if (!check) {
            binding.btnSign.setEnabled(true);
            binding.tvSign.setEnabled(true);
        } else {
            selectRole();
        }
    }
}
