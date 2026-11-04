package com.example.studyconnect.di;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

/**
 * Provider SharedPreferences.
 *
 * <p>Retrofit tidak lagi disediakan di sini: satu-satunya instance ada di
 * {@code data/ApiClient} supaya tidak ada dua baseUrl yang bisa berbeda.
 */
@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    public SharedPreferences.Editor provideSharedPreferencesEditor(SharedPreferences sharedPreferences) {
        return sharedPreferences.edit();
    }
}
