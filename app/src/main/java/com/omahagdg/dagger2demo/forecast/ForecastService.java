package com.omahagdg.dagger2demo.forecast;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ForecastService {

    @GET("forecast/daily")
    Observable<Response<ForecastResponse>> getForecast(@Query("id") long cityId);
}
