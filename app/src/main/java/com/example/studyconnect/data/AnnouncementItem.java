package com.example.studyconnect.data;

/** Satu pengumuman dari {@code GET /api/announcement/{classId}}. */
public class AnnouncementItem {
    private int id;
    private String content;
    private String created_at;
    private String attachment_path;

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getAttachmentPath() {
        return attachment_path;
    }
}
