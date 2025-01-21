package com.example.studyconnect.ui.class_ui.forum;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyconnect.databinding.InfoPengumumanBinding;
import com.example.studyconnect.ui.class_ui.assignment.ViewMateriActivity;
import com.example.studyconnect.utils.Helper;

import java.io.File;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ViewForumActivity extends AppCompatActivity {
    private InfoPengumumanBinding binding;
    private ForumViewModel viewModel;
    private int announcementId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = InfoPengumumanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(ForumViewModel.class);
        announcementId = getIntent().getIntExtra("announcementId", 0);

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

        viewModel.getAnnouncementData(announcementId).observe(this, announcement -> {
            binding.tvDeskripsiForum.setText(announcement.getContent());
            if (announcement.getAttachment_path() == null) {
                binding.fileCard.setVisibility(View.GONE);
            } else {
                binding.namaGambar.setText(Helper.getFileNameFromPath(announcement.getAttachment_path()));
                binding.namaPdf.setText(Helper.getFileNameFromPath(announcement.getAttachment_path()));
                if (Helper.isPdfFile(Helper.getFileNameFromPath(announcement.getAttachment_path()))) {
                    binding.fileCard.setVisibility(View.GONE);
                    binding.pdfCard.setVisibility(View.VISIBLE);

                    viewModel.isLoading.postValue(true);

                    Helper.downloadPdfToCache(this, announcement.getAttachment_path(), new Helper.PdfDownloadListener() {
                        @Override
                        public void onDownloadSuccess(File pdfFile) {
                            Helper.showPdfPreview(binding.pdfView, pdfFile);
                            binding.pdfView.setOnClickListener(v -> {
                                Helper.showFullScreenFileDialog(ViewForumActivity.this, announcement.getAttachment_path(), pdfFile);
                            });
                            viewModel.isLoading.postValue(false);
                        }

                        @Override
                        public void onDownloadFailed(String errorMessage) {
                            viewModel.isLoading.postValue(false);
                            Toast.makeText( ViewForumActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Helper.loadImageWithGlide(this, announcement.getAttachment_path(), binding.gambar);

                    binding.fileCard.setOnClickListener(v -> {
                        Helper.showFullScreenFileDialog(this, announcement.getAttachment_path(), null);
                    });
                }
            }

        });
    }
}