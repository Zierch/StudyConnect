package com.example.studyconnect.ui.class_ui.assignment.tab_view_guru;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyconnect.R;
import com.example.studyconnect.databinding.FragmentPetunjukTugasBinding;
import com.example.studyconnect.ui.class_ui.assignment.SubmissionViewModel;
import com.example.studyconnect.utils.Helper;
import com.example.studyconnect.utils.SharedPreferencesManager;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PetunjukFragment extends Fragment {

    private SubmissionViewModel viewModel;
    private FragmentPetunjukTugasBinding binding;
    private int assignmentId, maxScore;
    @Inject
    SharedPreferencesManager sharedPreferencesManager;

    public static PetunjukFragment newInstance(int assignmentId) {
        PetunjukFragment fragment = new PetunjukFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("assignmentId", assignmentId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        assert getArguments() != null;
        assignmentId = getArguments().getInt("assignmentId");

        binding = FragmentPetunjukTugasBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(SubmissionViewModel.class);

        initObserver();
        return binding.getRoot();
    }

    private void initObserver(){
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading ->
                Helper.showLoading(binding.progressBar.progressBar, isLoading)
        );

        viewModel.getErrorMsg().observe(getViewLifecycleOwner(), errorMsg -> {
            Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show();
        });

        viewModel.getAssignment(assignmentId).observe(getViewLifecycleOwner(), classAssignmentResponse -> {
            sharedPreferencesManager.saveData("assignmentType", classAssignmentResponse.getType());
            sharedPreferencesManager.saveData("maxScore", classAssignmentResponse.getMax_score());
            sharedPreferencesManager.saveData("options", classAssignmentResponse.getOptions());


            maxScore = classAssignmentResponse.getMax_score();
            binding.judulTugas.setText(classAssignmentResponse.getTitle());
            binding.maxPoint.setText(maxScore + " " + getString(R.string.poin));

            if (classAssignmentResponse.getAttachment_path() != null){
                Helper.loadImageWithGlide(requireContext(), classAssignmentResponse.getAttachment_path(), binding.gambar);
                binding.namaGambar.setText(Helper.getFileNameFromPath(classAssignmentResponse.getAttachment_path()));

                binding.fileCard.setOnClickListener(v -> {
                    Helper.showFullScreenImageDialog(requireContext(), classAssignmentResponse.getAttachment_path());
                });
            } else {
                binding.linearLampiran.setVisibility(View.GONE);
            }

            if (classAssignmentResponse.getDue_date() != null) {
                String formattedDate = Helper.formatDateYMDHI("Batas waktu : ", classAssignmentResponse.getDue_date());
                binding.dueDate.setText(formattedDate);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}