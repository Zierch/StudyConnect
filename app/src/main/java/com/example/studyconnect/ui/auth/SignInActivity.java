package com.example.studyconnect.ui.auth;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyconnect.data.StudyRepository;
import com.example.studyconnect.databinding.ActivitySignInBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignInActivity extends AppCompatActivity {
    protected ActivitySignInBinding binding;
    private String email, password;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        initView();
    }

    private void initView(){

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
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Password Kosong !", Toast.LENGTH_SHORT).show();
            binding.etSignupPassword.setError("Isi Password !");
            check = false;
        }

        if (!check) {
            binding.btnSign.setEnabled(true);
        } else {
            viewModel.signIn(email, password);
        }
    }
}
