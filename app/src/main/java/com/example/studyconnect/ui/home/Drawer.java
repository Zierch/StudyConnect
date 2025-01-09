package com.example.studyconnect.ui.home;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.studyconnect.ui.auth.AuthViewModel;
import com.google.android.material.navigation.NavigationView;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Drawer extends AppCompatActivity {
    private ActivityDrawerBinding binding;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private AuthViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        drawerLayout = binding.drawerLayout;
        navigationView = binding.navDrawer;

        initView();
    }

    private void initView() {
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
                    Toast.makeText(Drawer.this, "404 Not Found", Toast.LENGTH_SHORT).show();
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

        fragmentTransaction.runOnCommit(() -> {
            // Set toolbar dari fragment aktif
            setToolbar(fragment);
        });
    }

    private void setToolbar(Fragment fragment) {
        View view = fragment.getView();
        if (view != null) {
            Toolbar toolbar = view.findViewById(R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);

                // Setup DrawerToggle
                toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
                drawerLayout.addDrawerListener(toggle);
                toggle.syncState();

                // OnClick Listener untuk membuka Drawer
                toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
            }
        }
    }
}
