package com.example.studyconnect.ui.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyconnect.data.response.ClassResponse;
import com.example.studyconnect.data.response.RegisterResponse;
import com.example.studyconnect.databinding.ItemKelasBinding;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private final List<ClassResponse> classList = new ArrayList<>();
    private final OnItemClickListener onItemClickListener;
    private RegisterResponse user;

    // Constructor untuk menerima listener
    public HomeAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    // Interface untuk click listener
    public interface OnItemClickListener {
        void onItemClick(ClassResponse classItem);
    }

    // Set data user
    public void setUser(RegisterResponse user) {
        this.user = user;
        notifyDataSetChanged();
    }

    public void setClassList(List<ClassResponse> newClassList) {
        classList.clear();
        classList.addAll(newClassList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeAdapter.ViewHolder(ItemKelasBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClassResponse classItem = classList.get(position);

        holder.binding.namaKelas.setText(classItem.getName());
        holder.binding.mapel.setText(classItem.getSubject());

        if (user.getRole().equals("siswa")){
            holder.binding.namaGuru.setText(user.getName());
        } else {
            holder.binding.namaGuru.setVisibility(View.GONE);
        }

        // Set onClickListener
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(classItem));
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemKelasBinding binding;
        public ViewHolder(@NonNull ItemKelasBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
