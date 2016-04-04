package com.omahagdg.dagger2demo.main;

import com.omahagdg.dagger2demo.dagger.ForMain;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@ForMain class WeatherLoader {

    private final WeatherService service;

    @Inject WeatherLoader(WeatherService service) {
        this.service = service;
    }

    Observable<WeatherResponse> getWeatherForCity(final String cityName) {
        return service.getWeatherForCity(cityName)
                .map(Response::body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    Observable<WeatherResponse> getWeatherForLocation(double latitude, double longitude) {
        return service.getWeatherForLocation(latitude, longitude)
                .map(Response::body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    Observable<WeatherResponse> getWeatherForCityId(long cityId) {
        return service.getWeatherForId(cityId)
                .map(Response::body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
