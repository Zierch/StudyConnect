package com.example.studyconnect.ui.archieved;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.studyconnect.R;
import com.example.studyconnect.databinding.FragmentArsipBinding;
import com.example.studyconnect.ui.adapter.HomeAdapter;
import com.example.studyconnect.ui.class_ui.ClassFragment;
import com.example.studyconnect.ui.home.HomeViewModel;
import com.example.studyconnect.utils.SharedPreferencesManager;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ArchivedFragment extends Fragment {
    private FragmentArsipBinding binding;
    private HomeViewModel homeViewModel;
    private HomeAdapter homeAdapter;

    @Inject
    SharedPreferencesManager sharedPreferencesManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentArsipBinding.inflate(inflater, container, false);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.fetchArchivedClasses();

        initView();
        initObserver();

        return binding.getRoot();
    }

    private void initView() {
        // Ubah judul ActionBar
        if (requireActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) requireActivity();
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setTitle("Kelas yang diarsipkan");
            }
        }

        homeAdapter = new HomeAdapter(classItem -> {
            Bundle bundle = new Bundle();
            bundle.putInt("classId", classItem.getId());
            bundle.putString("className", classItem.getName());

            ClassFragment classFragment = new ClassFragment();
            classFragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_host, classFragment)
                    .addToBackStack(null)
                    .commit();
        }, homeViewModel, sharedPreferencesManager);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(homeAdapter);
    }

    private void initObserver() {
        homeViewModel.getArchivedClasses().observe(getViewLifecycleOwner(), classes -> {
            if (classes != null) {
                homeAdapter.setClassList(classes); // Set data ke adapter
            } else {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        homeViewModel.fetchArchivedClasses();
        homeAdapter.notifyDataSetChanged();
    }
}
