package com.example.studyconnect.data.model;

import androidx.annotation.NonNull;

public class ListTugasKelas {
    private int id;
    private String title;
    private int class_id;
    private String type;
    private String status;
    private String created_at;
    private boolean isMateri;

    public ListTugasKelas(
            int id, String title, int class_id, String type, String status,
            String created_at, boolean isMateri
    ) {
        this.id = id;
        this.title = title;
        this.class_id = class_id;
        this.type = type;
        this.status = status;
        this.created_at = created_at;
        this.isMateri = isMateri;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public boolean isMateri() {
        return isMateri;
    }

    public void setMateri(boolean materi) {
        isMateri = materi;
    }

    @NonNull
    @Override
    public String toString() {
        return "ListTugasKelas{" +
                "id=" + id +
                ", created_at='" + created_at + '\'' +
                ", isMateri=" + isMateri +
                '}';
    }
}
