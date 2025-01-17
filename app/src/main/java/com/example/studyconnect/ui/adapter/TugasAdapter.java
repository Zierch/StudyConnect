package com.example.studyconnect.ui.adapter;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyconnect.R;
import com.example.studyconnect.data.response.ClassAssignmentResponse;
import com.example.studyconnect.data.response.RegisterResponse;
import com.example.studyconnect.databinding.BottomTwoDialogBinding;
import com.example.studyconnect.databinding.ItemTugasListBinding;
import com.example.studyconnect.ui.class_ui.assignment.ClassAssignmentViewModel;
import com.example.studyconnect.ui.class_ui.assignment.FormTugasActivity;
import com.example.studyconnect.utils.Helper;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class TugasAdapter extends RecyclerView.Adapter<TugasAdapter.ViewHolder> {
    private final List<ClassAssignmentResponse> classAssignmentList = new ArrayList<>();
    private final OnItemClickListener onItemClickListener;
    private final ClassAssignmentViewModel viewModel;
    private RegisterResponse user;

    // Constructor untuk menerima listener
    public TugasAdapter(
            OnItemClickListener onItemClickListener,
            ClassAssignmentViewModel viewModel
    ) {
        this.onItemClickListener = onItemClickListener;
        this.viewModel = viewModel;
    }

    // Interface untuk click listener
    public interface OnItemClickListener {
        void onItemClick(ClassAssignmentResponse classAssignmentItem);
    }

    // Set data user
    @SuppressLint("NotifyDataSetChanged")
    public void setUser(RegisterResponse user) {
        this.user = user;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setClassAssignmentList(
            List<ClassAssignmentResponse> newClassAssignmentList
    ) {
        classAssignmentList.clear();
        classAssignmentList.addAll(newClassAssignmentList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TugasAdapter.ViewHolder(ItemTugasListBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClassAssignmentResponse classAssignmentItem = classAssignmentList.get(position);

        holder.binding.tvJudulTugas.setText(classAssignmentItem.getTitle());

        String inputDate = classAssignmentItem.getCreated_at();

        String formattedDate;
        if (classAssignmentItem.getStatus().equals("published")) {
            formattedDate = Helper.formatDate("Diposting ", inputDate);
        } else {
            formattedDate = Helper.formatDate("Dijadwalkan ", inputDate);
        }
        holder.binding.tvDiposting.setText(formattedDate);

        if (user != null) {
            if (user.getRole().equals("student")) {
                Log.d("TAG", "onBindViewHolder: " + user.getRole());
                holder.binding.settingTugas.setVisibility(View.GONE);
            }
        }

        holder.binding.settingTugas.setOnClickListener(v -> {
            // Tampilkan BottomSheet
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    holder.itemView.getContext()
            );
            BottomTwoDialogBinding bottomSheetBinding =
                    BottomTwoDialogBinding.inflate(
                            LayoutInflater.from(holder.itemView.getContext())
                    );

            bottomSheetBinding.option1.setText(R.string.text_edit);
            bottomSheetBinding.option2.setText(R.string.text_hapus);

            bottomSheetBinding.option1.setOnClickListener(view -> {
                int classId = classAssignmentItem.getClass_id();
                Intent intent = new Intent(holder.itemView.getContext(), FormTugasActivity.class);
                intent.putExtra("isUpdate", true);
                intent.putExtra("classId", classId);
                intent.putExtra("assignmentId", classAssignmentItem.getId());
                holder.itemView.getContext().startActivity(intent);

                bottomSheetDialog.dismiss();
            });

            bottomSheetBinding.option2.setOnClickListener(view -> {
                viewModel.deleteAssignment(
                        classAssignmentItem.getId(),
                        classAssignmentItem.getClass_id()
                );

                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.setContentView(bottomSheetBinding.getRoot());
            bottomSheetDialog.show();
        });

        // Set onClickListener
        holder.itemView.setOnClickListener(v ->
                onItemClickListener.onItemClick(classAssignmentItem)
        );
    }

    @Override
    public int getItemCount() {
        return classAssignmentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTugasListBinding binding;

        public ViewHolder(@NonNull ItemTugasListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
