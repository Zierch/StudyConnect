package com.example.studyconnect.ui.class_ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.studyconnect.R;
import com.example.studyconnect.data.Session;
import com.example.studyconnect.databinding.FragmentClassBinding;

/**
 * Layar kelas: toolbar + tiga tab (Forum, Tugas, Orang) di atas NavHostFragment
 * yang memakai {@code navigation/class_navigation.xml}.
 */
public class ClassActivity extends AppCompatActivity {

    public static final String EXTRA_CLASS_ID = "class_id";
    public static final String EXTRA_CLASS_NAME = "class_name";
    public static final String EXTRA_TEACHER_ID = "teacher_id";
    public static final String EXTRA_CLASS_CODE = "class_code";

    /** Id kelas dari intent, 0 bila tidak ada. */
    public static int classIdOf(android.app.Activity activity) {
        return activity.getIntent().getIntExtra(EXTRA_CLASS_ID, 0);
    }

    /** Nama kelas dari intent, kosong bila tidak ada. */
    public static String classNameOf(android.app.Activity activity) {
        String name = activity.getIntent().getStringExtra(EXTRA_CLASS_NAME);
        return name == null ? "" : name;
    }

    /** Id guru pemilik kelas dari intent, 0 bila tidak ada. */
    public static int teacherIdOf(android.app.Activity activity) {
        return activity.getIntent().getIntExtra(EXTRA_TEACHER_ID, 0);
    }

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

        String className = classNameOf(this);
        if (!className.isEmpty()) {
            binding.toolbar.setTitle(className);
            binding.bannerNama.setText(className);
        }
        String guru = Session.name(this);
        binding.bannerGuru.setText(getString(R.string.text_banner_guru,
                guru == null || guru.isEmpty() ? "Guru kelas" : guru));
        String code = getIntent().getStringExtra(EXTRA_CLASS_CODE);
        binding.bannerKode.setText(getString(R.string.text_kode_kelas,
                code == null || code.isEmpty() ? "—" : code));

        // Panah toolbar mengikuti back sistem: mundur antar tab dulu, baru keluar kelas.
        binding.toolbar.setNavigationOnClickListener(view ->
                getOnBackPressedDispatcher().onBackPressed());
        binding.toolbarAvatar.setOnClickListener(view ->
                Toast.makeText(this, R.string.text_belum_tersedia, Toast.LENGTH_SHORT).show());
        binding.fabAdd.setOnClickListener(view ->
                Toast.makeText(this, R.string.text_buat_belum_tersedia, Toast.LENGTH_SHORT).show());

        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_fragment_class);
        if (navHost == null) {
            throw new IllegalStateException("NavHostFragment tidak ditemukan di fragment_class");
        }
        NavController navController = navHost.getNavController();

        binding.tabForum.setOnClickListener(v -> navController.navigate(R.id.navigation_forum));
        binding.tabTugas.setOnClickListener(v ->
                navController.navigate(R.id.navigation_class_assignment));
        binding.tabOrang.setOnClickListener(v ->
                navController.navigate(R.id.navigation_student_list));

        navController.addOnDestinationChangedListener((controller, destination, args) ->
                selectTab(destination.getId()));
        selectTab(navController.getCurrentDestination() == null
                ? R.id.navigation_forum : navController.getCurrentDestination().getId());
    }

    /** Tandai tab aktif dan geser garis bawah biru (frame FigCraft). */
    private void selectTab(int destinationId) {
        boolean forum = destinationId == R.id.navigation_forum;
        boolean tugas = destinationId == R.id.navigation_class_assignment;
        boolean orang = destinationId == R.id.navigation_student_list;
        binding.tabForum.setTextColor(color(forum));
        binding.tabTugas.setTextColor(color(tugas));
        binding.tabOrang.setTextColor(color(orang));
        binding.indForum.setVisibility(forum ? View.VISIBLE : View.GONE);
        binding.indTugas.setVisibility(tugas ? View.VISIBLE : View.GONE);
        binding.indOrang.setVisibility(orang ? View.VISIBLE : View.GONE);
    }

    private int color(boolean active) {
        return androidx.core.content.ContextCompat.getColor(this,
                active ? R.color.sc_primary : R.color.sc_on_surface_muted);
    }
}
