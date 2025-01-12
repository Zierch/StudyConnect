package com.example.studyconnect.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyconnect.R;
import com.example.studyconnect.databinding.ActivityDrawerBinding;
import com.example.studyconnect.ui.archieved.ArchivedFragment;
import com.example.studyconnect.ui.auth.AuthViewModel;
import com.example.studyconnect.ui.auth.SignInActivity;
import com.example.studyconnect.utils.Helper;
import com.example.studyconnect.utils.SharedPreferencesManager;
import com.google.android.material.navigation.NavigationView;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Drawer extends AppCompatActivity {
    private ActivityDrawerBinding binding;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private boolean backPressedOnce = false;
    private Toast backToast;
    private AuthViewModel viewModel;

    @Inject
    SharedPreferencesManager sharedPreferencesManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBar.toolbar);
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        drawerLayout = binding.drawerLayout;
        navigationView = binding.navDrawer;

        // Inisialisasi ActionBarDrawerToggle
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, binding.appBar.toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        viewModel.getIsLoading().observe(this, isLoading -> {
            Helper.showLoading(binding.progressBar.progressBar, isLoading);
        });

        viewModel.getIsSuccess().observe(this, isSuccess -> {
            Helper.startFlagedActivity(SignInActivity.class, this);
            finish();
        });

        viewModel.getErrorMsg().observe(this, errorMsg -> {
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
        });

        initView();
    }

    private void initView() {
        if (sharedPreferencesManager.getData("role", "").equals("siswa")) {
            binding.navDrawer.getMenu().findItem(R.id.archived).setVisible(false);
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    // Jika ada fragment di back stack, kembali ke fragment sebelumnya
                    binding.appBar.toolbar.setTitle(R.string.app_toolbar_title);
                    getSupportFragmentManager().popBackStack();
                } else {
                    if (backPressedOnce) {
                        if (backToast != null) backToast.cancel();
                        finish();
                    } else {
                        backPressedOnce = true;
                        backToast = Toast.makeText(Drawer.this, "Press back again to exit", Toast.LENGTH_SHORT);
                        backToast.show();

                        // Reset flag setelah 2 detik
                        new Handler().postDelayed(() -> backPressedOnce = false, 2000);
                    }
                }
            }
        });

//        Default fragment
        replaceFragment(new HomeFragment());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int selected = item.getItemId();

                if (selected == R.id.home) {
                    replaceFragment(new HomeFragment());
                } else if (selected == R.id.notification) {
                    Toast.makeText(Drawer.this, "404 Not Found", Toast.LENGTH_SHORT).show();
                } else if (selected == R.id.archived) {
                    replaceFragment(new ArchivedFragment());
                } else if (selected == R.id.setting) {
                    Toast.makeText(Drawer.this, "404 Not Found", Toast.LENGTH_SHORT).show();
                } else if (selected == R.id.logout) {
                    viewModel.logout();
                }

                drawerLayout.closeDrawer(navigationView);
                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        binding.appBar.toolbar.setTitle(R.string.app_toolbar_title);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_host, fragment);
        fragmentTransaction.commit();
    }
}
