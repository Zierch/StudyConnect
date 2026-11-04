package com.example.studyconnect.data;

/** Satu tugas dari {@code GET /api/assignment/{classId}}. */
public class AssignmentItem {
    private int id;
    private String title;
    private String description;
    private String due_date;
    private int max_score;
    private String type;
    private String status;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDueDate() {
        return due_date;
    }

    public int getMaxScore() {
        return max_score;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }
}
