package com.example.studyconnect.ui.class_ui.forum;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.example.studyconnect.R;
import com.example.studyconnect.databinding.FragmentForumBinding;
import com.example.studyconnect.ui.adapter.ForumAdapter;
import com.example.studyconnect.utils.Helper;

public class ForumFragment extends Fragment {

    private FragmentForumBinding binding;
    private ForumViewModel viewModel;
    private ForumAdapter adapter;
    private int classId;

    public static ForumFragment newInstance(int classId) {
        ForumFragment fragment = new ForumFragment();
        Bundle args = new Bundle();
        args.putInt("classId", classId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            classId = getArguments().getInt("classId");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentForumBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ForumViewModel.class);

        adapter = new ForumAdapter(announcementItem -> {

        }, viewModel);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        initObserver();
        initView();

        return binding.getRoot();
    }

    private void initView() {
        binding.cardAdd.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), FormForumActivity.class);
            intent.putExtra("isUpdate", false);
            intent.putExtra("AnnouncementClassId", classId);
            startActivity(intent);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initObserver() {
        viewModel.fetchClass(classId);

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            Log.d("Loading", String.valueOf(isLoading));
            Helper.showLoading(binding.progressBarIncl.progressBar, isLoading);
        });

        viewModel.getClassData().observe(getViewLifecycleOwner(), classes -> {
            if (classes != null) {
                binding.cardLinear.setBackgroundResource(R.drawable.gradient_light_blue_background);
                binding.namaKelas.setText(classes.getName());
                binding.deskripsi.setText(classes.getDescription());

                viewModel.getUser(classes.getTeacher_id()).observe(getViewLifecycleOwner(), teacher -> {
                            binding.namaGuru.setText(teacher.getName());
                            adapter.setUser(teacher);
                        }
                );
            }
        });

        viewModel.getAnnouncements(classId).observe(getViewLifecycleOwner(), announcementResponses -> {
            adapter.setAnnouncementList(announcementResponses);
            adapter.notifyDataSetChanged();
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.fetchAnnouncement(classId);
        adapter.notifyDataSetChanged();
    }
}