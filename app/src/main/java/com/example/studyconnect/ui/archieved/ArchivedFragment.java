package com.example.studyconnect.ui.archieved;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.studyconnect.R;
import com.example.studyconnect.databinding.FragmentArsipBinding;
import com.example.studyconnect.ui.adapter.ArchiveAdapter;
import com.example.studyconnect.ui.class_ui.ClassFragment;
import com.example.studyconnect.ui.home.HomeViewModel;
import com.example.studyconnect.utils.Helper;
import com.example.studyconnect.utils.SharedPreferencesManager;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ArchivedFragment extends Fragment {
    private FragmentArsipBinding binding;
    private HomeViewModel viewModel;
    private ArchiveAdapter adapter;

    @Inject
    SharedPreferencesManager sharedPreferencesManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentArsipBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        initObserver();
        initView();

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

        binding.arsipEmptyList.tvTitleEmptyList.setText(R.string.text_title_empty_arsip);
        binding.arsipEmptyList.tvDescEmptyList.setText(R.string.text_desc_empty_arsip);

        adapter = new ArchiveAdapter(classItem -> {
            Bundle bundle = new Bundle();
            bundle.putInt("classId", classItem.getId());
            bundle.putString("className", classItem.getName());
            bundle.putBoolean("isArchived", true);

            ClassFragment classFragment = new ClassFragment();
            classFragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_host, classFragment)
                    .addToBackStack("ClassFragment")
                    .commit();
        }, viewModel);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    private void initObserver() {
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading ->
                Helper.showLoading(binding.progressBar.progressBar, isLoading)
        );

        viewModel.getErrorMsg().observe(getViewLifecycleOwner(), errorMsg ->
                Toast.makeText(requireActivity(), errorMsg, Toast.LENGTH_SHORT).show()
        );

        viewModel.getArchivedClasses().observe(getViewLifecycleOwner(), classes -> {
            if (classes != null && !classes.isEmpty()) {
                binding.arsipEmptyList.emptyList.setVisibility(View.GONE);
                adapter.setClassList(classes);
                binding.recyclerView.setVisibility(View.VISIBLE);
            } else {
                binding.arsipEmptyList.emptyList.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        viewModel.fetchArchivedClasses();
        adapter.notifyDataSetChanged();
    }
}
