package com.omahagdg.dagger2demo.forecast;

import com.omahagdg.dagger2demo.dagger.ForForecast;
import com.omahagdg.dagger2demo.main.FragmentTestBase;
import com.omahagdg.dagger2demo.main.WeatherResponse;

import org.mockito.Mockito;

import java.text.SimpleDateFormat;
import java.util.Locale;

import dagger.Module;
import dagger.Provides;

@Module
public class MockForecastModule {

    @Provides @ForForecast WeatherResponse provideWeatherResponse() {
        return FragmentTestBase.getMockWeatherResponse();
    }

    @Provides @ForForecast SimpleDateFormat provideSimpleDateFormat() {
        return new SimpleDateFormat("EEE, MMM d", Locale.US);
    }

    @Provides @ForForecast ForecastService provideForecastService() {
        return Mockito.mock(ForecastService.class);
    }
}
