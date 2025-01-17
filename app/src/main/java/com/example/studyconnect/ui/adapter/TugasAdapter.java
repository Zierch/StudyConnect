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
import com.example.studyconnect.data.model.ListTugasKelas;
import com.example.studyconnect.data.response.RegisterResponse;
import com.example.studyconnect.databinding.BottomTwoDialogBinding;
import com.example.studyconnect.databinding.ItemTugasListBinding;
import com.example.studyconnect.ui.class_ui.assignment.ClassAssignmentViewModel;
import com.example.studyconnect.ui.class_ui.assignment.FormMateriActivity;
import com.example.studyconnect.ui.class_ui.assignment.FormTugasActivity;
import com.example.studyconnect.utils.Helper;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class TugasAdapter extends RecyclerView.Adapter<TugasAdapter.ViewHolder> {
    private final List<ListTugasKelas> listTugasKelas = new ArrayList<>();
    private final OnItemClickListener onItemClickListener;
    private final ClassAssignmentViewModel viewModel;
    private RegisterResponse user;

    public TugasAdapter(
            OnItemClickListener onItemClickListener,
            ClassAssignmentViewModel viewModel
    ) {
        this.onItemClickListener = onItemClickListener;
        this.viewModel = viewModel;
    }

    // Interface untuk click listener
    public interface OnItemClickListener {
        void onItemClick(ListTugasKelas tugasKelasItem);
    }

    // Set data user
    @SuppressLint("NotifyDataSetChanged")
    public void setUser(RegisterResponse user) {
        this.user = user;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setlistTugasKelas(
            List<ListTugasKelas> newlistTugasKelas
    ) {
        listTugasKelas.clear();
        listTugasKelas.addAll(newlistTugasKelas);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemTugasListBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListTugasKelas tugasKelasItem = listTugasKelas.get(position);

        holder.binding.tvJudulTugas.setText(tugasKelasItem.getTitle());

        String inputDate = tugasKelasItem.getCreated_at();

        switch (tugasKelasItem.getType()) {
            case "multiple_choice":
                holder.binding.ivIconItem.setImageResource(R.drawable.ic_multiple_choice);
                break;
            case "file_upload":
                holder.binding.ivIconItem.setImageResource(R.drawable.ic_file_upload_circle);
                break;
            case "essay":
                holder.binding.ivIconItem.setImageResource(R.drawable.ic_tugas_circle);
                break;
            default:
                holder.binding.ivIconItem.setImageResource(R.drawable.ic_materi_circle);
                break;
        }

        String formattedDate;
        if (tugasKelasItem.getStatus().equals("published")) {
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
                int classId = tugasKelasItem.getClass_id();
                Intent intent = null;

                switch (tugasKelasItem.getType()) {
                    case "multiple_choice":
                        break;
                    case "file_upload":
                    case "essay":
                        intent = new Intent(holder.itemView.getContext(), FormTugasActivity.class);
                        break;
                    default:
                        intent = new Intent(holder.itemView.getContext(), FormMateriActivity.class);
                        break;
                }

                assert intent != null;
                intent.putExtra("isUpdate", true);
                intent.putExtra("classId", classId);
                intent.putExtra("assignmentId", tugasKelasItem.getId());
                holder.itemView.getContext().startActivity(intent);

                bottomSheetDialog.dismiss();
            });

            bottomSheetBinding.option2.setOnClickListener(view -> {
                viewModel.deleteAssignment(
                        tugasKelasItem.getId(),
                        tugasKelasItem.getClass_id()
                );

                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.setContentView(bottomSheetBinding.getRoot());
            bottomSheetDialog.show();
        });

        // Set onClickListener
        holder.itemView.setOnClickListener(v ->
                onItemClickListener.onItemClick(tugasKelasItem)
        );
    }

    @Override
    public int getItemCount() {
        return listTugasKelas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTugasListBinding binding;

        public ViewHolder(@NonNull ItemTugasListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
