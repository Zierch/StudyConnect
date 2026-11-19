package com.example.studyconnect.ui.class_ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyconnect.data.AssignmentItem;
import com.example.studyconnect.databinding.ItemTugasListBinding;

import java.util.ArrayList;
import java.util.List;

/** Daftar tugas pada tab "Tugas Kelas". */
public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.Holder> {

    private final List<AssignmentItem> items = new ArrayList<>();

    public void submit(List<AssignmentItem> data) {
        items.clear();
        if (data != null) {
            items.addAll(data);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(ItemTugasListBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        AssignmentItem item = items.get(position);
        holder.binding.textView.setText(item.getTitle());
        String due = item.getDueDate();
        holder.binding.tvDiposting.setText(due == null || due.isEmpty()
                ? holder.binding.getRoot().getContext().getString(
                        com.example.studyconnect.R.string.tak_ada_batas_waktu)
                : holder.binding.getRoot().getContext().getString(
                        com.example.studyconnect.R.string.text_tenggat_label, due));
        String title = item.getTitle() == null ? "" : item.getTitle().trim();
        holder.binding.tvInitial.setText(title.isEmpty() ? "T"
                : String.valueOf(Character.toUpperCase(title.charAt(0))));
        String status = item.getStatus() == null ? "" : item.getStatus().toLowerCase();
        boolean submitted = status.contains("submit") || status.contains("selesai")
                || status.contains("dikumpulkan");
        boolean materi = "materi".equalsIgnoreCase(item.getType());
        holder.binding.statusBadge.setText(materi
                ? com.example.studyconnect.R.string.text_materi
                : submitted ? com.example.studyconnect.R.string.text_sudah_dikumpulkan
                : com.example.studyconnect.R.string.text_belum_dikumpulkan);
        holder.binding.statusBadge.setBackgroundResource(materi
                ? com.example.studyconnect.R.drawable.sc_pill_info
                : submitted ? com.example.studyconnect.R.drawable.sc_pill_success
                : com.example.studyconnect.R.drawable.sc_pill_warn);
        holder.binding.statusBadge.setTextColor(androidx.core.content.ContextCompat.getColor(
                holder.binding.getRoot().getContext(), materi
                        ? com.example.studyconnect.R.color.sc_primary
                        : submitted ? com.example.studyconnect.R.color.sc_success_text
                        : com.example.studyconnect.R.color.sc_warn_text));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final ItemTugasListBinding binding;

        Holder(ItemTugasListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
