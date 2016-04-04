package com.omahagdg.dagger2demo.forecast;

import com.omahagdg.dagger2demo.dagger.ForForecast;

import dagger.Subcomponent;

@ForForecast
@Subcomponent(modules = MockForecastModule.class)
public interface MockForecastComponent extends ForecastComponent {

    ForecastService forecastService();
}
