package com.example.studyconnect.data.response;

public class LoginResponse {
    private String email;
    private String password;
    private String token;

    public String getToken() {
        return token;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
