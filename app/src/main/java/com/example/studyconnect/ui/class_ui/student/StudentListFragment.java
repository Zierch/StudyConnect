package com.example.studyconnect.ui.class_ui.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyconnect.databinding.FragmentStudentListBinding;
import com.example.studyconnect.utils.Helper;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class StudentListFragment extends Fragment {

    private static final String ARG_CLASS_ID = "classId";
    private FragmentStudentListBinding binding;
    private StudentListViewModel viewModel;
    private int classId;

    public static StudentListFragment newInstance(int classId) {
        StudentListFragment fragment = new StudentListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CLASS_ID, classId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            classId = getArguments().getInt(ARG_CLASS_ID);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStudentListBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(StudentListViewModel.class);

        initObserver();
        initView();

        return binding.getRoot();
    }

    private void initView() {
    }

    private void initObserver() {
        viewModel.fetchUser(classId);

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading ->
                Helper.showLoading(binding.progressBarIncl.progressBar, isLoading)
        );

        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.includeGuruList.tvNamaGuru.setText(user.getName());
            }
        });

//        classViewModel.getClassResult().observe(getViewLifecycleOwner(), classResponse -> {
//            if (classResponse != null) {
//                int teacherId = classResponse.getTeacher_id();
//
//                classViewModel.getTeacher(teacherId).observe(getViewLifecycleOwner(), teacher -> {
//                    if (teacher != null) {
//                        binding.includeGuruList.tvNamaGuru.setText(teacher.getName());
//                    }
//                });
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}