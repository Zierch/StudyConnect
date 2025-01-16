package com.example.studyconnect.data.response;

public class AnnouncementResponse {
    private int id;
    private int class_id;
    private int teacher_id;
    private String content;
    private String attachment_path;
    private String scheduled_at;
    private String status;
    private String created_at;
    private String updated_at;

    public AnnouncementResponse(
            int id,
            int class_id,
            int teacher_id,
            String content,
            String attachment_path,
            String scheduled_at,
            String status,
            String created_at,
            String updated_at) {
        this.id = id;
        this.class_id = class_id;
        this.teacher_id = teacher_id;
        this.content = content;
        this.attachment_path = attachment_path;
        this.scheduled_at = scheduled_at;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttachment_path() {
        return attachment_path;
    }

    public void setAttachment_path(String attachment_path) {
        this.attachment_path = attachment_path;
    }

    public String getScheduled_at() {
        return scheduled_at;
    }

    public void setScheduled_at(String scheduled_at) {
        this.scheduled_at = scheduled_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "AnnouncementResponse{" +
                "id=" + id +
                ", class_id=" + class_id +
                ", teacher_id=" + teacher_id +
                ", content='" + content + '\'' +
                ", attachment_path='" + attachment_path + '\'' +
                ", scheduled_at='" + scheduled_at + '\'' +
                ", status='" + status + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
