package com.omahagdg.dagger2demo.forecast;

import com.omahagdg.dagger2demo.dagger.ForForecast;

import dagger.Subcomponent;

@ForForecast
@Subcomponent(modules = ForecastModule.class)
public interface ForecastComponent {

    void inject(ForecastItemView forecastItemView);

    void inject(ForecastFragment forecastFragment);

    void inject(DayDetailView dayDetailView);
}
