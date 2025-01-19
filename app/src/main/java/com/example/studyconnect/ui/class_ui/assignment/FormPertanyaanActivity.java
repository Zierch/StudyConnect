package com.example.studyconnect.ui.class_ui.assignment;

import static com.example.studyconnect.utils.Helper.REQUEST_CODE_CAPTURE_IMAGE;
import static com.example.studyconnect.utils.Helper.REQUEST_CODE_PICK_FILE;
import static com.example.studyconnect.utils.Helper.REQUEST_CODE_PICK_IMAGE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyconnect.R;
import com.example.studyconnect.databinding.ActivityFormPertanyaanBinding;
import com.example.studyconnect.databinding.BottomTwoDialogBinding;
import com.example.studyconnect.databinding.BottomUploadDialogBinding;
import com.example.studyconnect.databinding.ItemPilihanGandaBinding;
import com.example.studyconnect.utils.Helper;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FormPertanyaanActivity extends AppCompatActivity {
    private ActivityFormPertanyaanBinding binding;
    private ClassAssignmentViewModel viewModel;
    private int questionPosition = 1;
    private int classId, assignmentId;
    private boolean isUpdate, isEssay = false, isSuccess = false;
    private String scheduledAt, dueDate, type, options = "";
    private File photoFile, file = null;
    private final Map<Integer, Map<String, Integer>> idMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormPertanyaanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(view -> finish());

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
                isEssay = Objects.equals(type, "essay");
                options = assignment.getOptions();

                Log.d("form", options);
                Log.d("form2", type);

                if (assignment.getOptions() != null) {
                    binding.tvBtnPilihan.setText(R.string.text_pilihan_ganda);

                    try {
                        JSONArray optionsArray = new JSONArray(assignment.getOptions());
                        for (int i = 0; i < optionsArray.length(); i++) {
                            String option = optionsArray.getString(i);

                            setRadioPilihan(true, option);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    binding.tvBtnPilihan.setText(R.string.text_jawaban_singkat);
                }

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
        binding.btnPilihanGanda.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    FormPertanyaanActivity.this
            );
            BottomTwoDialogBinding bottomSheetBinding =
                    BottomTwoDialogBinding.inflate(
                            LayoutInflater.from(FormPertanyaanActivity.this)
                    );

            bottomSheetBinding.option1.setText(R.string.text_jawaban_singkat);
            bottomSheetBinding.option2.setText(R.string.text_pilihan_ganda);

            bottomSheetBinding.option1.setOnClickListener(view -> {
                binding.btnAddPilihan.setVisibility(View.GONE);
                binding.parentPilihan.setVisibility(View.GONE);
                binding.tvBtnPilihan.setText(R.string.text_jawaban_singkat);
                isEssay = true;

                bottomSheetDialog.dismiss();
            });

            bottomSheetBinding.option2.setOnClickListener(view -> {
                binding.btnAddPilihan.setVisibility(View.VISIBLE);
                binding.parentPilihan.setVisibility(View.VISIBLE);
                binding.tvBtnPilihan.setText(R.string.text_pilihan_ganda);
                isEssay = false;

                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.setContentView(bottomSheetBinding.getRoot());
            bottomSheetDialog.show();
        });

        View.OnClickListener onClickAddPilihan = view -> {
            if (questionPosition <= 4) {
                setRadioPilihan(false, null);
            }
        };

        binding.btnAddPilihan.setOnClickListener(onClickAddPilihan);
        binding.rbPilihanParent.setOnClickListener(onClickAddPilihan);

        binding.btnPosting.setOnClickListener(view -> {
            isSuccess = false;
            String title = Objects.requireNonNull(binding.edtTitleTugas.getText()).toString();
            String description = Objects.requireNonNull(binding.edtDeskripsiTugas.getText()).toString();
            String points = Objects.requireNonNull(binding.edtPoinTugas.getText()).toString();

            if (!isEssay) {
                type = "multiple_choice";

                if (questionPosition - 1 >= 2 && questionPosition - 1 <= 4) {
                    isSuccess = true;
                    StringBuilder optionsBuilder = new StringBuilder();

                    for (Map.Entry<Integer, Map<String, Integer>> entry : idMap.entrySet()) {
                        Map<String, Integer> componentIds = entry.getValue();

                        int edtPilihanId = componentIds.get("edtPilihan1");

                        TextInputEditText edtPilihan = binding.getRoot().findViewById(edtPilihanId);

                        optionsBuilder.append(Objects.requireNonNull(
                                edtPilihan.getText()
                        )).append(",");
                    }

                    if (optionsBuilder.length() > 0 &&
                            optionsBuilder.charAt(optionsBuilder.length() - 1) == ',') {
                        optionsBuilder.deleteCharAt(optionsBuilder.length() - 1);
                    }

                    options = optionsBuilder.toString();
                } else {
                    isSuccess = false;
                    Toast.makeText(
                            this,
                            "Jumlah pilihan minimal 2 dan maksimal 4",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            } else {
                type = "essay";
                isSuccess = true;
            }

            if (isSuccess) {
                if (isUpdate) {
                    Log.d("form3", options);
                    Log.d("form4", type);
                    viewModel.updateAssignment(
                            assignmentId, title, description, dueDate,
                            points, file, type, options
                    );
                } else {
                    viewModel.createAssignment(
                            classId, title, description, dueDate, scheduledAt,
                            points, file, type, options
                    );
                }
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
                    FormPertanyaanActivity.this);
            BottomUploadDialogBinding uploadDialog = BottomUploadDialogBinding.inflate(
                    LayoutInflater.from(FormPertanyaanActivity.this)
            );

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
                    new BottomSheetDialog(FormPertanyaanActivity.this);
            BottomTwoDialogBinding bottomSheetBinding =
                    BottomTwoDialogBinding.inflate(
                            LayoutInflater.from(FormPertanyaanActivity.this)
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

    private void setRadioPilihan(boolean isUpdate, String option) {
        LinearLayout parentLayout = binding.parentPilihan;

        ItemPilihanGandaBinding itemBinding = ItemPilihanGandaBinding.inflate(
                LayoutInflater.from(
                        FormPertanyaanActivity.this
                ), parentLayout, false
        );

        int rootId = View.generateViewId();
        int rbPilihanId = View.generateViewId();
        int edtPilihanId = View.generateViewId();
        int btnRemoveId = View.generateViewId();

        itemBinding.getRoot().setId(rootId);
        itemBinding.rbPilihan1.setId(rbPilihanId);
        itemBinding.edtPilihan1.setId(edtPilihanId);
        itemBinding.btnRemovePilihan.setId(btnRemoveId);

        if (isUpdate) {
            itemBinding.edtPilihan1.setText(option);
        }

        Map<String, Integer> componentIds = new HashMap<>();
        componentIds.put("root", rootId);
        componentIds.put("rbPilihan1", rbPilihanId);
        componentIds.put("edtPilihan1", edtPilihanId);
        componentIds.put("btnRemovePilihan", btnRemoveId);

        idMap.put(rootId, componentIds);

        itemBinding.edtPilihan1.setHint("Pilihan " + questionPosition);

        itemBinding.btnRemovePilihan.setOnClickListener(v -> {
            parentLayout.removeView(itemBinding.getRoot());
            idMap.remove(rootId);
            questionPosition--;
        });

        parentLayout.addView(itemBinding.getRoot());
        questionPosition++;
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

    private void setFileShow(String attachment_path) {
        binding.namaFile.setText(Helper.getFileName(attachment_path));
        binding.btnRemove.setVisibility(View.VISIBLE);
        binding.btnAddFile.setEnabled(false);
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