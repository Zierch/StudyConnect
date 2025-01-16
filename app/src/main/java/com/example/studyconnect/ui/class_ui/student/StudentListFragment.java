package com.example.studyconnect.ui.class_ui.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.studyconnect.databinding.FragmentStudentListBinding;
import com.example.studyconnect.ui.adapter.StudentAdapter;
import com.example.studyconnect.utils.Helper;
import com.example.studyconnect.utils.SharedPreferencesManager;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class StudentListFragment extends Fragment {

    @Inject
    SharedPreferencesManager sharedPreferencesManager;

    private static final String ARG_CLASS_ID = "classId";
    private FragmentStudentListBinding binding;
    private StudentListViewModel viewModel;
    private StudentAdapter studentAdapter;
    private int classId;
    private int teacherId;

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

        teacherId = sharedPreferencesManager.getData("teacherId", 0);
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
        studentAdapter = new StudentAdapter(studentItem ->
                Toast.makeText(
                        requireContext(),
                        "Selected: " + studentItem.getName(),
                        Toast.LENGTH_SHORT
                ).show(), viewModel);

        binding.rvSiswaList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvSiswaList.setAdapter(studentAdapter);
    }

    private void initObserver() {
        viewModel.fetchUser(teacherId);
        viewModel.fetchStudents(classId);

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading ->
                Helper.showLoading(binding.progressBarIncl.progressBar, isLoading)
        );

        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                studentAdapter.setUser(user);
                binding.includeGuruList.tvNamaGuru.setText(user.getName());
            }
        });

        viewModel.getStudents().observe(getViewLifecycleOwner(), students -> {
            if (students != null) {
                studentAdapter.setStudentList(students);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}