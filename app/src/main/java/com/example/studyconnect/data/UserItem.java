package com.example.studyconnect.data;

/** Satu pengguna (guru atau siswa) dari API. */
public class UserItem {
    private int id;
    private String name;
    private String email;
    private String role;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
