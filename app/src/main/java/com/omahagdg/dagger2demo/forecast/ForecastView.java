package com.omahagdg.dagger2demo.forecast;

import com.omahagdg.dagger2demo.main.WeatherResponse;

public interface ForecastView {

    void setForecastData(ForecastResponse response);

    void setHeaderData(WeatherResponse weatherResponse);

    void daySelected(ForecastResponse.Day day, boolean animate);
}
