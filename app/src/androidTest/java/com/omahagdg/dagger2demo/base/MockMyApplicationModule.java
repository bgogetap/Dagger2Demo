package com.omahagdg.dagger2demo.base;

import android.content.Context;
import android.content.SharedPreferences;

import com.omahagdg.dagger2demo.R;
import com.omahagdg.dagger2demo.dagger.ComponentCache;
import com.omahagdg.dagger2demo.retrofit.AuthInterceptor;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class MockMyApplicationModule {

    private final MyApplication application;

    MockMyApplicationModule(MyApplication application) {
        this.application = application;
    }

    @Provides @Singleton Context provideApplicationContext() {
        return application;
    }

    @Provides @Singleton SharedPreferences provideSharedPreferences() {
        return Mockito.mock(SharedPreferences.class);
    }

    @Provides @Singleton OkHttpClient provideOkHttpClient() {
        String apiKey = application.getString(R.string.open_weather_map_api_key);
        return new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(apiKey))
                .build();
    }

    @Provides @Singleton Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .client(client)
                .baseUrl("http://www.test.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides @Singleton ComponentCache provideComponentCache() {
        return Mockito.mock(ComponentCache.class);
    }
}
