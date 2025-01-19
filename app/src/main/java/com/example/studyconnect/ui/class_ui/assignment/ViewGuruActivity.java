package com.example.studyconnect.ui.class_ui.assignment;

import android.os.Bundle;

import com.example.studyconnect.databinding.ActivityViewGuruBinding;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studyconnect.ui.adapter.ViewGuruPagerAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ViewGuruActivity extends AppCompatActivity {

    private ActivityViewGuruBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewGuruBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int assignmentId = getIntent().getIntExtra("assignmentId", 0);

        ViewGuruPagerAdapter viewGuruPagerAdapter = new ViewGuruPagerAdapter(
                this, getSupportFragmentManager(), assignmentId
        );
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(viewGuruPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        // Set up the toolbar with back button functionality
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}