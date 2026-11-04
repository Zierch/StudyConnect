package com.example.studyconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;

import com.example.studyconnect.data.Api;
import com.example.studyconnect.data.ApiClient;
import com.example.studyconnect.data.ClassItem;
import com.example.studyconnect.data.Session;
import com.example.studyconnect.databinding.FragmentHomeBinding;
import com.example.studyconnect.ui.ClassAdapter;
import com.example.studyconnect.ui.auth.LoginActivity;
import com.example.studyconnect.ui.class_ui.ClassActivity;

import java.util.List;

import retrofit2.Call;

/** Beranda: daftar kelas milik pengguna yang sedang login. */
public class MainActivity extends AppCompatActivity {

    private FragmentHomeBinding binding;
    private ClassAdapter adapter;
    private Call<List<ClassItem>> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        if (!Session.isLoggedIn(this)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (view, windowInsets) -> {
            Insets bars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return windowInsets;
        });

        // Menu sudah dipasang lewat app:menu di layout; cukup pasang pendengar klik.
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_logout) {
                logout();
                return true;
            }
            return false;
        });

        adapter = new ClassAdapter(this::openClass);
        binding.rvKelas.setLayoutManager(new LinearLayoutManager(this));
        binding.rvKelas.setAdapter(adapter);

        binding.joinClass.setOnClickListener(view ->
                Toast.makeText(this, R.string.text_gabung_belum_tersedia, Toast.LENGTH_SHORT).show());
        binding.createClass.setOnClickListener(view ->
                Toast.makeText(this, R.string.text_buat_belum_tersedia, Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadClasses();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Jangan biarkan jawaban datang setelah layar ditinggalkan.
        if (call != null) {
            call.cancel();
            call = null;
        }
    }

    private void loadClasses() {
        if (call != null) {
            // Permintaan sebelumnya belum selesai; jangan menumpuk.
            return;
        }
        binding.progress.setVisibility(View.VISIBLE);
        binding.tvKosong.setVisibility(View.GONE);

        call = ApiClient.service().getClasses(Session.token(this));
        Api.enqueue(call, this::showClasses, this::showError);
    }

    private void showClasses(List<ClassItem> classes) {
        call = null;
        binding.progress.setVisibility(View.GONE);
        adapter.submit(classes);
        boolean empty = classes == null || classes.isEmpty();
        binding.tvKosong.setVisibility(empty ? View.VISIBLE : View.GONE);
        if (empty) {
            binding.tvKosong.setText(R.string.text_belum_punya_kelas);
        }
    }

    private void showError(String message) {
        call = null;
        binding.progress.setVisibility(View.GONE);
        binding.tvKosong.setVisibility(View.VISIBLE);
        binding.tvKosong.setText(message);
    }

    private void logout() {
        Session.clear(this);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void openClass(ClassItem item) {
        Intent intent = new Intent(this, ClassActivity.class);
        intent.putExtra(ClassActivity.EXTRA_CLASS_ID, item.getId());
        intent.putExtra(ClassActivity.EXTRA_CLASS_NAME, item.getName());
        intent.putExtra(ClassActivity.EXTRA_TEACHER_ID, item.getTeacherId());
        startActivity(intent);
    }
}
