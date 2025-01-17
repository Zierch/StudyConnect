package com.example.studyconnect.utils;

@FunctionalInterface
public interface DateCallback {
    void onDateSelected(int year, int month, int day);
}

