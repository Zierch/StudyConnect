package com.example.studyconnect.ui.class_ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyconnect.databinding.ItemSiswaListBinding;

import java.util.ArrayList;
import java.util.List;

/** Daftar nama orang (guru atau siswa) pada tab Orang. */
public class NameAdapter extends RecyclerView.Adapter<NameAdapter.Holder> {

    private final List<String> names = new ArrayList<>();

    public void submit(List<String> data) {
        names.clear();
        if (data != null) {
            names.addAll(data);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(ItemSiswaListBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.binding.tvNamaSiswa.setText(names.get(position));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final ItemSiswaListBinding binding;

        Holder(ItemSiswaListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
