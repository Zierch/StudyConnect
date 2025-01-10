package com.example.studyconnect.ui.class_ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studyconnect.R;
import com.example.studyconnect.databinding.FragmentClassBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ClassFragment extends Fragment {

    private FragmentClassBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentClassBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assert getArguments() != null;
        int classId = getArguments().getInt("classId");

        binding.bottomNavFragmentClass.setOnItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_forum) {
                selectedFragment = new ForumFragment();
            } else if (itemId == R.id.navigation_class_assignment) {
                selectedFragment = new ClassAssignmentFragment();
            } else if (itemId == R.id.navigation_student_list) {
                selectedFragment = new StudentListFragment();
            }

            if (selectedFragment != null) {
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_fragment_class, selectedFragment)
                        .commit();
            }

            return true;
        });

        if (savedInstanceState == null) {
            binding.bottomNavFragmentClass.setSelectedItemId(R.id.navigation_forum);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}