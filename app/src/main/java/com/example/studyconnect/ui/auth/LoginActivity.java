package com.example.studyconnect.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.studyconnect.MainActivity;
import com.example.studyconnect.R;
import com.example.studyconnect.data.Api;
import com.example.studyconnect.data.ApiClient;
import com.example.studyconnect.data.LoginResponse;
import com.example.studyconnect.data.Session;
import com.example.studyconnect.databinding.ActivityLoginBinding;

import retrofit2.Call;

/** Login ke StudyConnect-API; token disimpan di {@link Session}. */
public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (view, windowInsets) -> {
            Insets bars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(view.getPaddingLeft(), bars.top, view.getPaddingRight(), bars.bottom);
            return windowInsets;
        });

        binding.btnLogin.setOnClickListener(view -> submit());
    }

    private void submit() {
        String email = text(binding.etEmail);
        String password = text(binding.etPassword);

        binding.tvError.setVisibility(View.GONE);
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            showError(getString(R.string.text_email_password_kosong));
            return;
        }

        setLoading(true);
        Api.enqueue(ApiClient.service().login(email, password),
                response -> onLoginSuccess(response, email),
                message -> {
                    setLoading(false);
                    showError(message);
                });
    }

    private void onLoginSuccess(LoginResponse response, String email) {
        setLoading(false);
        String token = response.getToken();
        if (token == null) {
            showError(getString(R.string.text_login_gagal));
            return;
        }
        // Simpan sesi dulu supaya permintaan profil di bawah sudah membawa token.
        Session.save(this, token, response.getRole(), email, response.getUserId());
        Api.enqueue(ApiClient.service().getUser(token, response.getUserId()),
                user -> {
                    if (user.getName() != null) {
                        Session.save(this, token, response.getRole(), user.getName(),
                                response.getUserId());
                    }
                    openHome();
                },
                message -> openHome());
    }

    private void openHome() {
        Toast.makeText(this, R.string.text_login_berhasil, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void setLoading(boolean loading) {
        binding.progress.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.btnLogin.setEnabled(!loading);
    }

    private void showError(String message) {
        binding.tvError.setVisibility(View.VISIBLE);
        binding.tvError.setText(message);
    }

    private static String text(android.widget.EditText field) {
        return field.getText() == null ? "" : field.getText().toString().trim();
    }
}
