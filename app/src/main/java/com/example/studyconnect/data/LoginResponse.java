package com.example.studyconnect.data;

/** Jawaban {@code POST /api/login}. */
public class LoginResponse {
    private String token;
    private int user_id;
    private String role;

    public String getToken() {
        return token;
    }

    public int getUserId() {
        return user_id;
    }

    public String getRole() {
        return role;
    }
}
