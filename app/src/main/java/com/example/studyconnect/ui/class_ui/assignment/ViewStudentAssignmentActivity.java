package com.example.studyconnect.ui.class_ui.assignment;

import static com.example.studyconnect.utils.Helper.REQUEST_CODE_PICK_IMAGE;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyconnect.R;
import com.example.studyconnect.databinding.DetailTugasStudentBinding;
import com.example.studyconnect.databinding.JawabanEsaiDialogBinding;
import com.example.studyconnect.databinding.JawabanPilihanDialogBinding;
import com.example.studyconnect.utils.Helper;
import com.example.studyconnect.utils.SharedPreferencesManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;

import java.io.File;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ViewStudentAssignmentActivity extends AppCompatActivity {
    @Inject
    SharedPreferencesManager sharedPreferencesManager;
    private DetailTugasStudentBinding bindingFile;
    private JawabanEsaiDialogBinding bindingEsai;
    private JawabanPilihanDialogBinding bindingPilihan;
    private SubmissionViewModel viewModel;
    private int assignmentId, submissionId, maxScore;
    private String type, answer;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int userId = sharedPreferencesManager.getData("userId", 0);
        assignmentId = getIntent().getIntExtra("assignmentId", 0);
        type = getIntent().getStringExtra("type");

        switch (type) {
            case "multiple_choice":
                bindingPilihan = JawabanPilihanDialogBinding.inflate(getLayoutInflater());
                setContentView(bindingPilihan.getRoot());
                break;
            case "file_upload":
                bindingFile = DetailTugasStudentBinding.inflate(getLayoutInflater());
                setContentView(bindingFile.getRoot());
                break;
            case "essay":
                bindingEsai = JawabanEsaiDialogBinding.inflate(getLayoutInflater());
                setContentView(bindingEsai.getRoot());
                break;
        }

        viewModel = new ViewModelProvider(this).get(SubmissionViewModel.class);
        viewModel.getSubmissions(assignmentId).observe(this, submissionResponses -> {
            submissionId = viewModel.getAnsweredSubmissionId(submissionResponses, assignmentId, userId);
            Log.d("TAG", "onCreate: " + submissionId);
        });

        initObserver();
        initView();
    }

    private void initView() {
        if (type.equals("file_upload")) {
            // Set up the toolbar with back button functionality
            setSupportActionBar(bindingFile.toolbar);
            bindingFile.toolbar.setNavigationOnClickListener(v -> onBackPressed());

            MaterialCardView bottomCard = findViewById(R.id.bottomCard);
            BottomSheetBehavior<MaterialCardView> bottomSheetBehavior = BottomSheetBehavior.from(bottomCard);

            // Mengatur keadaan awal
            bottomSheetBehavior.setSkipCollapsed(true);
            bottomSheetBehavior.setFitToContents(false);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            // Menambahkan callback untuk menangani perubahan keadaan
            bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    switch (newState) {
                        case BottomSheetBehavior.STATE_EXPANDED:
                            // Tampilkan layout bottom_jawaban saat di-expand
                            bindingFile.bottomJawaban.main.setVisibility(View.VISIBLE);
                            bindingFile.llCardTop.setVisibility(View.GONE);
                            bindingFile.bottomJawaban.main.animate().alpha(1f).setDuration(300).start();
                            break;
                        case BottomSheetBehavior.STATE_COLLAPSED:
                            // Sembunyikan layout bottom_jawaban saat collapsed
                            bindingFile.llCardTop.setVisibility(View.VISIBLE);
                            bindingFile.bottomJawaban.main.animate().alpha(0f).setDuration(300).withEndAction(() -> {
                                bindingFile.bottomJawaban.main.setVisibility(View.GONE);
                            }).start();
                            break;
                        case BottomSheetBehavior.STATE_HIDDEN:
                            // Sembunyikan layout bottom_jawaban saat hidden
                            bindingFile.bottomJawaban.main.setVisibility(View.GONE);
                            break;
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    // Animasi translate untuk elemen UI
                    float translationY = -100 * (1 - slideOffset); // Geser ke atas saat di-expand
                    bindingFile.bottomJawaban.main.setTranslationY(translationY);
                }
            });

            bindingFile.bottomJawaban.btnAddTugas.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
            });
            bindingFile.btnUploadTugas.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
            });

            bindingFile.bottomJawaban.btnRemove.setOnClickListener(v -> {
                // Hapus file dari penyimpanan jika ada
                if (file != null && file.exists()) {
                    boolean deleted = file.delete();
                    if (deleted) {
                        Toast.makeText(this, "File dihapus: " + file.getName(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Gagal menghapus file", Toast.LENGTH_SHORT).show();
                    }
                }

                // Reset variabel file
                file = null;

                bindingFile.bottomJawaban.fileCard.setVisibility(View.GONE);
                bindingFile.bottomJawaban.cardEmpty.setVisibility(View.VISIBLE);
                bindingFile.btnUploadTugas.setEnabled(true);
                bindingFile.bottomJawaban.btnAddTugas.setEnabled(true);
                bindingFile.bottomJawaban.btnRemove.setVisibility(View.GONE);
            });
        } else if (type.equals("essay")) {
            // Set up the toolbar with back button functionality
            setSupportActionBar(bindingEsai.toolbar);
            bindingEsai.toolbar.setNavigationOnClickListener(v -> onBackPressed());

            bindingEsai.btnSubmit.setOnClickListener(v -> {
                bindingEsai.btnSubmit.setEnabled(false);
                check();
            });
        } else {
            // Set up the toolbar with back button functionality
            setSupportActionBar(bindingPilihan.toolbar);
            bindingPilihan.toolbar.setNavigationOnClickListener(v -> onBackPressed());

            bindingPilihan.btnSubmitPilihan.setOnClickListener(v -> {
                bindingPilihan.btnSubmitPilihan.setEnabled(false);
                check();
            });
        }
    }

    private void initObserver() {
        viewModel.getIsLoading().observe(this, isLoading -> {
            switch (type) {
                case "multiple_choice":
                    Helper.showLoading(bindingPilihan.progressBar.progressBar, isLoading);
                    break;
                case "file_upload":
                    Helper.showLoading(bindingFile.progressBar.progressBar, isLoading);
                    break;
                case "essay":
                    Helper.showLoading(bindingEsai.progressBar.progressBar, isLoading);
                    break;
            }
        });

        viewModel.getIsSuccess().observe(this, isSuccess -> {
            if (isSuccess) {
                finish();
            }
        });

        viewModel.getErrorMsg().observe(this, errorMsg -> {
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();

            switch (type) {
                case "multiple_choice":
                    bindingPilihan.btnSubmitPilihan.setEnabled(true);
                    break;
                case "file_upload":
                    bindingFile.btnUploadTugas.setEnabled(true);
                    bindingFile.bottomJawaban.btnAddTugas.setEnabled(true);
                    break;
                case "essay":
                    bindingEsai.btnSubmit.setEnabled(true);
                    break;
            }
        });

        switch (type) {
            case "multiple_choice":
                viewModel.getAssignment(assignmentId).observe(this, classAssignmentResponse -> {
                    maxScore = classAssignmentResponse.getMax_score();
                    bindingPilihan.judulTugas.setText(classAssignmentResponse.getTitle());
                    bindingPilihan.maxScore.setText(maxScore + " " + getString(R.string.poin));

                    if (classAssignmentResponse.getAttachment_path() != null){
                        Helper.loadImageWithGlide(this, classAssignmentResponse.getAttachment_path(), bindingPilihan.gambar);
                        bindingPilihan.namaGambar.setText(Helper.getFileNameFromPath(classAssignmentResponse.getAttachment_path()));

                        bindingPilihan.fileCard.setOnClickListener(v -> {
                            Helper.showFullScreenImageDialog(this, classAssignmentResponse.getAttachment_path());
                        });
                    } else {
                        bindingPilihan.linearLampiran.setVisibility(View.GONE);
                    }

                    if (classAssignmentResponse.getDue_date() != null) {
                        String formattedDate = Helper.formatDateYMDHI("Batas waktu : ", classAssignmentResponse.getDue_date());
                        bindingPilihan.dueDate.setText(formattedDate);
                    }

                    String jsonOption = classAssignmentResponse.getOptions();
                    Helper.addRadioButtonsFromJsonArray(this, bindingPilihan.radioGroup, jsonOption);
                });

                viewModel.isAnswered().observe(this, isAnswered -> {
                    if (submissionId != 0) {
                        viewModel.getSubmissionById(submissionId).observe(this, submissionResponse -> {
                            bindingPilihan.btnSubmitPilihan.setEnabled(false);
                            bindingPilihan.btnSubmitPilihan.setText(R.string.text_diserahkan);

                            bindingPilihan.radioGroup.setEnabled(false);
                            Helper.setSelectedRadioButton(bindingPilihan.radioGroup, submissionResponse.getAnswer());

                            if (submissionResponse.getStatus().equals("graded")) {
                                bindingPilihan.maxScore.setText(submissionResponse.getScore() + "/" + maxScore);
                            }
                        });
                    }
                });

                break;
            case "file_upload":
                viewModel.getAssignment(assignmentId).observe(this, classAssignmentResponse -> {
                    maxScore = classAssignmentResponse.getMax_score();
                    bindingFile.judulTugas.setText(classAssignmentResponse.getTitle());
                    bindingFile.maxPoint.setText(maxScore + " " + getString(R.string.poin));
                    bindingFile.deskripsi.setText(classAssignmentResponse.getDescription());
                    bindingFile.bottomJawaban.judulTugas.setText(classAssignmentResponse.getTitle());

                    if (classAssignmentResponse.getAttachment_path() != null){
                        Helper.loadImageWithGlide(this, classAssignmentResponse.getAttachment_path(), bindingFile.gambar);
                        bindingFile.namaGambar.setText(Helper.getFileNameFromPath(classAssignmentResponse.getAttachment_path()));

                        bindingFile.fileCard.setOnClickListener(v -> {
                            Helper.showFullScreenImageDialog(this, classAssignmentResponse.getAttachment_path());
                        });
                    } else {
                        bindingFile.linearLampiran.setVisibility(View.GONE);
                    }

                    if (classAssignmentResponse.getDue_date() != null) {
                        String formattedDate = Helper.formatDateYMDHI("Batas waktu : ", classAssignmentResponse.getDue_date());
                        bindingFile.dueDate.setText(formattedDate);
                        bindingFile.bottomJawaban.dueDate.setText(formattedDate);
                    }
                });

                viewModel.isAnswered().observe(this, isAnswered -> {
                    if (submissionId != 0) {
                        viewModel.getSubmissionById(submissionId).observe(this, submissionResponse -> {
                            bindingFile.bottomJawaban.btnRemove.setVisibility(View.GONE);

                            Helper.loadImageWithGlide(this, submissionResponse.getAttachment_path(), bindingFile.bottomJawaban.gambar);
                            bindingFile.bottomJawaban.namaGambar.setText(Helper.getFileNameFromPath(submissionResponse.getAttachment_path()));

                            bindingFile.bottomJawaban.btnAddTugas.setText(R.string.text_diserahkan);
                            bindingFile.btnUploadTugas.setText(R.string.text_diserahkan);
                            bindingFile.btnUploadTugas.setEnabled(false);
                            bindingFile.bottomJawaban.btnAddTugas.setEnabled(false);

                            if (submissionResponse.getStatus().equals("graded")) {
                                bindingFile.maxPoint.setText(submissionResponse.getScore() + "/" + maxScore);
                            }
                        });
                    }
                });

                break;
            case "essay":
                viewModel.getAssignment(assignmentId).observe(this, classAssignmentResponse -> {
                    maxScore = classAssignmentResponse.getMax_score();
                    bindingEsai.judulTugas.setText(classAssignmentResponse.getTitle());
                    bindingEsai.maxScore.setText(maxScore + " " + getString(R.string.poin));

                    if (classAssignmentResponse.getAttachment_path() != null){
                        Helper.loadImageWithGlide(this, classAssignmentResponse.getAttachment_path(), bindingEsai.gambar);
                        bindingEsai.namaGambar.setText(Helper.getFileNameFromPath(classAssignmentResponse.getAttachment_path()));

                        bindingEsai.fileCard.setOnClickListener(v -> {
                            Helper.showFullScreenImageDialog(this, classAssignmentResponse.getAttachment_path());
                        });
                    } else {
                        bindingEsai.linearLampiran.setVisibility(View.GONE);
                    }

                    if (classAssignmentResponse.getDue_date() != null) {
                        String formattedDate = Helper.formatDateYMDHI("Batas waktu : ", classAssignmentResponse.getDue_date());
                        bindingEsai.dueDate.setText(formattedDate);
                    }
                });

                viewModel.isAnswered().observe(this, isAnswered -> {
                    if (submissionId != 0) {
                        viewModel.getSubmissionById(submissionId).observe(this, submissionResponse -> {
                            bindingEsai.btnSubmit.setEnabled(false);
                            bindingEsai.btnSubmit.setText(R.string.text_diserahkan);

                            bindingEsai.edtJawabanEsai.setText(submissionResponse.getAnswer());

                            if (submissionResponse.getStatus().equals("graded")) {
                                bindingEsai.maxScore.setText(submissionResponse.getScore() + "/" + maxScore);
                            }
                        });
                    }
                });

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data != null) {
                Uri fileUri = data.getData();
                file = Helper.copyFileToAppDirectory(this, fileUri);
                if (file != null) {
                    Toast.makeText(this, "File berhasil diupload : " + file.getName(), Toast.LENGTH_SHORT).show();
                    bindingFile.bottomJawaban.fileCard.setVisibility(View.VISIBLE);
                    bindingFile.bottomJawaban.cardEmpty.setVisibility(View.GONE);
                    bindingFile.bottomJawaban.btnRemove.setVisibility(View.VISIBLE);

                    bindingFile.bottomJawaban.btnAddTugas.setText(R.string.text_serahkan);
                    bindingFile.btnUploadTugas.setText(R.string.text_serahkan);

                    Helper.loadImageWithGlide(this, file, bindingFile.bottomJawaban.gambar);
                    bindingFile.bottomJawaban.namaGambar.setText(file.getName());

                    changeClickListener();
                }
            }
        }
    }

    private void changeClickListener() {
        bindingFile.bottomJawaban.btnAddTugas.setOnClickListener(v -> {
            bindingFile.btnUploadTugas.setEnabled(false);
            viewModel.createSubmission(assignmentId, answer, file);
        });
        bindingFile.btnUploadTugas.setOnClickListener(v -> {
            bindingFile.btnUploadTugas.setEnabled(false);
            viewModel.createSubmission(assignmentId, answer, file);
        });
    }

    private void check() {
        boolean check = true;
        if (type.equals("essay")) {
            answer = bindingEsai.edtJawabanEsai.getText().toString();

            if (answer.isEmpty()) {
                Toast.makeText(this, "Jawaban Kosong!", Toast.LENGTH_SHORT).show();
                bindingEsai.edtJawabanEsai.setError("Tidak boleh kosong!");
                check = false;
            }
        } else if (type.equals("multiple_choice")) {
            answer = getSelectedRadioButtonValue(bindingPilihan.radioGroup);

            if (answer.isEmpty()) {
                Toast.makeText(this, "Jawaban Kosong!", Toast.LENGTH_SHORT).show();
                check = false;
            }
        }

        if (check) {
            viewModel.createSubmission(assignmentId, answer, file);
        } else {
            if (type.equals("essay")) {
                bindingEsai.btnSubmit.setEnabled(true);
            } else if (type.equals("multiple_choice")) {
                bindingPilihan.btnSubmitPilihan.setEnabled(true);
            }
        }
    }

    private String getSelectedRadioButtonValue(RadioGroup radioGroup) {
        // Mendapatkan ID RadioButton yang dipilih
        int selectedId = radioGroup.getCheckedRadioButtonId();

        if (selectedId != -1) {
            // Menemukan RadioButton yang dipilih
            RadioButton selectedRadioButton = findViewById(selectedId);

            // Mengambil teks dari RadioButton yang dipilih
            return selectedRadioButton.getText().toString();
        } else {
            // Tidak ada RadioButton yang dipilih
            return null;
        }
    }
}