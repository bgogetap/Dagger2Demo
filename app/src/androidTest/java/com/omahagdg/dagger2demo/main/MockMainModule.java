package com.omahagdg.dagger2demo.main;

import com.omahagdg.dagger2demo.dagger.ForMain;

import org.mockito.Mockito;

import dagger.Module;
import dagger.Provides;

@Module
public class MockMainModule {

    @Provides @ForMain WeatherService provideWeatherService() {
        return Mockito.mock(WeatherService.class);
    }

    @Provides @ForMain LocationPermissionHelper provideLocationPermissionHelper() {
        return Mockito.mock(LocationPermissionHelper.class);
    }
}
