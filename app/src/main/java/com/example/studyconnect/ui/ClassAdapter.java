package com.example.studyconnect.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyconnect.data.ClassItem;
import com.example.studyconnect.databinding.ItemKelasBinding;

import java.util.ArrayList;
import java.util.List;

/** Menampilkan satu kartu kelas pada beranda. */
public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.Holder> {

    public interface OnClick {
        void onClass(ClassItem item);
    }

    private final List<ClassItem> items = new ArrayList<>();
    private final OnClick onClick;

    public ClassAdapter(OnClick onClick) {
        this.onClick = onClick;
    }

    public void submit(List<ClassItem> data) {
        items.clear();
        if (data != null) {
            items.addAll(data);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemKelasBinding binding = ItemKelasBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        ClassItem item = items.get(position);
        holder.binding.namaKelas.setText(item.getName());
        holder.binding.namaRuang.setText(item.getSubject() == null ? "" : item.getSubject());
        holder.binding.namaGuru.setText(item.getDescription() == null ? "" : item.getDescription());
        holder.binding.getRoot().setOnClickListener(view -> onClick.onClass(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final ItemKelasBinding binding;

        Holder(ItemKelasBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
