package com.example.studyconnect.ui.class_ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyconnect.databinding.FormJoinKelasBinding;
import com.example.studyconnect.utils.Helper;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class JoinClassActivity extends AppCompatActivity {
    private FormJoinKelasBinding binding;
    private ClassViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FormJoinKelasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(ClassViewModel.class);
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
            binding.btnGabung.setEnabled(true);
        });

        initView();
    }

    private void initView() {
        // Set up the toolbar with back button functionality
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        binding.kodeKelas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Tidak perlu melakukan apa-apa di sini
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Tidak perlu melakukan apa-apa di sini
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Ubah teks menjadi lowercase
                String input = s.toString();
                if (!input.equals(input.toLowerCase())) {
                    binding.kodeKelas.setText(input.toLowerCase());
                    binding.kodeKelas.setSelection(input.length()); // Pindahkan kursor ke akhir teks
                }
            }
        });

        binding.btnGabung.setOnClickListener(v -> {
            binding.btnGabung.setEnabled(false);
            check();
        });
    }

    private void check() {
        boolean check = true;

        // Mendapatkan variabel inputan
        String kodeKelas = binding.kodeKelas.getText().toString();

        if (kodeKelas.isEmpty()) {
            Toast.makeText(this, "Kode Kelas Kosong!", Toast.LENGTH_SHORT).show();
            binding.kodeKelas.setError("Isi Kode Kelas!");
            check = false;
        }


        if (check) {
            viewModel.joinClass(kodeKelas);
        }
    }
}