package com.example.studyconnect.utils;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Result<R> {

    private Result() {
        // Private constructor to prevent instantiation
    }

    public static final class Success<T> extends Result<T> {
        private final T data;

        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Success<?> success = (Success<?>) o;

            return Objects.equals(data, success.data);
        }

        @Override
        public int hashCode() {
            return data != null ? data.hashCode() : 0;
        }

        @NonNull
        @Override
        public String toString() {
            return "Success{" +
                    "data=" + data +
                    '}';
        }
    }

    public static final class Error<R> extends Result<R> {
        private final String error; // State untuk menyimpan pesan error
        private static final Error<Object> INSTANCE = new Error<>("Default error message");

        public Error(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }

        @SuppressWarnings("unchecked")
        public static <R> Error<R> getInstance(String error) {
            return (Error<R>) new Error<>(error);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Error<?> error1 = (Error<?>) o;

            return Objects.equals(error, error1.error);
        }

        @Override
        public int hashCode() {
            return error != null ? error.hashCode() : 0;
        }

        @NonNull
        @Override
        public String toString() {
            return "Error{" +
                    "error='" + error + '\'' +
                    '}';
        }
    }

    public static final class Loading<R> extends Result<R> {
        private static final Loading<Object> INSTANCE = new Loading<>();

        private Loading() {
            // Private constructor to prevent instantiation
        }

        @SuppressWarnings("unchecked")
        public static <R> Loading<R> getInstance() {
            return (Loading<R>) INSTANCE;
        }

        @NonNull
        @Override
        public String toString() {
            return "Loading";
        }
    }

    public static final class Empty<R> extends Result<R> {
        private static final Empty<Object> INSTANCE = new Empty<>();

        private Empty() {
            // Private constructor to prevent instantiation
        }

        @SuppressWarnings("unchecked")
        public static <R> Empty<R> getInstance() {
            return (Empty<R>) INSTANCE;
        }

        @NonNull
        @Override
        public String toString() {
            return "Empty";
        }
    }
}
