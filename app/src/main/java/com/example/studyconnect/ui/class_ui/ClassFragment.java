package com.example.studyconnect.ui.class_ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyconnect.R;
import com.example.studyconnect.databinding.FragmentClassBinding;
import com.example.studyconnect.ui.home.Drawer;
import com.example.studyconnect.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ClassFragment extends Fragment {

    private FragmentClassBinding binding;
    private ClassViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentClassBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ClassViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String className = getArguments().getString("className");
        // Ubah judul ActionBar
        if (requireActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) requireActivity();
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setTitle(className);
            }
        }

        assert getArguments() != null;
        int classId = getArguments().getInt("classId");

        boolean isArchived = getArguments().getBoolean("isArchived");
        if (isArchived){
            binding.restore.setOnClickListener(v -> {
                Log.d("ClassFragment", "Restore button clicked");
                binding.restore.setClickable(false);
                viewModel.restoreClass(classId);
            });
        } else {
            binding.archivedCard.setVisibility(View.GONE);
        }

        binding.bottomNavFragmentClass.setOnItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_forum) {
                selectedFragment = ForumFragment.newInstance(classId);
            } else if (itemId == R.id.navigation_class_assignment) {
                selectedFragment = ClassAssignmentFragment.newInstance(classId, isArchived);
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

        viewModel.getRestoreClassResult().observe(getViewLifecycleOwner(), isSuccess -> {
            if (isSuccess) {
                getParentFragmentManager().popBackStack();
            } else {
                binding.restore.setClickable(true);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}