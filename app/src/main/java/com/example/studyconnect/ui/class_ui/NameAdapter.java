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
    private int roleLabel = com.example.studyconnect.R.string.text_nama_siswa;

    public void setRoleLabel(int resId) {
        this.roleLabel = resId;
    }

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
        String nama = names.get(position);
        if (nama == null) {
            nama = "";
        }
        holder.binding.tvNamaSiswa.setText(nama);
        holder.binding.tvPeran.setText(roleLabel);
        if (nama.trim().isEmpty()) {
            holder.binding.tvInisial.setText("?");
            return;
        }
        String[] kata = nama.trim().split("\\s+");
        if (kata.length == 0 || kata[0].isEmpty()) {
            holder.binding.tvInisial.setText("?");
            return;
        }
        StringBuilder inisial = new StringBuilder();
        inisial.append(Character.toUpperCase(kata[0].charAt(0)));
        if (kata.length > 1 && !kata[1].isEmpty()) {
            inisial.append(Character.toUpperCase(kata[1].charAt(0)));
        }
        holder.binding.tvInisial.setText(inisial.toString());
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
