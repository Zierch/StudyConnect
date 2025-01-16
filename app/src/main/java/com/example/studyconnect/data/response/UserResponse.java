package com.example.studyconnect.data.response;

import androidx.annotation.NonNull;

public class UserResponse {
    private int id;
    private String email;
    private String role;
    private String name;
    private Pivot pivot;
    private String emailVerifiedAt;
    private String createdAt;
    private String updatedAt;

    public UserResponse(int id, String email, String name, String role, Pivot pivot, String emailVerifiedAt, String createdAt, String updatedAt) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
        this.pivot = pivot;
        this.emailVerifiedAt = emailVerifiedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setPivot(Pivot pivot) {
        this.pivot = pivot;
    }

    public Pivot getPivot() {
        return pivot;
    }

    public void setEmailVerifiedAt(String emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public String getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @NonNull
    @Override
    public String toString() {
        return
                "UserResponse{" +
                        ",id = '" + id + '\'' +
                        ",email = '" + email + '\'' +
                        ",role = '" + role + '\'' +
                        ",name = '" + name + '\'' +
                        ",pivot = '" + pivot + '\'' +
                        ",emailVerifiedAt = '" + emailVerifiedAt + '\'' +
                        ",createdAt = '" + createdAt + '\'' +
                        ",updatedAt = '" + updatedAt + '\'' +
                        "}";
    }
}
