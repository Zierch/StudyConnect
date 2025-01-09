package com.example.studyconnect.data.response;

public class LoginResponse {
    private String token;
    private int user_id;
    private String role;

    public String getToken() {
        return token;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getRole() {
        return role;
    }
}
