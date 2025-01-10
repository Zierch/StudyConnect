package com.example.studyconnect.ui.home;

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
import com.example.studyconnect.R;
import com.example.studyconnect.databinding.BottomHomeDialogBinding;
import com.example.studyconnect.databinding.FragmentHomeBinding;
import com.example.studyconnect.ui.adapter.HomeAdapter;
import com.example.studyconnect.ui.class_ui.ClassFragment;
import com.example.studyconnect.ui.class_ui.ClassViewModel;
import com.example.studyconnect.ui.class_ui.FormClassActivity;
import com.example.studyconnect.utils.SharedPreferencesManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private HomeAdapter homeAdapter;

    @Inject
    SharedPreferencesManager sharedPreferencesManager;
    private int teacherId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        teacherId = sharedPreferencesManager.getData("teacherId", 0);

        initView();
        initObserver();

        return binding.getRoot();
    }

    private void initView() {
        if (teacherId != 0) {
            binding.joinClass.setVisibility(View.GONE);
        } else {
            binding.createClass.setVisibility(View.GONE);
        }

        binding.add.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
            BottomHomeDialogBinding bottomBinding = BottomHomeDialogBinding.inflate(getLayoutInflater());
            bottomSheetDialog.setContentView(bottomBinding.getRoot());

            // Menyembunyikan tombol sesuai dengan kondisi teacherId
            bottomBinding.createClass.setVisibility(teacherId == 0 ? View.GONE : View.VISIBLE);
            bottomBinding.joinClass.setVisibility(teacherId != 0 ? View.GONE : View.VISIBLE);

            bottomBinding.createClass.setOnClickListener(v1 -> {
                Intent intent = new Intent(requireContext(), FormClassActivity.class);
                intent.putExtra("isUpdate", false);
                startActivity(intent);
                bottomSheetDialog.dismiss();
            });

            bottomBinding.joinClass.setOnClickListener(v1 -> {
//                Intent intent = new Intent(requireContext(), );
//                startActivity(intent);
                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.show();
        });

        homeAdapter = new HomeAdapter(classItem -> {
            Bundle bundle = new Bundle();
            bundle.putInt("classId", classItem.getId());

            ClassFragment classFragment = new ClassFragment();
            classFragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, classFragment)
                    .addToBackStack(null)
                    .commit();
        }, homeViewModel, sharedPreferencesManager);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(homeAdapter);
    }

    private void initObserver() {
        homeViewModel.getClasses().observe(getViewLifecycleOwner(), classes -> {
            if (classes != null) {
                homeAdapter.setClassList(classes); // Set data ke adapter
                binding.linearButton.setVisibility(View.GONE);
            } else {
                binding.linearButton.setVisibility(View.VISIBLE);
            }
        });

        homeViewModel.getUser(teacherId).observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                homeAdapter.setUser(user);
            }
        });
    }

//    recy ga ke refresh kalo ada perubahan
    @Override
    public void onResume() {
        super.onResume();
        homeViewModel.fetchClasses();
        homeAdapter.notifyDataSetChanged();
    }
}
