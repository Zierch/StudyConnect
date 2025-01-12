package com.example.studyconnect.ui.class_ui.forum;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyconnect.databinding.FragmentForumBinding;
import com.example.studyconnect.utils.Helper;

public class ForumFragment extends Fragment {

    private FragmentForumBinding binding;
    private ForumViewModel viewModel;
    private int classId;

    public static ForumFragment newInstance(int classId) {
        ForumFragment fragment = new ForumFragment();
        Bundle args = new Bundle();
        args.putInt("classId", classId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            classId = getArguments().getInt("classId");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentForumBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ForumViewModel.class);

        initObserver();

        return binding.getRoot();
    }

    private void initObserver() {
        viewModel.fetchClass(classId);

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            Log.d("Loading", String.valueOf(isLoading));
            Helper.showLoading(binding.progressBarIncl.progressBar, isLoading);
        });

        viewModel.getClassData().observe(getViewLifecycleOwner(), classes -> {
            if (classes != null) {
                binding.namaKelas.setText(classes.getName());
                binding.deskripsi.setText(classes.getDescription());

                viewModel.fetchUser(classes.getTeacher_id());

                viewModel.getUser().observe(getViewLifecycleOwner(), teacher ->
                        binding.namaGuru.setText(teacher.getName())
                );
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}