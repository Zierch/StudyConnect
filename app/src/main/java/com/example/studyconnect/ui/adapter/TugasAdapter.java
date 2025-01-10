package com.example.studyconnect.ui.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyconnect.data.response.ClassAssignmentResponse;
import com.example.studyconnect.data.response.RegisterResponse;
import com.example.studyconnect.databinding.ItemTugasListBinding;

import java.util.ArrayList;
import java.util.List;

public class TugasAdapter extends RecyclerView.Adapter<TugasAdapter.ViewHolder> {
    private final List<ClassAssignmentResponse> classAssignmentList = new ArrayList<>();
    private final OnItemClickListener onItemClickListener;
    private RegisterResponse user;

    // Constructor untuk menerima listener
    public TugasAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    // Interface untuk click listener
    public interface OnItemClickListener {
        void onItemClick(ClassAssignmentResponse classAssignmentItem);
    }

    // Set data user
    public void setUser(RegisterResponse user) {
        this.user = user;
        notifyDataSetChanged();
    }

    public void setClassAssignmentList(List<ClassAssignmentResponse> newClassAssignmentList) {
        classAssignmentList.clear();
        classAssignmentList.addAll(newClassAssignmentList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TugasAdapter.ViewHolder(ItemTugasListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClassAssignmentResponse classAssignmentItem = classAssignmentList.get(position);

        holder.binding.tvJudulTugas.setText(classAssignmentItem.getTitle());
        holder.binding.tvDiposting.setText(classAssignmentItem.getCreatedAt());

        // Set onClickListener
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(classAssignmentItem));
    }

    @Override
    public int getItemCount() {
        return classAssignmentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemTugasListBinding binding;

        public ViewHolder(@NonNull ItemTugasListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
