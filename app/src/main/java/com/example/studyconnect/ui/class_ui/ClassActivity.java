package com.example.studyconnect.ui.class_ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.studyconnect.R;
import com.example.studyconnect.databinding.FragmentClassBinding;

/**
 * Layar kelas: toolbar + tiga tab (Forum, Tugas, Orang) di atas NavHostFragment
 * yang memakai {@code navigation/class_navigation.xml}.
 */
public class ClassActivity extends AppCompatActivity {

    private FragmentClassBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = FragmentClassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.container, (view, windowInsets) -> {
            Insets systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return windowInsets;
        });

        // Panah toolbar mengikuti back sistem: mundur antar tab dulu, baru keluar kelas.
        binding.toolbar.setNavigationOnClickListener(view ->
                getOnBackPressedDispatcher().onBackPressed());

        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_fragment_class);
        if (navHost == null) {
            throw new IllegalStateException("NavHostFragment tidak ditemukan di fragment_class");
        }
        NavController navController = navHost.getNavController();
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
}
