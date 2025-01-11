package com.example.studyconnect.data.response;

import androidx.annotation.NonNull;

public class ClassResponse {
    private int id;
    private String name;
    private String subject;
    private String description;
    private int teacher_id;
    private String code;
    private String status;

    public ClassResponse(int id, String name, String subject, String description, int teacher_id, String code) {
        this.id = id;
        this.name = name;
        this.subject = subject;
        this.description = description;
        this.teacher_id = teacher_id;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ClassResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", subject='" + subject + '\'' +
                ", description='" + description + '\'' +
                ", teacher_id=" + teacher_id +
                ", code='" + code + '\'' +
                '}';
    }
}
