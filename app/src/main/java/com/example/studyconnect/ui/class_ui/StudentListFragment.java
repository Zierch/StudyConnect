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
import com.example.studyconnect.data.Session;
import com.example.studyconnect.data.UserItem;
import com.example.studyconnect.databinding.FragmentStudentListBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Tab Orang: guru pengampu dan siswa yang bergabung.
 *
 * <p>Endpoint daftar siswa hanya boleh dipanggil guru pemilik kelas, jadi siswa cukup
 * melihat nama guru yang dikirim lewat intent tanpa memanggil API.
 */
public class StudentListFragment extends Fragment {

    private FragmentStudentListBinding binding;
    private NameAdapter teacherAdapter;
    private NameAdapter studentAdapter;
    private Call<List<UserItem>> call;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStudentListBinding.inflate(inflater, container, false);
        int classId = ClassActivity.classIdOf(requireActivity());

        teacherAdapter = new NameAdapter();
        studentAdapter = new NameAdapter();
        binding.rvGuruList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvGuruList.setAdapter(teacherAdapter);
        binding.rvSiswaList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvSiswaList.setAdapter(studentAdapter);

        loadTeacher(ClassActivity.teacherIdOf(requireActivity()));

        if ("teacher".equals(Session.role(requireContext()))) {
            loadStudents(classId);
        } else {
            binding.tvKosongSiswa.setVisibility(View.VISIBLE);
            binding.tvKosongSiswa.setText(R.string.text_daftar_siswa_khusus_guru);
        }
        return binding.getRoot();
    }

    /** Nama guru diambil dari API supaya siswa juga melihat nama yang benar. */
    private void loadTeacher(int teacherId) {
        if (teacherId <= 0) {
            return;
        }
        Api.enqueue(ApiClient.service().getUser(Session.token(requireContext()), teacherId),
                user -> {
                    if (binding != null && user.getName() != null) {
                        List<String> names = new ArrayList<>();
                        names.add(user.getName());
                        teacherAdapter.submit(names);
                    }
                },
                message -> {
                    // Biarkan baris guru kosong; kesalahan sudah terlihat di daftar siswa.
                });
    }

    private void loadStudents(int classId) {
        if (classId <= 0) {
            return;
        }
        call = ApiClient.service().getStudents(Session.token(requireContext()), classId);
        Api.enqueue(call, this::showStudents,
                message -> {
                    if (binding != null) {
                        binding.tvKosongSiswa.setVisibility(View.VISIBLE);
                        binding.tvKosongSiswa.setText(message);
                    }
                });
    }

    private void showStudents(List<UserItem> students) {
        if (binding == null) {
            return;
        }
        List<String> names = new ArrayList<>();
        if (students != null) {
            for (UserItem student : students) {
                names.add(student.getName());
            }
        }
        studentAdapter.submit(names);
        boolean empty = names.isEmpty();
        binding.tvKosongSiswa.setVisibility(empty ? View.VISIBLE : View.GONE);
        if (empty) {
            binding.tvKosongSiswa.setText(R.string.text_belum_ada_siswa);
        }
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
