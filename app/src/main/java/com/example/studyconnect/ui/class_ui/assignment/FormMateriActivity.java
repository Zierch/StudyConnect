package com.example.studyconnect.ui.class_ui.assignment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyconnect.databinding.ActivityFormMateriBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FormMateriActivity extends AppCompatActivity {
    ActivityFormMateriBinding binding;
    private ClassAssignmentViewModel viewModel;
    private int classId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormMateriBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(ClassAssignmentViewModel.class);
        classId = getIntent().getIntExtra("classId", 0);
    }
}