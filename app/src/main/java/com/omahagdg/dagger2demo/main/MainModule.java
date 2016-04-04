package com.omahagdg.dagger2demo.main;

import com.omahagdg.dagger2demo.dagger.ForMain;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@ForMain
@Module
public class MainModule {

    @Provides @ForMain WeatherService provideWeatherService(Retrofit retrofit) {
        return retrofit.create(WeatherService.class);
    }

    @Provides @ForMain LocationPermissionHelper provideLocationPermissionHelper(MainPresenter presenter) {
        return new LocationPermissionHelper(presenter);
    }
}
