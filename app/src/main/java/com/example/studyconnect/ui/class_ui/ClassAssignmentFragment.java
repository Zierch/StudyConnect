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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.studyconnect.databinding.AddTugasDialogBinding;
import com.example.studyconnect.databinding.FragmentClassAssignmentBinding;
import com.example.studyconnect.ui.adapter.TugasAdapter;
import com.example.studyconnect.utils.SharedPreferencesManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ClassAssignmentFragment extends Fragment {

    private FragmentClassAssignmentBinding binding;
    private ClassAssignmentViewModel classAssignmentViewModel;
    private TugasAdapter tugasAdapter;

    @Inject
    SharedPreferencesManager sharedPreferencesManager;
    private int teacherId;

    private static final String ARG_CLASS_ID = "classId";
    private int classId;
    private boolean isArchived;

    public static ClassAssignmentFragment newInstance(int classId, boolean isArchived) {
        ClassAssignmentFragment fragment = new ClassAssignmentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CLASS_ID, classId);
        args.putBoolean("isArchived", isArchived);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isArchived = getArguments().getBoolean("isArchived");
            classId = getArguments().getInt(ARG_CLASS_ID);
            Log.d("ClassAssignmentFragment", "Received classId: " + classId);
        } else {
            Log.e("ClassAssignmentFragment", "Arguments are null");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentClassAssignmentBinding.inflate(inflater, container, false);

        classAssignmentViewModel = new ViewModelProvider(this).get(ClassAssignmentViewModel.class);
//        classAssignmentViewModel.fetchClassAssignment(classId);
        teacherId = sharedPreferencesManager.getData("teacherId", 0);

        initView();
        initObserver();

        binding.fabAddTugas.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
            AddTugasDialogBinding bottomBinding = AddTugasDialogBinding.inflate(getLayoutInflater());
            bottomSheetDialog.setContentView(bottomBinding.getRoot());

            // Menyembunyikan tombol sesuai dengan kondisi teacherId
//            bottomBinding.createClass.setVisibility(teacherId == 0 ? View.GONE : View.VISIBLE);
//            bottomBinding.joinClass.setVisibility(teacherId != 0 ? View.GONE : View.VISIBLE);

            bottomBinding.btnAddTugas.setOnClickListener(v1 -> {
//                Intent intent = new Intent(requireContext(), FormClassActivity.class);
//                intent.putExtra("isUpdate", false);
//                startActivity(intent);
                bottomSheetDialog.dismiss();
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
        if (isArchived){
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
        }, classAssignmentViewModel);

        binding.rvListTugasKelas.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvListTugasKelas.setAdapter(tugasAdapter);
    }

    private void initObserver() {
        classAssignmentViewModel = new ViewModelProvider(this).get(ClassAssignmentViewModel.class);
        classAssignmentViewModel.getClassAssignment(classId).observe(getViewLifecycleOwner(), classAssignment -> {
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