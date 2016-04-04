package com.omahagdg.dagger2demo.forecast;

import com.omahagdg.dagger2demo.base.Presenter;
import com.omahagdg.dagger2demo.dagger.ForForecast;
import com.omahagdg.dagger2demo.main.WeatherResponse;

import javax.inject.Inject;

import timber.log.Timber;

@ForForecast
public class ForecastPresenter extends Presenter<ForecastView> {

    private final ForecastLoader forecastLoader;

    private WeatherResponse weatherResponse;
    private ForecastResponse data;
    private ForecastResponse.Day selectedDay;

    @Inject ForecastPresenter(
            WeatherResponse weatherResponse,
            ForecastLoader forecastLoader
    ) {
        this.weatherResponse = weatherResponse;
        this.forecastLoader = forecastLoader;
    }

    @Override protected void viewAttached() {
        if (selectedDay != null) {
            daySelected(selectedDay, false);
        }
        setHeaderData(weatherResponse);
    }

    private void setHeaderData(WeatherResponse weatherResponse) {
        if (getView() != null) {
            getView().setHeaderData(weatherResponse);
        }
        loadForecastData();
    }

    void loadForecastData() {
        if (data == null) {
            forecastLoader.getForecast(weatherResponse.id()).subscribe(data -> {
                this.data = data;
                updateView();
            }, throwable -> Timber.e("Error getting forecast", throwable));
        } else {
            updateView();
        }
    }

    private void updateView() {
        if (getView() != null) {
            getView().setForecastData(data);
        }
    }

    void daySelected(ForecastResponse.Day day, boolean animate) {
        selectedDay = day;
        if (getView() != null) {
            getView().daySelected(day, animate);
        }
    }

    void clearSelectedDay() {
        selectedDay = null;
    }

    @Override protected void viewDetached() {

    }
}
