package com.example.studyconnect.ui.class_ui.assignment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyconnect.R;
import com.example.studyconnect.databinding.DetailTugasStudentBinding;
import com.example.studyconnect.utils.Helper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ViewStudentAssignmentActivity extends AppCompatActivity {
    private DetailTugasStudentBinding binding;
    private ClassAssignmentViewModel viewModel;
    private int assignmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DetailTugasStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(ClassAssignmentViewModel.class);
        assignmentId = getIntent().getIntExtra("assignmentId", 0);

        initObserver();
        initView();
    }

    private void initView() {
        // Set up the toolbar with back button functionality
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());


        // Di dalam Activity atau Fragment
        MaterialCardView bottomCard = findViewById(R.id.bottomCard);
        BottomSheetBehavior<MaterialCardView> bottomSheetBehavior = BottomSheetBehavior.from(bottomCard);

        // Mengatur keadaan awal
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setFitToContents(false);
        bottomSheetBehavior.setSkipCollapsed(true);

        // Menambahkan callback untuk menangani perubahan keadaan
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        // Tampilkan layout bottom_jawaban saat di-expand
                        binding.bottomJawaban.main.setVisibility(View.VISIBLE);
                        binding.llCardTop.setVisibility(View.GONE);
                        binding.bottomJawaban.main.animate().alpha(1f).setDuration(300).start();
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        // Sembunyikan layout bottom_jawaban saat collapsed
                        binding.llCardTop.setVisibility(View.VISIBLE);
                        binding.bottomJawaban.main.animate().alpha(0f).setDuration(300).withEndAction(() -> {
                            binding.bottomJawaban.main.setVisibility(View.GONE);
                        }).start();
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        // Sembunyikan layout bottom_jawaban saat hidden
                        binding.bottomJawaban.main.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // Anda bisa menambahkan animasi atau logika tambahan saat BottomSheet digeser
            }
        });
    }

    private void initObserver() {
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
//            binding.btnPosting.setEnabled(true);
        });

        viewModel.getAssignment(assignmentId).observe(this, classAssignmentResponse -> {
            Log.d("TAG", "initObserver: "+classAssignmentResponse);
            binding.judulTugas.setText(classAssignmentResponse.getTitle());
            binding.maxPoint.setText(classAssignmentResponse.getMax_score() + " " + getString(R.string.poin));
            binding.deskripsi.setText(classAssignmentResponse.getDescription());
        });
    }
}