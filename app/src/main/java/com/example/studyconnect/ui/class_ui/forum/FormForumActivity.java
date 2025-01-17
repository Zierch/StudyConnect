package com.example.studyconnect.ui.class_ui.forum;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyconnect.R;
import com.example.studyconnect.databinding.AddForumDialogBinding;
import com.example.studyconnect.databinding.BottomTwoDialogBinding;
import com.example.studyconnect.databinding.BottomUploadDialogBinding;
import com.example.studyconnect.utils.Helper;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FormForumActivity extends AppCompatActivity {
    // Konstanta untuk request code
    private static final int REQUEST_CODE_PICK_FILE = 1001;
    private static final int REQUEST_CODE_PICK_IMAGE = 1002;
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 1003;
    private AddForumDialogBinding binding;
    private ForumViewModel viewModel;
    private boolean isUpdate = false;
    private int classId, announcementId;
    private String scheduledAt;
    private File photoFile, file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddForumDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(ForumViewModel.class);
        isUpdate = getIntent().getBooleanExtra("isUpdate", false);
        classId = getIntent().getIntExtra("AnnouncementClassId", 0);
        announcementId = getIntent().getIntExtra("AnnouncementId", 0);

        viewModel.getIsLoading().observe(this, isLoading ->
                Helper.showLoading(binding.progressBar.progressBar, isLoading)
        );

        viewModel.getIsSuccess().observe(this, isSuccess -> {
            if (isSuccess) {
                finish();
            }
        });

        viewModel.getErrorMsg().observe(this, errorMsg -> {
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
            binding.btnPosting.setEnabled(true);
        });

        initView();
    }

    @SuppressLint({"QueryPermissionsNeeded", "SetTextI18n"})
    private void initView() {
        // Set up the toolbar with back button functionality
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        if (isUpdate) {
            binding.btnSettings.setVisibility(View.GONE);
            viewModel.getAnnouncementData(announcementId).observe(this, announcementResponse -> {
                binding.edtDeskripsiForum.setText(announcementResponse.getContent());

                if (announcementResponse.getAttachment_path() != null) {
                    String attachmentPath = announcementResponse.getAttachment_path();
                    String fileName = attachmentPath.substring(attachmentPath.lastIndexOf("_") + 1);

                    // Tampilkan nama file di UI
                    binding.namaFile.setText(fileName);
                    binding.btnAddFile.setEnabled(false);
                    binding.btnRemove.setVisibility(View.VISIBLE);
                }

                if (announcementResponse.getStatus().equals("scheduled")) {
                    TimeZone wibTimeZone = TimeZone.getTimeZone("GMT+7");
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    sdf.setTimeZone(wibTimeZone);

                    String formattedDate = sdf.format(new Date());
                    System.out.println("Waktu WIB: " + formattedDate);

                    // Tampilkan tanggal yang sudah diformat ke UI
                    binding.viewJadwal.setVisibility(View.VISIBLE);
                    binding.waktuJadwal.setText("Dijadwalkan : " + formattedDate);
                    binding.btnRemoveJadwal.setVisibility(View.GONE);
                }
            });
        }

        binding.btnRemove.setVisibility(View.GONE);
        binding.btnAddFile.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    FormForumActivity.this);
            BottomUploadDialogBinding bottomUploadDialogBinding = BottomUploadDialogBinding.inflate(
                    LayoutInflater.from(FormForumActivity.this)
            );

            // Listener untuk memilih file dari penyimpanan
            bottomUploadDialogBinding.btnUploadFile.setOnClickListener(v1 -> {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*"); // Menerima semua jenis file
                startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
                bottomSheetDialog.dismiss();
            });

            // Listener untuk memilih foto dari galeri
            bottomUploadDialogBinding.btnUploadFoto.setOnClickListener(v1 -> {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                bottomSheetDialog.dismiss();
            });

            // Listener untuk mengambil foto menggunakan kamera
            bottomUploadDialogBinding.btnUploadCam.setOnClickListener(v1 -> {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    photoFile = Helper.createImageFile(this); // Buat file untuk menyimpan foto
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

            bottomSheetDialog.setContentView(bottomUploadDialogBinding.getRoot());
            bottomSheetDialog.show();
        });

        binding.btnRemove.setOnClickListener(v -> {
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
            photoFile = null;

            binding.namaFile.setText(R.string.text_tambahkan_lampiran);
            binding.btnAddFile.setEnabled(true);
            binding.btnRemove.setVisibility(View.GONE);
        });

        binding.btnPosting.setOnClickListener(v -> {
            binding.btnPosting.setEnabled(false);
            check();
        });

        binding.btnRemoveJadwal.setOnClickListener(v2 -> removeScheduledAt());

        binding.btnSettings.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    FormForumActivity.this);
            BottomTwoDialogBinding bottomSheetBinding = BottomTwoDialogBinding.inflate(
                    LayoutInflater.from(FormForumActivity.this));

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

                // Tutup BottomSheet setelah memilih tanggal dan waktu
                bottomSheetDialog.dismiss();
            });

            bottomSheetBinding.option2.setVisibility(View.GONE);
            bottomSheetDialog.setContentView(bottomSheetBinding.getRoot());
            bottomSheetDialog.show();
        });
    }

    private void removeScheduledAt() {
        // Reset variabel scheduledAt
        this.scheduledAt = null;

        // Update UI
        binding.viewJadwal.setVisibility(View.GONE);

        Toast.makeText(this, "Jadwal dihapus", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    private void setScheduledAt(String scheduledAt) {
        this.scheduledAt = scheduledAt;

        // Tampilkan nilai yang dipilih ke UI (opsional)
        binding.viewJadwal.setVisibility(View.VISIBLE);
        binding.waktuJadwal.setText("Dijadwalkan : " + scheduledAt);
    }

    private void check() {
        boolean check = true;

//        get variabel
        String content = Objects.requireNonNull(binding.edtDeskripsiForum.getText()).toString();

        if (content.isEmpty()) {
            Toast.makeText(this, "Isi Pengumuman Kosong!", Toast.LENGTH_SHORT).show();
            binding.edtDeskripsiForum.setError("Tidak boleh kosong!");
            check = false;
        }

        if (check) {
            if (isUpdate) {
                viewModel.updateAnnouncement(announcementId, content, file);
            } else {
                viewModel.createAnnouncement(classId, content, scheduledAt, file);
            }
        }
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
                            binding.namaFile.setText(file.getName());
                            binding.btnAddFile.setEnabled(false);
                            binding.btnRemove.setVisibility(View.VISIBLE);
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