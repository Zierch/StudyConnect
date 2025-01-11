package com.example.studyconnect.ui.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyconnect.R;
import com.example.studyconnect.data.response.ClassResponse;
import com.example.studyconnect.databinding.BottomTwoDialogBinding;
import com.example.studyconnect.databinding.ItemKelasBinding;
import com.example.studyconnect.ui.home.HomeViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.ViewHolder> {
    private final List<ClassResponse> classList = new ArrayList<>();
    private HomeViewModel viewModel;
    private final OnItemClickListener onItemClickListener;

    // Constructor untuk menerima listener
    public ArchiveAdapter(OnItemClickListener onItemClickListener, HomeViewModel viewModel) {
        this.onItemClickListener = onItemClickListener;
        this.viewModel = viewModel;
    }

    // Interface untuk click listener
    public interface OnItemClickListener {
        void onItemClick(ClassResponse classItem);
    }

    public void setClassList(List<ClassResponse> newClassList) {
        classList.clear();
        classList.addAll(newClassList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArchiveAdapter.ViewHolder(ItemKelasBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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
            BottomTwoDialogBinding bottomSheetBinding = BottomTwoDialogBinding.inflate(LayoutInflater.from(holder.itemView.getContext()));

            bottomSheetBinding.option1.setText("Pulihkan");
            bottomSheetBinding.option1.setOnClickListener(view -> {
                viewModel.leaveClass(classItem.getId());

                bottomSheetDialog.dismiss();
            });

            bottomSheetBinding.option2.setText("Hapus");
            bottomSheetBinding.option2.setOnClickListener(view -> {
                viewModel.deleteClass(classItem.getId());

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemKelasBinding binding;
        public ViewHolder(@NonNull ItemKelasBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            binding.linear.setBackground(binding.linear.getContext().getDrawable(R.drawable.gradient_light_blue_background));
        }
    }
}
