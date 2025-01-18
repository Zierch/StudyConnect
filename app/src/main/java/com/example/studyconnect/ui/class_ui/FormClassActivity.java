package com.example.studyconnect.ui.class_ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyconnect.databinding.FormKelasBinding;
import com.example.studyconnect.utils.Helper;
import com.example.studyconnect.utils.SharedPreferencesManager;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FormClassActivity extends AppCompatActivity {
    @Inject
    SharedPreferencesManager sharedPreferencesManager;
    private FormKelasBinding binding;
    private ClassViewModel viewModel;
    private String kodeKelas;
    private boolean isUpdate = false; // Defaultnya create, bisa diganti saat update
    private int teacherId, classId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FormKelasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        classId = sharedPreferencesManager.getData("classId", 0);
        teacherId = sharedPreferencesManager.getData("teacherId", 0);
        viewModel = new ViewModelProvider(this).get(ClassViewModel.class);
        isUpdate = getIntent().getBooleanExtra("isUpdate", false);

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
            binding.btnSimpan.setEnabled(true);
        });

        initView();
    }

    private void initView() {
        // Set up the toolbar with back button functionality
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        if (isUpdate) {
            binding.toolbar.setTitle("Setting Kelas");

            int classId = sharedPreferencesManager.getData("classId", 0);
            viewModel.getClassData(classId).observe(this, classResponse -> {
                if (classResponse != null) {
                    binding.namaKelas.setText(classResponse.getName());
                    binding.mataPelajaran.setText(classResponse.getSubject());
                    binding.deskripsiKelas.setText(classResponse.getDescription());
                    binding.kodeKelas.setText(classResponse.getCode());
                }
            });

            binding.tiCode.setEndIconOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String randomCode = Helper.generateRandomString();
                    binding.kodeKelas.setText(randomCode);
                }
            });
        } else {
            binding.label.setVisibility(View.GONE);
            binding.kodeKelas.setVisibility(View.GONE);
        }

        binding.kodeKelas.setOnLongClickListener(v -> {
            // Salin teks ke clipboard
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Kode Kelas", binding.kodeKelas.getText().toString());
            clipboard.setPrimaryClip(clip);

            Toast.makeText(this, "Kode Kelas disalin", Toast.LENGTH_SHORT).show();

            return true;
        });

        binding.btnSimpan.setOnClickListener(v -> {
            binding.btnSimpan.setEnabled(false);
            check();
        });
    }

    private void check() {
        boolean check = true;

        // Mendapatkan variabel inputan
        String namaKelas = binding.namaKelas.getText().toString();
        String mataPelajaran = binding.mataPelajaran.getText().toString();
        String deskripsiKelas = binding.deskripsiKelas.getText().toString();

        if (isUpdate) {
            kodeKelas = binding.kodeKelas.getText().toString();
        }

        if (namaKelas.isEmpty()) {
            Toast.makeText(this, "Nama Kelas Kosong!", Toast.LENGTH_SHORT).show();
            binding.namaKelas.setError("Isi Nama Kelas!");
            check = false;
        } else if (mataPelajaran.isEmpty()) {
            Toast.makeText(this, "Mata Pelajaran Kosong!", Toast.LENGTH_SHORT).show();
            binding.mataPelajaran.setError("Isi Mata Pelajaran!");
            check = false;
        }

        if (isUpdate) {
            if (kodeKelas.isEmpty()) {
                Toast.makeText(this, "Kode Kelas Kosong!", Toast.LENGTH_SHORT).show();
                binding.kodeKelas.setError("Isi Kode Kelas!");
                check = false;
            }
        }

        if (check) {
            if (isUpdate) {
                viewModel.updateClass(classId, namaKelas, mataPelajaran, deskripsiKelas, kodeKelas);
            } else {
                viewModel.createClass(namaKelas, mataPelajaran, deskripsiKelas, teacherId);
            }
        } else {
            binding.btnSimpan.setEnabled(true);
        }
    }

}

