package com.omahagdg.dagger2demo.forecast;

import com.omahagdg.dagger2demo.forecast.ForecastResponse.City;
import com.omahagdg.dagger2demo.main.WeatherResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import rx.Observable;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by bgogetap on 3/13/16.
 */
public class ForecastPresenterTest {

    @Mock ForecastLoader forecastLoader;
    @Mock ForecastView forecastView;

    private WeatherResponse weatherResponse = WeatherResponse.create("Omaha", 1, 1, Collections.<WeatherResponse.WeatherItem>emptyList(),
            WeatherResponse.Main.create(1, 1, 1, 1, 1), WeatherResponse.Wind.create(1, 1), WeatherResponse.Clouds.create(1), WeatherResponse.Sys.create(1, "", 1, 1));
    private City city = City.create(1, "Omaha", "US");
    private ForecastResponse forecastResponse = ForecastResponse.create(city, Collections.<ForecastResponse.Day>emptyList());

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ForecastPresenter forecastPresenter = new ForecastPresenter(weatherResponse, forecastLoader);
        when(forecastLoader.getForecast(1)).thenReturn(Observable.just(forecastResponse));
        forecastPresenter.takeView(forecastView);
    }

    @Test
    public void testWeatherResponseIsUsedToLoadForecastDataIfNotNull() {
        verify(forecastLoader, times(1)).getForecast(1);
        verify(forecastView, times(1)).setHeaderData(weatherResponse);
        verify(forecastView, times(1)).setForecastData(forecastResponse);
    }

    @Test public void testDaySelectedIsCalledWhenClicked() {

    }
}
