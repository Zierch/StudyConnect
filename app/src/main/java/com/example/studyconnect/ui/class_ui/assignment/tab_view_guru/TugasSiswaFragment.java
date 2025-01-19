package com.example.studyconnect.ui.class_ui.assignment.tab_view_guru;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.studyconnect.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TugasSiswaFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tugas_siswa, container, false);
    }
}