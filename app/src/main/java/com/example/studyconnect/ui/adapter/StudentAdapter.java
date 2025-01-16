package com.example.studyconnect.ui.adapter;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyconnect.R;
import com.example.studyconnect.data.response.UserResponse;
import com.example.studyconnect.data.response.RegisterResponse;
import com.example.studyconnect.databinding.BottomTwoDialogBinding;
import com.example.studyconnect.databinding.ItemSiswaListBinding;
import com.example.studyconnect.ui.class_ui.student.StudentListViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private final List<UserResponse> studentList = new ArrayList<>();
    private final OnItemClickListener onItemClickListener;
    private final StudentListViewModel viewModel;
    private RegisterResponse user;

    // Constructor untuk menerima listener
    public StudentAdapter(
            OnItemClickListener onItemClickListener,
            StudentListViewModel viewModel
    ) {
        this.onItemClickListener = onItemClickListener;
        this.viewModel = viewModel;
    }

    // Interface untuk click listener
    public interface OnItemClickListener {
        void onItemClick(UserResponse studentItem);
    }

    // Set data user
    @SuppressLint("NotifyDataSetChanged")
    public void setUser(RegisterResponse user) {
        this.user = user;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setStudentList(
            List<UserResponse> newstudentList
    ) {
        studentList.clear();
        studentList.addAll(newstudentList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StudentAdapter.ViewHolder(ItemSiswaListBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserResponse studentItem = studentList.get(position);

        holder.binding.tvStudentName.setText(studentItem.getName());

        if (user.getRole().equals("student"))
            holder.binding.ivSettingsStudent.setVisibility(View.GONE);

        holder.binding.ivSettingsStudent.setOnClickListener(v -> {
            // Tampilkan BottomSheet
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    holder.itemView.getContext()
            );
            BottomTwoDialogBinding bottomSheetBinding =
                    BottomTwoDialogBinding.inflate(
                            LayoutInflater.from(holder.itemView.getContext())
                    );

            bottomSheetBinding.option1.setText(R.string.text_kick);
            bottomSheetBinding.option2.setVisibility(View.GONE);

            bottomSheetBinding.option1.setOnClickListener(view -> {
                Toast.makeText(v.getContext(), "Kick", Toast.LENGTH_SHORT).show();

                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.setContentView(bottomSheetBinding.getRoot());
            bottomSheetDialog.show();
        });

        // Set onClickListener
        holder.itemView.setOnClickListener(v ->
                onItemClickListener.onItemClick(studentItem)
        );
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemSiswaListBinding binding;

        public ViewHolder(@NonNull ItemSiswaListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
