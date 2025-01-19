package com.example.studyconnect.ui.class_ui.assignment.tab_view_guru;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.studyconnect.data.model.StudentModel;
import com.example.studyconnect.databinding.FragmentTugasSiswaBinding;
import com.example.studyconnect.ui.adapter.SubStudentAdapter;
import com.example.studyconnect.ui.class_ui.assignment.SubmissionViewModel;
import com.example.studyconnect.ui.class_ui.assignment.ViewStudentAssignmentActivity;
import com.example.studyconnect.ui.class_ui.student.StudentListViewModel;
import com.example.studyconnect.utils.Helper;
import com.example.studyconnect.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TugasSiswaFragment extends Fragment {

    private static final String ARG_ASSIGNMENT_ID = "assignmentId";
    @Inject
    SharedPreferencesManager sharedPreferencesManager;
    private FragmentTugasSiswaBinding binding;
    private SubmissionViewModel viewModel;
    private StudentListViewModel studentListViewModel;
    private SubStudentAdapter studentAdapter;
    private List<StudentModel> studentList = new ArrayList<>();
    private StudentModel student = new StudentModel();
    private int assignmentId;

    public static TugasSiswaFragment newInstance(int assignmentId) {
        TugasSiswaFragment fragment = new TugasSiswaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ASSIGNMENT_ID, assignmentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            assignmentId = getArguments().getInt(ARG_ASSIGNMENT_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTugasSiswaBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(SubmissionViewModel.class);
        studentListViewModel = new ViewModelProvider(this).get(StudentListViewModel.class);

        initObserver();
        initView();

        return binding.getRoot();
    }

    private void initView() {
        studentAdapter = new SubStudentAdapter(studentItem -> {
            Intent intent = new Intent(getContext(), FormScoringActivity.class);
            intent.putExtra("submissionId", studentItem.getSubmissionId());
            intent.putExtra("studentName", studentItem.getName());
            startActivity(intent);
        });

        binding.rvSiswaList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvSiswaList.setAdapter(studentAdapter);
    }

    private void initObserver() {
        viewModel.getSubmissions(assignmentId).observe(getViewLifecycleOwner(), items -> {
            items.forEach(item -> {
                student.setSubmissionId(item.getId());
                studentListViewModel.fetchUser(item.getStudent_id());
                studentListViewModel.getUser().observe(getViewLifecycleOwner(), registerResponse -> {
                    student.setName(registerResponse.getName());

                    studentList.add(student);
                    studentAdapter.setSubStudentList(studentList);
                });
            });
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading ->
                Helper.showLoading(binding.progressBarIncl.progressBar, isLoading)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}