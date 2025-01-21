package com.example.studyconnect.ui.class_ui.assignment.tab_view_guru;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyconnect.databinding.FormScoreBinding;
import com.example.studyconnect.ui.class_ui.assignment.SubmissionViewModel;
import com.example.studyconnect.utils.Helper;
import com.example.studyconnect.utils.SharedPreferencesManager;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FormScoringActivity extends AppCompatActivity {
    private FormScoreBinding binding;
    private SubmissionViewModel viewModel;
    private int submissionId, maxScore;
    private String type, studentName;
    @Inject
    SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FormScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        type = sharedPreferencesManager.getData("assignmentType", null);
        maxScore = sharedPreferencesManager.getData("maxScore", 0);
        submissionId = getIntent().getIntExtra("submissionId", 0);
        studentName = getIntent().getStringExtra("studentName");
        viewModel = new ViewModelProvider(this).get(SubmissionViewModel.class);

        initObserver();
        initView();
    }

    private void initView() {
        // Set up the toolbar with back button functionality
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        binding.btnSimpan.setOnClickListener(v -> {
            binding.btnSimpan.setEnabled(false);
            check();
        });
    }

    private void initObserver() {
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

        viewModel.getSubmissionById(submissionId).observe(this, submissionResponse -> {
            binding.tvProfileName.setText(studentName);
            if (submissionResponse.getStatus().equals("graded")){
                binding.status.setText("Dinilai");
                binding.nilai.setText(String.valueOf(submissionResponse.getScore()));
            }
            binding.nilai.setHint("0 /" + maxScore);
            binding.nilai.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // Tidak perlu melakukan apa-apa di sini
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // Tidak perlu melakukan apa-apa di sini
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String input = editable.toString();
                    if (!input.isEmpty()) {
                        try {
                            int nilai = Integer.parseInt(input); // Konversi ke integer
                            if (nilai > maxScore) {
                                binding.tfNilai.setError("Nilai tidak boleh lebih dari " + maxScore);
                            } else {
                                binding.tfNilai.setError(null); // Hapus pesan error jika nilai valid
                            }
                        } catch (NumberFormatException e) {
                            binding.tfNilai.setError("Input harus berupa bilangan bulat");
                        }
                    } else {
                        binding.tfNilai.setError(null); // Hapus pesan error jika input kosong
                    }
                }
            });

            switch (type) {
                case "multiple_choice":
                    binding.cardPilihan.setVisibility(View.VISIBLE);
                    String options = sharedPreferencesManager.getData("options", null);
                    Helper.addRadioButtonsFromJsonArray(this, binding.radioGroup, options);

                    binding.radioGroup.setEnabled(false);
                    for (int i = 0; i < binding.radioGroup.getChildCount(); i++) {
                        View child = binding.radioGroup.getChildAt(i);
                        if (child instanceof RadioButton) {
                            child.setEnabled(false);
                            child.setClickable(false);
                            child.setFocusable(false);
                        }
                    }

                    Helper.setSelectedRadioButton(binding.radioGroup, submissionResponse.getAnswer());
                    break;
                case "file_upload":
                    binding.linearLampiran.setVisibility(View.VISIBLE);
                    Helper.loadImageWithGlide(this, submissionResponse.getAttachment_path(), binding.gambar);
                    binding.namaGambar.setText(Helper.getFileNameFromPath(submissionResponse.getAttachment_path()));
                    binding.fileCard.setOnClickListener(v -> {
                        Helper.showFullScreenFileDialog(this, submissionResponse.getAttachment_path(), null);
                    });
                    break;
                case "essay":
                    binding.cardEsai.setVisibility(View.VISIBLE);
                    binding.edtJawabanEsai.setEnabled(false);
                    binding.edtJawabanEsai.setText(submissionResponse.getAnswer());
                    break;
            }
        });
    }

    private void check() {
        boolean check = true;
        String nilai = binding.nilai.getText().toString();

        if (nilai.isEmpty()) {
            Toast.makeText(this, "Nilai Kosong!", Toast.LENGTH_SHORT).show();
            binding.nilai.setError("Tidak boleh kosong!");
            check = false;
        }
        if (check) {
            viewModel.scoreSubmission(submissionId, Integer.parseInt(nilai));
        } else {
            binding.btnSimpan.setEnabled(false);
        }
    }
}