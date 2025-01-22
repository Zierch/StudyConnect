package com.example.studyconnect.data.response;

import androidx.annotation.NonNull;

public class ClassAssignmentResponse {
    private int id;
    private int class_id;
    private int max_score;
    private String attachment_path;
    private String link_url;
    private String due_date;
    private String description;
    private String scheduled_at;
    private String created_at;
    private String title;
    private String type;
    private String updated_at;
    private String options;
    private String status;

    public ClassAssignmentResponse(
            int class_id,
            int max_score,
            int id,
            String attachment_path,
            String due_date,
            String description,
            String scheduled_at,
            String created_at,
            String title,
            String type,
            String updated_at,
            String options,
            String status) {
        this.attachment_path = attachment_path;
        this.class_id = class_id;
        this.due_date = due_date;
        this.description = description;
        this.scheduled_at = scheduled_at;
        this.created_at = created_at;
        this.title = title;
        this.type = type;
        this.updated_at = updated_at;
        this.max_score = max_score;
        this.options = options;
        this.id = id;
        this.status = status;
    }

    public String getAttachment_path() {
        return attachment_path;
    }

    public void setAttachment_path(String attachment_path) {
        this.attachment_path = attachment_path;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScheduled_at() {
        return scheduled_at;
    }

    public void setScheduled_at(String scheduled_at) {
        this.scheduled_at = scheduled_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getMax_score() {
        return max_score;
    }

    public void setMax_score(int max_score) {
        this.max_score = max_score;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    @Override
    @NonNull
    public String toString() {
        return "ClassAssignmentResponse{" +
                "attachment_path=" + attachment_path +
                ", class_id=" + class_id +
                ", due_date='" + due_date + '\'' +
                ", description='" + description + '\'' +
                ", scheduled_at='" + scheduled_at + '\'' +
                ", created_at='" + created_at + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", max_score=" + max_score +
                ", options='" + options + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}