package com.omahagdg.dagger2demo.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.omahagdg.dagger2demo.permissions.PermissionHelper;
import com.omahagdg.dagger2demo.utils.StringHelper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;

import rx.Observable;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by bgogetap on 3/12/16.
 */
public class MainPresenterTest {

    @Mock MainView mainView;
    @Mock SharedPreferences sharedPreferences;
    @Mock SharedPreferences.Editor editor;
    @Mock WeatherLoader weatherLoader;
    @Mock Context context;

    private MainPresenter mainPresenter;
    private WeatherResponse weatherResponse = WeatherResponse.create("Omaha", 1, 1, Collections.<WeatherResponse.WeatherItem>emptyList(),
            WeatherResponse.Main.create(1, 1, 1, 1, 1), WeatherResponse.Wind.create(1, 1), WeatherResponse.Clouds.create(1), WeatherResponse.Sys.create(1, "", 1, 1));
    private WeatherResponse anotherWeatherResponse = WeatherResponse.create("Lincoln", 2, 1, Collections.<WeatherResponse.WeatherItem>emptyList(),
            WeatherResponse.Main.create(1, 1, 1, 1, 1), WeatherResponse.Wind.create(1, 1), WeatherResponse.Clouds.create(1), WeatherResponse.Sys.create(1, "", 1, 1));
    private Observable<WeatherResponse> responseObservable;
    private Observable<WeatherResponse> anotherResponseObservable;

    @SuppressLint("CommitPrefEdits")
    @Before
    public void setupPresenter() {
        MockitoAnnotations.initMocks(this);
        PermissionHelper permissionHelper = new PermissionHelper();
        StringHelper stringHelper = new StringHelper(context);
        when(sharedPreferences.edit()).thenReturn(editor);
        when(editor.putLong(anyString(), anyLong())).thenReturn(editor);
        mainPresenter = new MainPresenter(weatherLoader, sharedPreferences, stringHelper, permissionHelper);
        responseObservable = Observable.just(weatherResponse);
        anotherResponseObservable = Observable.just(anotherWeatherResponse);
    }

    @Test
    public void testDataLoadedAndPassedToViewWhenViewAttached() {
        when(sharedPreferences.getLong("last_city_id", -1)).thenReturn(-1L);
        when(weatherLoader.getWeatherForCity("Omaha")).thenReturn(responseObservable);
        mainPresenter.takeView(mainView);
        verify(weatherLoader, times(1)).getWeatherForCity("Omaha");
        verify(mainView).setData(Collections.singletonList(weatherResponse));
    }

    @Test
    public void testSavedCityIdIsLoadedIfSetInSharedPrefs() {
        when(sharedPreferences.getLong("last_city_id", -1)).thenReturn(1L);
        when(weatherLoader.getWeatherForCityId(1L)).thenReturn(responseObservable);
        mainPresenter.takeView(mainView);
        verify(weatherLoader, times(1)).getWeatherForCityId(1L);
        verify(mainView).setData(Collections.singletonList(weatherResponse));
    }

    @Test
    public void testNewCitiesAreAddedToDataList() {
        when(sharedPreferences.getLong("last_city_id", -1)).thenReturn(-1L);
        when(weatherLoader.getWeatherForCity("Omaha")).thenReturn(responseObservable);
        when(weatherLoader.getWeatherForCity("Lincoln")).thenReturn(anotherResponseObservable);
        mainPresenter.takeView(mainView);
        mainPresenter.loadWeatherForCity("Lincoln");
        verify(mainView, atLeastOnce()).setData(Arrays.asList(anotherWeatherResponse, weatherResponse));
    }

    @Test
    public void testDuplicateCitiesNotAddedToList() {
        when(sharedPreferences.getLong("last_city_id", -1)).thenReturn(-1L);
        when(weatherLoader.getWeatherForCity("Omaha")).thenReturn(responseObservable);
        mainPresenter.takeView(mainView);
        mainPresenter.loadWeatherForCity("Omaha");
        verify(mainView, times(2)).setData(Collections.singletonList(weatherResponse));
        verify(mainView, never()).setData(Arrays.asList(weatherResponse, weatherResponse));
    }
}
