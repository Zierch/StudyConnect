package com.example.studyconnect.ui.class_ui.assignment.tab_view_guru;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyconnect.databinding.FragmentPetunjukTugasBinding;
import com.example.studyconnect.ui.class_ui.assignment.SubmissionViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PetunjukFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private SubmissionViewModel viewModel;
    private FragmentPetunjukTugasBinding binding;

    public static PetunjukFragment newInstance(int index) {
        PetunjukFragment fragment = new PetunjukFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentPetunjukTugasBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(SubmissionViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}