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
import com.example.studyconnect.databinding.ActivityFormMateriBinding;
import com.example.studyconnect.databinding.BottomUploadDialogBinding;
import com.example.studyconnect.utils.Helper;
import com.example.studyconnect.utils.SharedPreferencesManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FormMateriActivity extends AppCompatActivity {

    @Inject
    SharedPreferencesManager sharedPreferencesManager;

    ActivityFormMateriBinding binding;
    private ClassAssignmentViewModel viewModel;
    private int classId, materialId, teacherId;
    private boolean isUpdate;
    private String link;
    private File photoFile, file = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormMateriBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(view -> finish());

        viewModel = new ViewModelProvider(this).get(ClassAssignmentViewModel.class);
        classId = getIntent().getIntExtra("classId", 0);
        isUpdate = getIntent().getBooleanExtra("isUpdate", false);
        materialId = getIntent().getIntExtra("materialId", 0);
        teacherId = sharedPreferencesManager.getData("teacherId", 0);

        initUpdate();
        initObserver();
        initView();
    }

    private void initUpdate() {
        if (isUpdate) {
            binding.btnPosting.setText(R.string.text_update);

            viewModel.getMaterial(materialId).observe(this, material -> {
                binding.edtTitleTugas.setText(material.getTitle());
                binding.edtDeskripsiTugas.setText(material.getDescription());
                if (material.getAttachment_path() != null) {
                    setFileShow(material.getAttachment_path());
                }
            });
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

            if (isUpdate) {
                viewModel.updateMaterial(
                        materialId, title, description, file, link, String.valueOf(teacherId)
                );
            } else {
                viewModel.createMaterial(
                        classId, title, description, file, link, String.valueOf(teacherId)
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
                    FormMateriActivity.this);
            BottomUploadDialogBinding uploadDialog = BottomUploadDialogBinding.inflate(
                    LayoutInflater.from(FormMateriActivity.this)
            );
            uploadDialog.btnUploadLink.setVisibility(View.VISIBLE);

            uploadDialog.btnUploadLink.setOnClickListener(v1 -> {
                // Buat dialog untuk memasukkan link
                AlertDialog.Builder builder = new AlertDialog.Builder(FormMateriActivity.this);
                builder.setTitle("Tambahkan Link");

                // Buat container (LinearLayout) untuk menambahkan padding
                LinearLayout container = new LinearLayout(FormMateriActivity.this);
                container.setOrientation(LinearLayout.VERTICAL);
                int padding = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()
                );
                container.setPadding(padding, padding, padding, padding); // Atur padding

                // Tambahkan input field
                final EditText input = new EditText(FormMateriActivity.this);
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
                        Toast.makeText(FormMateriActivity.this, "URL tidak valid", Toast.LENGTH_SHORT).show();
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