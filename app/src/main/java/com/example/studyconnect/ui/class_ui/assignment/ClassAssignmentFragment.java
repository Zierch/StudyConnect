package com.example.studyconnect.ui.class_ui.assignment;

import android.content.Intent;
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

import com.example.studyconnect.databinding.AddTugasDialogBinding;
import com.example.studyconnect.databinding.FragmentClassAssignmentBinding;
import com.example.studyconnect.ui.adapter.TugasAdapter;
import com.example.studyconnect.utils.Helper;
import com.example.studyconnect.utils.SharedPreferencesManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ClassAssignmentFragment extends Fragment {
    public static final String IS_ARCHIVED = "isArchived";
    private static final String ARG_CLASS_ID = "classId";

    @Inject
    SharedPreferencesManager sharedPreferencesManager;

    private FragmentClassAssignmentBinding binding;
    private ClassAssignmentViewModel viewModel;
    private TugasAdapter tugasAdapter;
    private int userId;
    private int classId;
    private boolean isArchived;

    public static ClassAssignmentFragment newInstance(int classId, boolean isArchived) {
        ClassAssignmentFragment fragment = new ClassAssignmentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CLASS_ID, classId);
        args.putBoolean(IS_ARCHIVED, isArchived);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isArchived = getArguments().getBoolean("isArchived");
            classId = getArguments().getInt(ARG_CLASS_ID);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentClassAssignmentBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ClassAssignmentViewModel.class);

//        teacherId = sharedPreferencesManager.getData("classTeacherId", 0);
        userId = sharedPreferencesManager.getData("userId", 0);
        String userRole = sharedPreferencesManager.getData("role", null);

        initObserver();
        initView();

        if (userRole.equals("student")) {
            binding.fabAddTugas.setVisibility(View.GONE);
        }
        binding.fabAddTugas.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
            AddTugasDialogBinding bottomBinding = AddTugasDialogBinding.inflate(getLayoutInflater());
            bottomSheetDialog.setContentView(bottomBinding.getRoot());

            // Menyembunyikan tombol sesuai dengan kondisi teacherId
//            bottomBinding.createClass.setVisibility(teacherId == 0 ? View.GONE : View.VISIBLE);
//            bottomBinding.joinClass.setVisibility(teacherId != 0 ? View.GONE : View.VISIBLE);

            bottomBinding.btnAddTugas.setOnClickListener(v1 -> {
                bottomSheetDialog.dismiss();

                Intent intent = new Intent(requireContext(), FormTugasActivity.class);
                intent.putExtra("classId", classId);
                startActivity(intent);
            });

            bottomBinding.btnAddPertanyaan.setOnClickListener(v1 -> {
//                Intent intent = new Intent(requireContext(), FormClassActivity.class);
//                intent.putExtra("isUpdate", false);
//                startActivity(intent);
                bottomSheetDialog.dismiss();
            });

            bottomBinding.btnAddMateri.setOnClickListener(v1 -> {
//                Intent intent = new Intent(requireContext(), );
//                startActivity(intent);
                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.show();
        });

        return binding.getRoot();
    }

    private void initView() {
        if (isArchived) {
            binding.fabAddTugas.setVisibility(View.GONE);
        }

        tugasAdapter = new TugasAdapter(classAssignmentItem -> {
            Bundle bundle = new Bundle();
            bundle.putInt("AssignmentId", classAssignmentItem.getId());

//            ClassFragment classFragment = new ClassFragment();
//            classFragment.setArguments(bundle);

//            getParentFragmentManager().beginTransaction()
//                    .replace(R.id.frame_layout, classFragment)
//                    .addToBackStack(null)
//                    .commit();
        }, viewModel);

        binding.rvListTugasKelas.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvListTugasKelas.setAdapter(tugasAdapter);
    }

    private void initObserver() {
        viewModel.fetchClassAssignment(classId);
        viewModel.fetchUser(userId);

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading ->
                Helper.showLoading(binding.progressBar.progressBar, isLoading)
        );

        viewModel.getErrorMsg().observe(getViewLifecycleOwner(), errorMsg ->
                Toast.makeText(requireActivity(), errorMsg, Toast.LENGTH_SHORT).show()
        );

        viewModel.getClassAssignment().observe(getViewLifecycleOwner(), classAssignment -> {
            if (classAssignment != null && !classAssignment.isEmpty()) {
                binding.rvListTugasKelas.setVisibility(View.VISIBLE);
                binding.includeTugasEmptyList.emptyList.setVisibility(View.GONE);
                tugasAdapter.setClassAssignmentList(classAssignment);
            } else {
                binding.rvListTugasKelas.setVisibility(View.GONE);
                binding.includeTugasEmptyList.emptyList.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                tugasAdapter.setUser(user);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.fetchClassAssignment(classId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}