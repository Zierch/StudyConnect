package com.example.studyconnect.ui.class_ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyconnect.data.AnnouncementItem;
import com.example.studyconnect.databinding.ItemForumTextBinding;

import com.example.studyconnect.data.Session;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/** Daftar pengumuman pada tab Forum. */
public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.Holder> {

    private static final SimpleDateFormat ISO =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    private static final SimpleDateFormat READABLE =
            new SimpleDateFormat("d MMM yyyy, HH:mm", new Locale("id", "ID"));

    private final List<AnnouncementItem> items = new ArrayList<>();
    private String authorName = "";

    /** Nama yang ditampilkan sebagai penulis pengumuman. */
    public void setAuthorName(String name) {
        this.authorName = name;
    }

    public void submit(List<AnnouncementItem> data) {
        items.clear();
        if (data != null) {
            items.addAll(data);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(ItemForumTextBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        AnnouncementItem item = items.get(position);
        holder.binding.tvDeskripsiForum.setText(item.getContent());
        holder.binding.tvProfileName.setText(authorName);
        holder.binding.tvJamUpload.setText(pretty(item.getCreatedAt()));
    }

    /** "2026-11-04T14:06:07.000000Z" -> "4 Nov 2026, 14:06". */
    private static String pretty(String iso) {
        if (iso == null || iso.length() < 19) {
            return "";
        }
        try {
            Date date = ISO.parse(iso.substring(0, 19));
            return date == null ? iso : READABLE.format(date);
        } catch (ParseException e) {
            return iso;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final ItemForumTextBinding binding;

        Holder(ItemForumTextBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
