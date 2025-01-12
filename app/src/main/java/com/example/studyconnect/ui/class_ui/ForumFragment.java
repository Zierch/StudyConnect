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

import com.example.studyconnect.databinding.FragmentForumBinding;

public class ForumFragment extends Fragment {

    private FragmentForumBinding binding;
    private ClassViewModel viewModel;
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
            Log.d("ClassAssignmentFragment", "Received classId: " + classId);
        } else {
            Log.e("ClassAssignmentFragment", "Arguments are null");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentForumBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ClassViewModel.class);
        viewModel.fetchClass(classId);

        initObserver();
        initView();
        return binding.getRoot();
    }

    private void initView(){

    }

    private void initObserver(){
//        viewModel.getClassResult().observe(getViewLifecycleOwner(), classes -> {
//            if (classes != null) {
//                binding.namaKelas.setText(classes.getName());
//                binding.deskripsi.setText(classes.getDescription());
//
//                viewModel.getTeacher(classes.getTeacher_id()).observe(getViewLifecycleOwner(), teacher -> {
//                    binding.namaGuru.setText(teacher.getName());
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