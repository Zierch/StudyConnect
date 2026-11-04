package com.example.studyconnect.ui.class_ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.studyconnect.R;
import com.example.studyconnect.data.AnnouncementItem;
import com.example.studyconnect.data.Api;
import com.example.studyconnect.data.ApiClient;
import com.example.studyconnect.data.Session;
import com.example.studyconnect.databinding.FragmentForumBinding;

import java.util.List;

import retrofit2.Call;

/** Tab Forum: identitas kelas + daftar pengumuman dari API. */
public class ForumFragment extends Fragment {

    private FragmentForumBinding binding;
    private AnnouncementAdapter adapter;
    private Call<List<AnnouncementItem>> call;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentForumBinding.inflate(inflater, container, false);
        int classId = ClassActivity.classIdOf(requireActivity());

        binding.namaKelas.setText(ClassActivity.classNameOf(requireActivity()));
        binding.namaRuang.setText(getString(R.string.text_forum));
        binding.namaGuru.setText(Session.name(requireContext()));

        adapter = new AnnouncementAdapter();
        adapter.setAuthorName(Session.name(requireContext()));
        binding.rvForum.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvForum.setAdapter(adapter);

        loadAnnouncements(classId);
        return binding.getRoot();
    }

    private void loadAnnouncements(int classId) {
        if (classId <= 0) {
            binding.includeForumEmpty.getRoot().setVisibility(View.VISIBLE);
            return;
        }
        call = ApiClient.service().getAnnouncements(Session.token(requireContext()), classId);
        Api.enqueue(call, this::showAnnouncements,
                message -> {
                    if (binding != null) {
                        Api.showError(binding.getRoot(), message);
                    }
                });
    }

    private void showAnnouncements(List<AnnouncementItem> items) {
        if (binding == null) {
            return;
        }
        boolean empty = items == null || items.isEmpty();
        adapter.submit(items);
        binding.includeForumEmpty.getRoot().setVisibility(empty ? View.VISIBLE : View.GONE);
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
