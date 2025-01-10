package com.example.studyconnect.ui.class_ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.studyconnect.databinding.FragmentClassAssignmentBinding;
import com.example.studyconnect.ui.adapter.TugasAdapter;
import com.example.studyconnect.utils.SharedPreferencesManager;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ClassAssignmentFragment extends Fragment {

    private FragmentClassAssignmentBinding binding;
    private TugasAdapter tugasAdapter;

    @Inject
    SharedPreferencesManager sharedPreferencesManager;
    private int teacherId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentClassAssignmentBinding.inflate(inflater, container, false);

//        int classId = getArguments().getInt("classId");
        int classId = 1;

        Log.d("ClassAssignmentFragment", "classId: " + classId);
        teacherId = sharedPreferencesManager.getData("teacherId", 0);

        initView();
        initObserver();

        return binding.getRoot();
    }

    private void initView() {
        tugasAdapter = new TugasAdapter(classAssignmentItem -> {
            Bundle bundle = new Bundle();
            bundle.putInt("AssignmentId", classAssignmentItem.getId());
//            ClassFragment classFragment = new ClassFragment();
//            classFragment.setArguments(bundle);

//            getParentFragmentManager().beginTransaction()
//                    .replace(R.id.frame_layout, classFragment)
//                    .addToBackStack(null)
//                    .commit();
        });

        binding.rvListTugasKelas.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvListTugasKelas.setAdapter(tugasAdapter);
    }

    private void initObserver() {
        ClassAssignmentViewModel classAssignmentViewModel = new ViewModelProvider(this).get(ClassAssignmentViewModel.class);
        classAssignmentViewModel.getClassAssignment(1).observe(getViewLifecycleOwner(), classAssignment -> {
            if (classAssignment != null) {
                tugasAdapter.setClassAssignmentList(classAssignment); // Set data ke adapter
            }
        });

        classAssignmentViewModel.getUser(teacherId).observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                tugasAdapter.setUser(user);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}