package com.example.studyconnect.ui.adapter;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
    private HomeViewModel viewModel;
    private final OnItemClickListener onItemClickListener;
    private final SharedPreferencesManager sharedPreferencesManager;
    private RegisterResponse user;

    // Constructor untuk menerima listener
    public HomeAdapter(OnItemClickListener onItemClickListener, HomeViewModel viewModel, SharedPreferencesManager sharedPreferencesManager) {
        this.onItemClickListener = onItemClickListener;
        this.viewModel = viewModel;
        this.sharedPreferencesManager = sharedPreferencesManager;
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

        // Set listener untuk more button
        holder.binding.more.setOnClickListener(v -> {
            // Tampilkan BottomSheet
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(holder.itemView.getContext());
            BottomItemKelasDialogBinding bottomSheetBinding = BottomItemKelasDialogBinding.inflate(LayoutInflater.from(holder.itemView.getContext()));

            if (user.getRole().equals("siswa")) {
                bottomSheetBinding.editKelas.setVisibility(View.GONE);
                bottomSheetBinding.arsipkanKelas.setVisibility(View.GONE);
            } else {
                bottomSheetBinding.batalkanPendaftaran.setVisibility(View.GONE);
            }

            bottomSheetBinding.batalkanPendaftaran.setOnClickListener(view -> {
                viewModel.leaveClass(classItem.getId());

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

            bottomSheetBinding.arsipkanKelas.setText("Delete (sementara)");
            bottomSheetBinding.arsipkanKelas.setOnClickListener(view -> {
                viewModel.deleteClass(classItem.getId());

                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.setContentView(bottomSheetBinding.getRoot());
            bottomSheetDialog.show();
        });

        if (sharedPreferencesManager.getData("role", "").equals("siswa")){
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
