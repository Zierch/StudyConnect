package com.example.studyconnect.ui.class_ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyconnect.databinding.SettingKelasBinding;
import com.example.studyconnect.utils.SharedPreferencesManager;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FormClassActivity extends AppCompatActivity {
    private SettingKelasBinding binding;
    private ClassViewModel viewModel;
    private String namaKelas, mataPelajaran, deskripsiKelas, kodeKelas;
    private boolean isUpdate = false; // Defaultnya create, bisa diganti saat update

    @Inject
    SharedPreferencesManager sharedPreferencesManager;
    private int teacherId, classId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SettingKelasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        classId = sharedPreferencesManager.getData("classId", 0);
        teacherId = sharedPreferencesManager.getData("teacherId", 0);
        viewModel = new ViewModelProvider(this).get(ClassViewModel.class);
        isUpdate = getIntent().getBooleanExtra("isUpdate", false);

        initView();
    }

    private void initView() {
        // Set up the toolbar with back button functionality
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Observasi hasil create class
        viewModel.getCreateClassResult().observe(this, isSuccess -> {
            if (isSuccess) {
                finish(); // Selesai setelah berhasil
            }
        });

        // Observasi hasil update class
        viewModel.getUpdateClassResult().observe(this, isSuccess -> {
            if (isSuccess) {
                finish();
            }
        });

        if (isUpdate) {
            binding.toolbar.setTitle("Update Kelas");
            binding.btnSimpan.setText("Update");

//            // Ambil data kelas yang sudah ada dan tampilkan di field
//            // Misalnya, ambil data kelas dari intent atau database
//            namaKelas = getIntent().getStringExtra("namaKelas");
//            mataPelajaran = getIntent().getStringExtra("mataPelajaran");
//            deskripsiKelas = getIntent().getStringExtra("deskripsiKelas");
//            kodeKelas = getIntent().getStringExtra("kodeKelas");

            binding.namaKelas.setText(namaKelas);
            binding.mataPelajaran.setText(mataPelajaran);
            binding.deskripsiKelas.setText(deskripsiKelas);
            binding.kodeKelas.setText(kodeKelas);
        } else {
            binding.toolbar.setTitle("Create Kelas");

            binding.label.setVisibility(View.GONE);
            binding.kodeKelas.setVisibility(View.GONE);
        }

        binding.btnSimpan.setOnClickListener(v -> {
            binding.btnSimpan.setEnabled(false);
            check();
        });
    }

    private void check() {
        boolean check = true;

        // Mendapatkan variabel inputan
        namaKelas = binding.namaKelas.getText().toString();
        mataPelajaran = binding.mataPelajaran.getText().toString();
        deskripsiKelas = binding.deskripsiKelas.getText().toString();

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

