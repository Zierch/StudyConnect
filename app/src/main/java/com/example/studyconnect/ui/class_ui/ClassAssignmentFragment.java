package com.example.studyconnect.ui.class_ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.studyconnect.R;
import com.example.studyconnect.data.Api;
import com.example.studyconnect.data.ApiClient;
import com.example.studyconnect.data.AssignmentItem;
import com.example.studyconnect.data.Session;
import com.example.studyconnect.databinding.FragmentClassAssignmentBinding;

import java.util.List;

import retrofit2.Call;

/** Tab Tugas Kelas: daftar tugas dari API, atau keadaan kosong. */
public class ClassAssignmentFragment extends Fragment {

    private FragmentClassAssignmentBinding binding;
    private AssignmentAdapter adapter;
    private Call<List<AssignmentItem>> call;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentClassAssignmentBinding.inflate(inflater, container, false);
        int classId = ClassActivity.classIdOf(requireActivity());

        adapter = new AssignmentAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);

        loadAssignments(classId);
        return binding.getRoot();
    }

    private void loadAssignments(int classId) {
        if (classId <= 0) {
            showEmpty(true);
            return;
        }
        call = ApiClient.service().getAssignments(Session.token(requireContext()), classId);
        Api.enqueue(call, this::showAssignments,
                message -> {
                    if (binding != null) {
                        Api.showError(binding.getRoot(), message);
                    }
                });
    }

    private void showAssignments(List<AssignmentItem> items) {
        if (binding == null) {
            return;
        }
        boolean empty = items == null || items.isEmpty();
        adapter.submit(items);
        showEmpty(empty);
    }

    private void showEmpty(boolean empty) {
        binding.includeTugasEmptyList.getRoot().setVisibility(empty ? View.VISIBLE : View.GONE);
        binding.recyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (call != null) {
            call.cancel();
            call = null;
        }
        binding = null;
    }
}
