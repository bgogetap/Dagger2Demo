package com.omahagdg.dagger2demo.forecast;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ForecastLoader {

    private final ForecastService service;

    @Inject ForecastLoader(ForecastService service) {
        this.service = service;
    }

    Observable<ForecastResponse> getForecast(long cityId) {
        return service.getForecast(cityId)
                .map(Response::body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
