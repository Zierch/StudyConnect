package com.example.studyconnect.data.response;

import androidx.annotation.NonNull;

public class MaterialResponse {
    private String attachment_path;
    private String updated_at;
    private int teacher_id;
    private int class_id;
    private String description;
    private String scheduled_at;
    private String created_at;
    private int id;
    private String title;
    private String status;

    public void setAttachment_path(String attachment_path) {
        this.attachment_path = attachment_path;
    }

    public String getAttachment_path() {
        return attachment_path;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setScheduled_at(String scheduled_at) {
        this.scheduled_at = scheduled_at;
    }

    public String getScheduled_at() {
        return scheduled_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @NonNull
    @Override
    public String toString() {
        return
                "MaterialResponse{" +
                        "attachment_path = '" + attachment_path + '\'' +
                        ",updated_at = '" + updated_at + '\'' +
                        ",teacher_id = '" + teacher_id + '\'' +
                        ",class_id = '" + class_id + '\'' +
                        ",description = '" + description + '\'' +
                        ",scheduled_at = '" + scheduled_at + '\'' +
                        ",created_at = '" + created_at + '\'' +
                        ",id = '" + id + '\'' +
                        ",title = '" + title + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}