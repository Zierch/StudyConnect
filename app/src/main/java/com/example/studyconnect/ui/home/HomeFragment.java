package com.example.studyconnect.ui.home;

import android.annotation.SuppressLint;
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

import com.example.studyconnect.R;
import com.example.studyconnect.databinding.BottomHomeDialogBinding;
import com.example.studyconnect.databinding.FragmentHomeBinding;
import com.example.studyconnect.ui.adapter.HomeAdapter;
import com.example.studyconnect.ui.class_ui.ClassFragment;
import com.example.studyconnect.ui.class_ui.FormClassActivity;
import com.example.studyconnect.ui.class_ui.JoinClassActivity;
import com.example.studyconnect.utils.Helper;
import com.example.studyconnect.utils.SharedPreferencesManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {
    @Inject
    SharedPreferencesManager sharedPreferencesManager;
    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private HomeAdapter homeAdapter;
    private String userRole;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        userRole = sharedPreferencesManager.getData("role", null);

        initView();
        initObserver();

        return binding.getRoot();
    }

    private void initView() {
        if (userRole.equals("teacher")) {
            binding.joinClass.setVisibility(View.GONE);
        } else {
            binding.createClass.setVisibility(View.GONE);
        }

        binding.add.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
            BottomHomeDialogBinding bottomBinding = BottomHomeDialogBinding.inflate(getLayoutInflater());
            bottomSheetDialog.setContentView(bottomBinding.getRoot());

            // Menyembunyikan tombol sesuai dengan role
            if (userRole.equals("teacher")) {
                bottomBinding.joinClass.setVisibility(View.GONE);
            } else {
                bottomBinding.createClass.setVisibility(View.GONE);
            }

            bottomBinding.createClass.setOnClickListener(v1 -> {
                Intent intent = new Intent(requireContext(), FormClassActivity.class);
                intent.putExtra("isUpdate", false);
                startActivity(intent);
                bottomSheetDialog.dismiss();
            });

            bottomBinding.joinClass.setOnClickListener(v1 -> {
                Helper.startActivity(JoinClassActivity.class, requireContext());
                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.show();
        });

        homeAdapter = new HomeAdapter(classItem -> {
            sharedPreferencesManager.saveData("classTeacherId", classItem.getTeacher_id());
            Bundle bundle = new Bundle();
            bundle.putInt("classId", classItem.getId());
            bundle.putString("className", classItem.getName());
            bundle.putBoolean("isArchived", false);

            ClassFragment classFragment = new ClassFragment();
            classFragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_host, classFragment)
                    .addToBackStack("ClassFragment")
                    .commit();
        }, homeViewModel, sharedPreferencesManager, getViewLifecycleOwner());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(homeAdapter);
    }

    private void initObserver() {
        homeViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading ->
                Helper.showLoading(binding.progressBarIncl.progressBar, isLoading)
        );

        homeViewModel.getErrorMsg().observe(getViewLifecycleOwner(), errorMsg ->
                Toast.makeText(requireActivity(), errorMsg, Toast.LENGTH_SHORT).show()
        );

        homeViewModel.getClasses().observe(getViewLifecycleOwner(), classes -> {
            if (classes != null) {
                homeAdapter.setClassList(classes);
                binding.linearButton.setVisibility(View.GONE);
            } else {
                binding.linearButton.setVisibility(View.VISIBLE);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        homeViewModel.fetchActiveClasses();
        homeAdapter.notifyDataSetChanged();
    }
}
