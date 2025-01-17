package com.example.studyconnect.di;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.example.studyconnect.data.ApiService;
import com.example.studyconnect.data.StudyRepository;
import com.example.studyconnect.utils.Helper;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public MyApp provideMyApp(@ApplicationContext Context context) {
        return (MyApp) context;
    }

    @Provides
    @Singleton
    public ApiService provideApiService(@ApplicationContext Context context) {
        File httpCacheDirectory = new File(context.getCacheDir(), "http-cache");
        int cacheSize = 10 * 1024 * 1024; // 10 MB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);

        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    SharedPreferences preferences = provideSharedPreferences(context);
                    String token = preferences.getString("token", null);

                    if (token != null) {
                        request = request.newBuilder()
                                .header("Authorization", "Bearer " + token)
                                .build();
                    }
                    if (Helper.hasNetworkConnection(context)) {
                        request = request.newBuilder()
                                .header("Cache-Control", "public, max-age=60") // Cache 1 menit
                                .build();
                    } else {
                        request = request.newBuilder()
                                .header("Cache-Control", "public, only-if-cached, max-stale=604800") // Cache offline (7 hari)
                                .build();
                    }
                    return chain.proceed(request);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://studyconnect-api-production.up.railway.app/api/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ApiService.class);
    }

    @Provides
    @Singleton
    public StudyRepository provideMyRepository(ApiService apiService, @ApplicationContext Context context) {
        return new StudyRepository(apiService, context);
    }

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences(@ApplicationContext Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    public SharedPreferences.Editor provideSharedPreferencesEditor(SharedPreferences sharedPreferences) {
        return sharedPreferences.edit();
    }
}

