package com.example.studyconnect.ui.class_ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyconnect.databinding.InfoKelasBinding;
import com.example.studyconnect.utils.Helper;
import com.example.studyconnect.utils.SharedPreferencesManager;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class InfoClassActivity extends AppCompatActivity {
    @Inject
    SharedPreferencesManager sharedPreferencesManager;
    private InfoKelasBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = InfoKelasBinding.inflate(getLayoutInflater());

        int classId = sharedPreferencesManager.getData("infoClassId", 0);
        ClassViewModel viewModel = new ViewModelProvider(this).get(ClassViewModel.class);

        viewModel.getIsLoading().observe(this, isLoading -> {
            Helper.showLoading(binding.progressBar.progressBar, isLoading);
        });

        viewModel.getIsSuccess().observe(this, isSuccess -> {
            if (isSuccess) {
                finish();
            }
        });

        viewModel.getErrorMsg().observe(this, errorMsg -> {
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
        });

        // Set up the toolbar with back button functionality
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        viewModel.getClassData(classId).observe(this, classResponse -> {
            if (classResponse != null) {
                if (classResponse.getName() == null) {
                    binding.linearNama.setVisibility(View.GONE);
                } else if (classResponse.getSubject() == null) {
                    binding.linearMapel.setVisibility(View.GONE);
                } else if (classResponse.getDescription() == null) {
                    binding.linearDeskripsi.setVisibility(View.GONE);
                } else {
                    binding.namaKelas.setText(classResponse.getName());
                    binding.mataPelajaran.setText(classResponse.getSubject());
                    binding.deskripsi.setText(classResponse.getDescription());
                }
            }
        });

        setContentView(binding.getRoot());
    }
}