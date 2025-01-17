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
import com.example.studyconnect.data.response.AnnouncementResponse;
import com.example.studyconnect.data.response.RegisterResponse;
import com.example.studyconnect.databinding.BottomTwoDialogBinding;
import com.example.studyconnect.databinding.ItemForumGambarBinding;
import com.example.studyconnect.ui.class_ui.forum.FormForumActivity;
import com.example.studyconnect.ui.class_ui.forum.ForumViewModel;
import com.example.studyconnect.utils.Helper;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ViewHolder> {
    private final List<AnnouncementResponse> announcementList = new ArrayList<>();
    private final OnItemClickListener onItemClickListener;
    private final ForumViewModel viewModel;
    private RegisterResponse user;

    // Constructor untuk menerima listener
    public ForumAdapter(
            OnItemClickListener onItemClickListener,
            ForumViewModel viewModel
    ) {
        this.onItemClickListener = onItemClickListener;
        this.viewModel = viewModel;
    }

    // Set data user
    @SuppressLint("NotifyDataSetChanged")
    public void setUser(RegisterResponse user) {
        this.user = user;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setAnnouncementList(
            List<AnnouncementResponse> newAnnouncementList
    ) {
        announcementList.clear();
        announcementList.addAll(newAnnouncementList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ForumAdapter.ViewHolder(ItemForumGambarBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AnnouncementResponse announcementItem = announcementList.get(position);

        String inputDate = announcementItem.getCreated_at();

        if (announcementItem.getStatus().equals("published")) {
            String formattedDate = Helper.formatDate("Diposting ", inputDate);
            holder.binding.tvJamUpload.setText(formattedDate);
        } else {
            String formattedDate = Helper.formatDate("Dijadwalkan ", inputDate);
            holder.binding.tvJamUpload.setText(formattedDate);
        }

        holder.binding.tvDeskripsiForum.setText(announcementItem.getContent());

        if (user != null) {
            holder.binding.tvProfileName.setText(user.getName());
            if (user.getRole().equals("student")) {
                holder.binding.ivEditForum.setVisibility(View.GONE);
            }
        }

        if (announcementItem.getAttachment_path() == null) {
            holder.binding.fileCard.setVisibility(View.GONE);
        } else {
            holder.binding.namaGambar.setText(Helper.getFileNameFromPath(announcementItem.getAttachment_path()));
            Log.d("TAG", "onBindViewHolder: " + announcementItem.getAttachment_path());
            Helper.loadImageWithGlide(holder.itemView.getContext(), announcementItem.getAttachment_path(), holder.binding.gambar);
        }

        holder.binding.ivEditForum.setOnClickListener(v -> {
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
                int announcementId = announcementItem.getId();
                Intent intent = new Intent(holder.itemView.getContext(), FormForumActivity.class);
                intent.putExtra("isUpdate", true);
                intent.putExtra("announcementId", announcementId);
                holder.itemView.getContext().startActivity(intent);

                bottomSheetDialog.dismiss();
            });

            bottomSheetBinding.option2.setOnClickListener(view -> {
                viewModel.deleteAnnouncement(announcementItem.getId());

                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.setContentView(bottomSheetBinding.getRoot());
            bottomSheetDialog.show();
        });

        // Set onClickListener
        holder.itemView.setOnClickListener(v ->
                onItemClickListener.onItemClick(announcementItem)
        );
    }

    @Override
    public int getItemCount() {
        return announcementList.size();
    }

    // Interface untuk click listener
    public interface OnItemClickListener {
        void onItemClick(AnnouncementResponse announcementItem);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemForumGambarBinding binding;

        public ViewHolder(@NonNull ItemForumGambarBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
