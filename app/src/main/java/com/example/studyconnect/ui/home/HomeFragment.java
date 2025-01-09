package com.example.studyconnect.ui.home;

import android.os.Bundle;
import android.os.Parcelable;
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
import com.example.studyconnect.databinding.FragmentHomeBinding;
import com.example.studyconnect.ui.adapter.HomeAdapter;
import com.example.studyconnect.ui.class_ui.ClassFragment;
import com.example.studyconnect.utils.SharedPreferencesManager;

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

        teacherId = sharedPreferencesManager.getData("teacherId", 0);

        initView();
        initObserver();

        return binding.getRoot();
    }

    private void initView() {
        homeAdapter = new HomeAdapter(classItem -> {
            Bundle bundle = new Bundle();
            bundle.putInt("classId", classItem.getId());

            ClassFragment classFragment = new ClassFragment();
            classFragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, classFragment)
                    .addToBackStack(null)
                    .commit();
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(homeAdapter);
    }

    private void initObserver() {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getClasses().observe(getViewLifecycleOwner(), classes -> {
            if (classes != null) {
                homeAdapter.setClassList(classes); // Set data ke adapter
            }
        });

        homeViewModel.getUser(teacherId).observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                homeAdapter.setUser(user);
            }
        });
    }
}
