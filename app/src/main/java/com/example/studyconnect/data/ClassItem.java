package com.example.studyconnect.data;

/** Satu kelas seperti yang dikembalikan {@code GET /api/classes}. */
public class ClassItem {
    private int id;
    private String name;
    private String subject;
    private String description;
    private String code;
    private int teacher_id;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSubject() {
        return subject;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public int getTeacherId() {
        return teacher_id;
    }
}
