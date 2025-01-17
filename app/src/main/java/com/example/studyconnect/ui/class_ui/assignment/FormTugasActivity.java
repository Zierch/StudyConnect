package com.example.studyconnect.ui.class_ui.assignment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyconnect.R;
import com.example.studyconnect.databinding.ActivityFormTugasBinding;
import com.example.studyconnect.databinding.BottomTwoDialogBinding;
import com.example.studyconnect.utils.Helper;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FormTugasActivity extends AppCompatActivity {
    private ActivityFormTugasBinding binding;
    private ClassAssignmentViewModel viewModel;
    private int classId;
    private String scheduledAt;
    private String dueDate;
    private Boolean isUpdate;
    private int assignmentId;

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

            viewModel.fetchAssignmentById(assignmentId);
            viewModel.getAssignment().observe(this, assignment -> {
                binding.edtTitleTugas.setText(assignment.getTitle());
                binding.edtDeskripsiTugas.setText(assignment.getDescription());
                binding.edtPoinTugas.setText(String.valueOf(assignment.getMax_score()));
                binding.switchDueDate.setChecked(assignment.getDue_date() != null);

                if (assignment.getDue_date() != null) {
                    setDueDate(Helper.formatDateYMDHI(assignment.getDue_date()));
                }

                if (assignment.getScheduled_at() != null) {
                    setScheduledAt(Helper.formatDateYMDHI(assignment.getScheduled_at()));
                }
            });
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

    @SuppressLint("SetTextI18n")
    private void initView() {
        binding.btnPosting.setOnClickListener(v -> {
            String title = Objects.requireNonNull(binding.edtTitleTugas.getText()).toString();
            String description = Objects.requireNonNull(binding.edtDeskripsiTugas.getText()).toString();
            String points = Objects.requireNonNull(binding.edtPoinTugas.getText()).toString();
            String fileUrl = "";
            int type = 1;

            if (isUpdate) {
                viewModel.updateAssignment(
                        assignmentId, classId, title, description, dueDate, scheduledAt,
                        points, fileUrl, type
                );
            } else {
                viewModel.createAssignment(
                        classId, title, description, dueDate, scheduledAt,
                        points, fileUrl, type
                );
            }
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

        // Update UI
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

        // Update UI
        binding.tvDueDate.setText("Batas Waktu : " + dueDate);
        binding.switchDueDate.setChecked(true);
    }

    private void removeDueDate() {
        this.dueDate = null;

        binding.tvDueDate.setText(R.string.text_batas_waktu);
    }
}