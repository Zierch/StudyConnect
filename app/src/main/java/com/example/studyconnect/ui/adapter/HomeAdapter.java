package com.example.studyconnect.ui.adapter;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyconnect.data.response.ClassResponse;
import com.example.studyconnect.data.response.RegisterResponse;
import com.example.studyconnect.databinding.BottomItemKelasDialogBinding;
import com.example.studyconnect.databinding.ItemKelasBinding;
import com.example.studyconnect.ui.class_ui.FormClassActivity;
import com.example.studyconnect.ui.home.HomeViewModel;
import com.example.studyconnect.utils.SharedPreferencesManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private final List<ClassResponse> classList = new ArrayList<>();
    private final HomeViewModel viewModel;
    private LifecycleOwner lifecycleOwner;
    private final OnItemClickListener onItemClickListener;
    private final SharedPreferencesManager sharedPreferencesManager;

    // Constructor untuk menerima listener
    public HomeAdapter(OnItemClickListener onItemClickListener, HomeViewModel viewModel, SharedPreferencesManager sharedPreferencesManager, LifecycleOwner lifecycleOwner) {
        this.onItemClickListener = onItemClickListener;
        this.viewModel = viewModel;
        this.sharedPreferencesManager = sharedPreferencesManager;
        this.lifecycleOwner = lifecycleOwner;
    }

    @SuppressLint("NotifyDataSetChanged")
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
        String role = sharedPreferencesManager.getData("role", null);

        holder.binding.namaKelas.setText(classItem.getName());
        holder.binding.mapel.setText(classItem.getSubject());

        viewModel.getUser(classItem.getTeacher_id()).observe(lifecycleOwner, user -> {
            if (user != null) {
                if (role.equals("student")) {
                    holder.binding.namaGuru.setText(user.getName());
                } else {
                    holder.binding.namaGuru.setVisibility(View.GONE);
                }
            }
        });

        // Set listener untuk more button
        holder.binding.more.setOnClickListener(v -> {
            // Tampilkan BottomSheet
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(holder.itemView.getContext());
            BottomItemKelasDialogBinding bottomSheetBinding = BottomItemKelasDialogBinding.inflate(LayoutInflater.from(holder.itemView.getContext()));

            if (role.equals("student")) {
                bottomSheetBinding.editKelas.setVisibility(View.GONE);
                bottomSheetBinding.arsipkanKelas.setVisibility(View.GONE);
            } else {
                bottomSheetBinding.batalkanPendaftaran.setVisibility(View.GONE);
            }

            bottomSheetBinding.batalkanPendaftaran.setOnClickListener(view -> {
                viewModel.leaveClass(classItem.getCode());
                bottomSheetDialog.dismiss();
            });

            bottomSheetBinding.editKelas.setOnClickListener(view -> {
                int classId = classItem.getId();
                sharedPreferencesManager.saveData("classId", classId);
                Intent intent = new Intent(holder.itemView.getContext(), FormClassActivity.class);
                intent.putExtra("isUpdate", true);
                holder.itemView.getContext().startActivity(intent);
                bottomSheetDialog.dismiss();
            });

            bottomSheetBinding.arsipkanKelas.setOnClickListener(view -> {
                viewModel.archiveClass(classItem.getId());
                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.setContentView(bottomSheetBinding.getRoot());
            bottomSheetDialog.show();
        });

        // Set onClickListener
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(classItem));
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(ClassResponse classItem);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemKelasBinding binding;

        public ViewHolder(@NonNull ItemKelasBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}