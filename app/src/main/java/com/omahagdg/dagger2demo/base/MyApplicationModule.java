package com.omahagdg.dagger2demo.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omahagdg.dagger2demo.R;
import com.omahagdg.dagger2demo.dagger.ComponentCache;
import com.omahagdg.dagger2demo.retrofit.AuthInterceptor;
import com.omahagdg.dagger2demo.retrofit.AutoValueAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class MyApplicationModule {

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    private final MyApplication application;

    MyApplicationModule(MyApplication application) {
        this.application = application;
    }

    @Provides @Singleton Context provideApplicationContext() {
        return application;
    }

    @Provides @Singleton SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides @Singleton OkHttpClient provideOkHttpClient() {
        String apiKey = application.getString(R.string.open_weather_map_api_key);
        return new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(apiKey))
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

    @Provides @Singleton Retrofit provideRetrofit(OkHttpClient client) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new AutoValueAdapterFactory())
                .create();
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides @Singleton ComponentCache provideComponentCache() {
        return new ComponentCache();
    }
}
