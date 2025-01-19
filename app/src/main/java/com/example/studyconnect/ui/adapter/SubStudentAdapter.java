package com.example.studyconnect.ui.adapter;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyconnect.data.model.StudentModel;
import com.example.studyconnect.databinding.ItemSiswaListBinding;

import java.util.ArrayList;
import java.util.List;

public class SubStudentAdapter extends RecyclerView.Adapter<SubStudentAdapter.ViewHolder> {
    private final List<StudentModel> studentList = new ArrayList<>();
    private final OnItemClickListener onItemClickListener;

    // Constructor untuk menerima listener
    public SubStudentAdapter(
            OnItemClickListener onItemClickListener
    ) {
        this.onItemClickListener = onItemClickListener;
    }

    // Interface untuk click listener
    public interface OnItemClickListener {
        void onItemClick(StudentModel studentItem);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSubStudentList(
            List<StudentModel> newstudentList
    ) {
        studentList.clear();
        studentList.addAll(newstudentList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubStudentAdapter.ViewHolder(ItemSiswaListBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentModel studentItem = studentList.get(position);

        holder.binding.tvStudentName.setText(studentItem.getName());

        holder.binding.ivSettingsStudent.setVisibility(View.GONE);

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

