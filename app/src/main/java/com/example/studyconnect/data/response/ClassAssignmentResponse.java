package com.example.studyconnect.data.response;

import androidx.annotation.NonNull;

public class ClassAssignmentResponse {
    private Object attachmentPath;
    private int classId;
    private String dueDate;
    private String description;
    private String scheduledAt;
    private String createdAt;
    private String title;
    private String type;
    private String updatedAt;
    private int maxScore;
    private String options;
    private int id;
    private String status;

    public ClassAssignmentResponse(
            Object attachmentPath,
            int classId,
            String dueDate,
            String description,
            String scheduledAt,
            String createdAt,
            String title,
            String type,
            String updatedAt,
            int maxScore,
            String options,
            int id,
            String status) {
        this.attachmentPath = attachmentPath;
        this.classId = classId;
        this.dueDate = dueDate;
        this.description = description;
        this.scheduledAt = scheduledAt;
        this.createdAt = createdAt;
        this.title = title;
        this.type = type;
        this.updatedAt = updatedAt;
        this.maxScore = maxScore;
        this.options = options;
        this.id = id;
        this.status = status;
    }

    public Object getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(Object attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(String scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
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

    @Override
    @NonNull
    public String toString() {
        return "ClassAssignmentResponse{" +
                "attachmentPath=" + attachmentPath +
                ", classId=" + classId +
                ", dueDate='" + dueDate + '\'' +
                ", description='" + description + '\'' +
                ", scheduledAt='" + scheduledAt + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", maxScore=" + maxScore +
                ", options='" + options + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}