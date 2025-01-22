package com.example.studyconnect.ui.class_ui.assignment;

import static com.example.studyconnect.utils.Helper.REQUEST_CODE_CAPTURE_IMAGE;
import static com.example.studyconnect.utils.Helper.REQUEST_CODE_PICK_FILE;
import static com.example.studyconnect.utils.Helper.REQUEST_CODE_PICK_IMAGE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyconnect.R;
import com.example.studyconnect.databinding.ActivityFormTugasBinding;
import com.example.studyconnect.databinding.BottomTwoDialogBinding;
import com.example.studyconnect.databinding.BottomUploadDialogBinding;
import com.example.studyconnect.utils.Helper;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FormTugasActivity extends AppCompatActivity {
    private ActivityFormTugasBinding binding;
    private ClassAssignmentViewModel viewModel;
    private int classId;
    private int assignmentId;
    private boolean isUpdate;
    private String scheduledAt;
    private String dueDate;
    private File photoFile, file = null;
    private String type, link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormTugasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        viewModel = new ViewModelProvider(this).get(ClassAssignmentViewModel.class);
        classId = getIntent().getIntExtra("classId", 0);
        isUpdate = getIntent().getBooleanExtra("isUpdate", false);
        assignmentId = getIntent().getIntExtra("assignmentId", 0);

        initUpdate();
        initObserver();
        initView();
    }

    private void initUpdate() {
        if (isUpdate) {
            binding.btnPosting.setText(R.string.text_update);
            binding.btnScheduleAt.setVisibility(View.GONE);
            binding.btnRemoveJadwal.setVisibility(View.GONE);

            viewModel.getAssignment(assignmentId).observe(this, assignment -> {
                binding.edtTitleTugas.setText(assignment.getTitle());
                binding.edtDeskripsiTugas.setText(assignment.getDescription());
                binding.edtPoinTugas.setText(String.valueOf(assignment.getMax_score()));
                binding.switchDueDate.setChecked(assignment.getDue_date() != null);
                type = assignment.getType();

                if (assignment.getAttachment_path() != null) {
                    setFileShow(assignment.getAttachment_path());
                }

                if (assignment.getDue_date() != null) {
                    setDueDate(Helper.formatDateYMDHI(assignment.getDue_date()));
                }

                if (assignment.getScheduled_at() != null) {
                    setScheduledAt(Helper.formatDateYMDHI(assignment.getScheduled_at()));
                }
            });
        } else {
            binding.btnScheduleAt.setVisibility(View.VISIBLE);
            binding.btnRemoveJadwal.setVisibility(View.VISIBLE);
        }
    }

    private void setFileShow(String attachment_path) {
        binding.namaFile.setText(Helper.getFileName(attachment_path));
        binding.btnRemove.setVisibility(View.VISIBLE);
        binding.btnAddFile.setEnabled(false);
    }

    private void initObserver() {
        viewModel.getIsLoading().observe(this, isLoading ->
                Helper.showLoading(binding.progressBar.progressBar, isLoading)
        );

        viewModel.getErrorMsg().observe(this, errorMessage ->
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        );

        viewModel.getIsSuccess().observe(this, isSuccess -> {
            if (isSuccess) {
                finish();
            }
        });
    }

    @SuppressLint({"SetTextI18n", "QueryPermissionsNeeded"})
    private void initView() {
        binding.btnPosting.setOnClickListener(v -> {
            String title = Objects.requireNonNull(binding.edtTitleTugas.getText()).toString();
            String description = Objects.requireNonNull(binding.edtDeskripsiTugas.getText()).toString();
            String points = Objects.requireNonNull(binding.edtPoinTugas.getText()).toString();
            type = "file_upload";

            if (isUpdate) {
                viewModel.updateAssignment(
                        assignmentId, title, description, dueDate,
                        points, file, link, type, ""
                );
            } else {
                viewModel.createAssignment(
                        classId, title, description, dueDate, scheduledAt,
                        points, file, link, type, ""
                );
            }
        });

        binding.btnRemove.setOnClickListener(v -> {
            if (file != null && file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    Toast.makeText(
                            this,
                            "File dihapus: " + file.getName(),
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    Toast.makeText(
                            this,
                            "Gagal menghapus file",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            file = null;
            photoFile = null;

            binding.namaFile.setText(R.string.text_tambahkan_lampiran);
            binding.btnAddFile.setEnabled(true);
            binding.btnRemove.setVisibility(View.GONE);
        });

        binding.btnAddFile.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    FormTugasActivity.this);
            BottomUploadDialogBinding uploadDialog = BottomUploadDialogBinding.inflate(
                    LayoutInflater.from(FormTugasActivity.this)
            );
            uploadDialog.btnUploadLink.setVisibility(View.VISIBLE);

            uploadDialog.btnUploadLink.setOnClickListener(v1 -> {
                // Buat dialog untuk memasukkan link
                AlertDialog.Builder builder = new AlertDialog.Builder(FormTugasActivity.this);
                builder.setTitle("Tambahkan Link");

                // Buat container (LinearLayout) untuk menambahkan padding
                LinearLayout container = new LinearLayout(FormTugasActivity.this);
                container.setOrientation(LinearLayout.VERTICAL);
                int padding = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()
                );
                container.setPadding(padding, padding, padding, padding); // Atur padding

                // Tambahkan input field
                final EditText input = new EditText(FormTugasActivity.this);
                input.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
                input.setHint("Tambahkan link");
                container.addView(input);

                // Set container sebagai view untuk dialog
                builder.setView(container);

                builder.setPositiveButton("Tambahkan", (dialog, which) -> {
                    String url = input.getText().toString().trim();

                    // Validasi URL
                    if (!url.isEmpty() && android.util.Patterns.WEB_URL.matcher(url).matches()) {
                        link = url;
                        binding.namaFile.setText(link);
                        binding.btnRemove.setVisibility(View.VISIBLE);
                        binding.btnAddFile.setEnabled(false);
                    } else {
                        Toast.makeText(FormTugasActivity.this, "URL tidak valid", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Batal", (dialog, which) -> {
                    dialog.cancel();
                });

                // Tampilkan dialog
                AlertDialog dialog = builder.create();
                dialog.show();

                // Tutup bottom sheet
                bottomSheetDialog.dismiss();
            });

            uploadDialog.btnUploadFile.setOnClickListener(v1 -> {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
                bottomSheetDialog.dismiss();
            });

            uploadDialog.btnUploadFoto.setOnClickListener(v1 -> {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                );
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                bottomSheetDialog.dismiss();
            });

            uploadDialog.btnUploadCam.setOnClickListener(v1 -> {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    photoFile = Helper.createImageFile(this);

                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(this,
                                "com.example.studyconnect.fileprovider",
                                photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(intent, REQUEST_CODE_CAPTURE_IMAGE);

                        bottomSheetDialog.dismiss();
                    }
                }
            });

            bottomSheetDialog.setContentView(uploadDialog.getRoot());
            bottomSheetDialog.show();
        });

        binding.switchDueDate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Helper.showDatePickerDialog(this, (selectedYear, selectedMonth, selectedDay) ->
                        Helper.showTimePickerDialog(
                                this,
                                selectedYear,
                                selectedMonth,
                                selectedDay,
                                this::setDueDate
                        ));
            } else {
                removeDueDate();
            }
        });

        binding.btnDueDate.setOnClickListener(v ->
                binding.switchDueDate.setChecked(!binding.switchDueDate.isChecked())
        );

        binding.btnRemoveJadwal.setOnClickListener(v -> removeScheduledAt());

        binding.btnScheduleAt.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog =
                    new BottomSheetDialog(FormTugasActivity.this);
            BottomTwoDialogBinding bottomSheetBinding =
                    BottomTwoDialogBinding.inflate(
                            LayoutInflater.from(FormTugasActivity.this)
                    );

            bottomSheetBinding.option1.setText(R.string.jadwalkan);
            bottomSheetBinding.option1.setOnClickListener(v1 -> {
                Helper.showDatePickerDialog(this, (selectedYear, selectedMonth, selectedDay) ->
                        Helper.showTimePickerDialog(
                                this,
                                selectedYear,
                                selectedMonth,
                                selectedDay,
                                this::setScheduledAt
                        ));

                bottomSheetDialog.dismiss();
            });


            bottomSheetBinding.option2.setVisibility(View.GONE);
            bottomSheetDialog.setContentView(bottomSheetBinding.getRoot());
            bottomSheetDialog.show();
        });
    }

    @SuppressLint("SetTextI18n")
    private void setScheduledAt(String getDate) {
        this.scheduledAt = getDate;

        binding.viewJadwal.setVisibility(View.VISIBLE);
        binding.waktuJadwal.setText("Jadwal : " + scheduledAt);
    }

    private void removeScheduledAt() {
        this.scheduledAt = null;

        binding.viewJadwal.setVisibility(View.GONE);

        Toast.makeText(this, "Jadwal dihapus", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    private void setDueDate(String getDate) {
        this.dueDate = getDate;

        binding.tvDueDate.setText("Batas Waktu : " + dueDate);
        binding.switchDueDate.setChecked(true);
    }

    private void removeDueDate() {
        this.dueDate = null;

        binding.tvDueDate.setText(R.string.text_batas_waktu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_PICK_FILE:
                case REQUEST_CODE_PICK_IMAGE:
                    if (data != null) {
                        Uri fileUri = data.getData();
                        file = Helper.copyFileToAppDirectory(this, fileUri);
                        if (file != null) {
                            setFileShow(file.getName());
                        }
                    }
                    break;

                case REQUEST_CODE_CAPTURE_IMAGE:
                    if (photoFile != null) {
                        file = photoFile;
                        binding.namaFile.setText(file.getName());
                        binding.btnAddFile.setEnabled(false);
                        binding.btnRemove.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }
}