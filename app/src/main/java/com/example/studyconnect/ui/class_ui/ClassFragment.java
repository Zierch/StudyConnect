package com.example.studyconnect.ui.class_ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyconnect.R;
import com.example.studyconnect.databinding.FragmentClassBinding;
import com.example.studyconnect.ui.class_ui.assignment.ClassAssignmentFragment;
import com.example.studyconnect.ui.class_ui.forum.ForumFragment;
import com.example.studyconnect.ui.class_ui.student.StudentListFragment;
import com.example.studyconnect.ui.home.Drawer;
import com.example.studyconnect.utils.Helper;
import com.example.studyconnect.utils.SharedPreferencesManager;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ClassFragment extends Fragment {

    @Inject
    SharedPreferencesManager sharedPreferencesManager;
    private FragmentClassBinding binding;
    private ClassViewModel viewModel;
    private int selectedItemId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentClassBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ClassViewModel.class);

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading ->
                Helper.showLoading(binding.progressBarIncl.progressBar, isLoading)
        );

        viewModel.getIsSuccess().observe(getViewLifecycleOwner(), isSuccess ->
                getParentFragmentManager().popBackStack()
        );

        viewModel.getErrorMsg().observe(getViewLifecycleOwner(), errorMsg -> {
            Toast.makeText(requireActivity(), errorMsg, Toast.LENGTH_SHORT).show();
            binding.restore.setClickable(true);
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assert getArguments() != null;
        String className = getArguments().getString("className");
        int classsId = getArguments().getInt("classId");
        String userRole = sharedPreferencesManager.getData("role", null);

        // Akses Toolbar dari Activity
        if (requireActivity() instanceof Drawer) {
            Drawer activity = (Drawer) requireActivity();
            Toolbar toolbar = activity.getToolbar();
            toolbar.setTitle(className);
            ImageView imageView = toolbar.findViewById(R.id.optionalItem);
            imageView.setVisibility(View.VISIBLE);
            if (userRole.equals("student")) {
                imageView.setImageResource(R.drawable.ic_info_outline);
                imageView.setOnClickListener(v -> {
                    sharedPreferencesManager.saveData("infoClassId", classsId);
                    Intent intent = new Intent(requireContext(), InfoClassActivity.class);
                    startActivity(intent);
                });
            } else {
                imageView.setOnClickListener(v -> {
                    sharedPreferencesManager.saveData("classId", classsId);
                    Intent intent = new Intent(requireContext(), FormClassActivity.class);
                    intent.putExtra("isUpdate", true);
                    startActivity(intent);
                });
            }

        }

        assert getArguments() != null;
        int classId = getArguments().getInt("classId");

        boolean isArchived = getArguments().getBoolean("isArchived");
        if (isArchived) {
            binding.restore.setOnClickListener(v -> {
                Log.d("ClassFragment", "Restore button clicked");
                binding.restore.setClickable(false);
                viewModel.restoreClass(classId);
            });
        } else {
            binding.archivedCard.setVisibility(View.GONE);
        }

        binding.bottomNavFragmentClass.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            // Jika item yang dipilih sama dengan item yang sedang aktif, kembalikan true
            if (itemId == selectedItemId) {
                return true;
            }

            if (itemId == R.id.navigation_forum) {
                selectedFragment = ForumFragment.newInstance(classId, isArchived);
            } else if (itemId == R.id.navigation_class_assignment) {
                selectedFragment = ClassAssignmentFragment.newInstance(classId, isArchived);
            } else if (itemId == R.id.navigation_student_list) {
                selectedFragment = StudentListFragment.newInstance(classId);
            }

            if (selectedFragment != null) {
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_fragment_class, selectedFragment)
                        .commit();
                selectedItemId = itemId;
            }

            return true;
        });

        if (savedInstanceState == null) {
            binding.bottomNavFragmentClass.setSelectedItemId(R.id.navigation_forum);
            selectedItemId = R.id.navigation_forum;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (requireActivity() instanceof Drawer) {
            Drawer activity = (Drawer) requireActivity();
            ImageView imageView = activity.getToolbar().findViewById(R.id.optionalItem);
            imageView.setImageResource(R.drawable.ic_setting);
            imageView.setVisibility(View.GONE);
        }
        binding = null;
    }
}