package com.omahagdg.dagger2demo.main;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface WeatherService {

    @GET("weather")
    Observable<Response<WeatherResponse>> getWeatherForCity(@Query("q") String cityName);

    @GET("weather") Observable<Response<WeatherResponse>> getWeatherForLocation(
            @Query("lat") double latitude,
            @Query("lon") double longitude);

    @GET("weather") Observable<Response<WeatherResponse>> getWeatherForId(@Query("id") long cityId);
}
