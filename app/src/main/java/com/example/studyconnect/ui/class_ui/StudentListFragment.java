package com.example.studyconnect.ui.class_ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyconnect.databinding.FragmentStudentListBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class StudentListFragment extends Fragment {

    private static final String ARG_CLASS_ID = "classId";
    private FragmentStudentListBinding binding;
    private ClassViewModel classViewModel;
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
            Log.d("StudentListFragment", "Received classId: " + classId);
        } else {
            Log.e("StudentListFragment", "Arguments are null");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStudentListBinding.inflate(inflater, container, false);
        classViewModel = new ViewModelProvider(this).get(ClassViewModel.class);
        classViewModel.fetchClass(classId);

        initObserver();
        initView();

        return binding.getRoot();
    }

    private void initView() {
    }

    private void initObserver() {
        classViewModel.getClassResult().observe(getViewLifecycleOwner(), classResponse -> {
            if (classResponse != null) {
                int teacherId = classResponse.getTeacher_id();

                classViewModel.getTeacher(teacherId).observe(getViewLifecycleOwner(), teacher -> {
                    if (teacher != null) {
                        binding.includeGuruList.tvNamaGuru.setText(teacher.getName());
                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}