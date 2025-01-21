package com.example.studyconnect.ui.class_ui.assignment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyconnect.databinding.InfoMateriBinding;
import com.example.studyconnect.utils.Helper;

import java.io.File;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ViewMateriActivity extends AppCompatActivity {
    private InfoMateriBinding binding;
    private ClassAssignmentViewModel viewModel;
    private int materiId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = InfoMateriBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(ClassAssignmentViewModel.class);
        materiId = getIntent().getIntExtra("materiId", 0);

        initObserver();
        initView();
    }

    private void initView() {
        // Set up the toolbar with back button functionality
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void initObserver() {
        viewModel.getIsLoading().observe(this, isLoading ->
                Helper.showLoading(binding.progressBar.progressBar, isLoading)
        );

        viewModel.getErrorMsg().observe(this, errorMsg -> {
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
        });

        viewModel.getMaterial(materiId).observe(this, materialResponse -> {
            binding.judul.setText(materialResponse.getTitle());
            if (materialResponse.getDescription() != null) {
                binding.tvDeskripsi.setText(materialResponse.getDescription());
            } else {
                binding.tvDeskripsi.setVisibility(View.GONE);
            }
            if (materialResponse.getAttachment_path() != null) {
                binding.namaGambar.setText(Helper.getFileNameFromPath(materialResponse.getAttachment_path()));
                binding.namaPdf.setText(Helper.getFileNameFromPath(materialResponse.getAttachment_path()));

                if (Helper.isPdfFile(Helper.getFileNameFromPath(materialResponse.getAttachment_path()))) {
                    binding.fileCard.setVisibility(View.GONE);
                    binding.pdfCard.setVisibility(View.VISIBLE);

                    viewModel.isLoading.postValue(true);

                    Helper.downloadPdfToCache(this, materialResponse.getAttachment_path(), new Helper.PdfDownloadListener() {
                        @Override
                        public void onDownloadSuccess(File pdfFile) {
                            Helper.showPdfPreview(binding.pdfView, pdfFile);
                            binding.pdfView.setOnClickListener(v -> {
                                Helper.showFullScreenFileDialog(ViewMateriActivity.this, materialResponse.getAttachment_path(), pdfFile);
                            });
                            viewModel.isLoading.postValue(false);
                        }

                        @Override
                        public void onDownloadFailed(String errorMessage) {
                            viewModel.isLoading.postValue(false);
                            Toast.makeText(ViewMateriActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Helper.loadImageWithGlide(this, materialResponse.getAttachment_path(), binding.gambar);
                    binding.fileCard.setOnClickListener(v -> {
                        Helper.showFullScreenFileDialog(this, materialResponse.getAttachment_path(), null);
                    });
                }
            }
        });
    }
}