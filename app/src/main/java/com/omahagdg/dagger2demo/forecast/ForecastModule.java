package com.omahagdg.dagger2demo.forecast;

import com.omahagdg.dagger2demo.dagger.ForForecast;
import com.omahagdg.dagger2demo.main.WeatherResponse;

import java.text.SimpleDateFormat;
import java.util.Locale;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ForecastModule {

    private final WeatherResponse weatherResponse;

    public ForecastModule(WeatherResponse weatherResponse) {
        this.weatherResponse = weatherResponse;
    }

    @Provides @ForForecast WeatherResponse provideWeatherResponse() {
        return weatherResponse;
    }

    @Provides @ForForecast SimpleDateFormat provideSimpleDateFormat() {
        return new SimpleDateFormat("EEE, MMM d", Locale.US);
    }

    @Provides @ForForecast ForecastService provideForecastService(Retrofit retrofit) {
        return retrofit.create(ForecastService.class);
    }
}
