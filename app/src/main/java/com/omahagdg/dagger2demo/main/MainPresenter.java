package com.omahagdg.dagger2demo.main;

import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;

import com.omahagdg.dagger2demo.R;
import com.omahagdg.dagger2demo.base.Presenter;
import com.omahagdg.dagger2demo.dagger.ForMain;
import com.omahagdg.dagger2demo.permissions.Permission;
import com.omahagdg.dagger2demo.permissions.PermissionHelper;
import com.omahagdg.dagger2demo.utils.StringHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static android.content.Context.LOCATION_SERVICE;
import static android.location.LocationManager.GPS_PROVIDER;

@ForMain
public class MainPresenter extends Presenter<MainView>
        implements LocationPermissionHelper.LocationPermissionResultListener {

    static final String LAST_CITY_ID_KEY = "last_city_id";
    static final long INVALID_CITY_ID = -1;

    private final WeatherLoader weatherLoader;
    private final SharedPreferences sharedPreferences;
    private final StringHelper stringHelper;
    private final PermissionHelper permissionHelper;

    private List<WeatherResponse> data;
    private Subscription intervalSubscription;

    @Inject MainPresenter(
            WeatherLoader weatherLoader,
            SharedPreferences sharedPreferences,
            StringHelper stringHelper,
            PermissionHelper permissionHelper
    ) {
        this.weatherLoader = weatherLoader;
        this.sharedPreferences = sharedPreferences;
        this.stringHelper = stringHelper;
        this.permissionHelper = permissionHelper;
    }

    @Override protected void viewAttached() {
        loadData();
    }

    private void loadData() {
        if (data == null) {
            data = new ArrayList<>();
            long lastCityId = sharedPreferences.getLong(LAST_CITY_ID_KEY, INVALID_CITY_ID);
            if (lastCityId == INVALID_CITY_ID) {
                loadWeatherForCity("Omaha");
            } else {
                loadWeatherForCityId(lastCityId);
            }
        } else {
            setData();
        }
    }

    private void weatherLoaded(WeatherResponse response) {
        if (data.contains(response)) {
            int position = data.indexOf(response);
            data.remove(response);
            data.add(position, response);
        } else {
            data.add(0, response);
        }
    }

    private void setData() {
        if (getView() != null) {
            getView().setData(data);
        }
        if (data.size() > 0) {
            sharedPreferences.edit().putLong(LAST_CITY_ID_KEY, data.get(0).id()).apply();
        }
    }

    void refreshData() {
        if (data == null) return;
        Observable.from(data)
                .flatMap(weatherResponse -> weatherLoader.getWeatherForCityId(weatherResponse.id()))
                .filter(newResponse -> newResponse != null) // Open Weather Map free API call limits are strict
                .subscribe(this::weatherLoaded,
                        throwable -> Timber.d("Error refreshing data", throwable),
                        this::setData);
    }

    void loadWeatherForLocation() {
        if (permissionHelper.hasPermission(Permission.LOCATION)) {
            Location lastKnownLocation = getLastKnownLocation();
            if (lastKnownLocation != null) {
                weatherLoader.getWeatherForLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude())
                        .filter(response -> response != null)
                        .subscribe(this::weatherLoaded,
                                throwable -> Timber.e("Error loading weather for location", throwable),
                                this::setData);
            }
        } else {
            permissionHelper.requestPermission(Permission.LOCATION);
        }
    }

    void loadWeatherForCity(String cityName) {
        weatherLoader.getWeatherForCity(cityName)
                .filter(response -> response != null)
                .subscribe(this::weatherLoaded,
                        throwable -> Timber.e("Error loading weather for " + cityName, throwable),
                        this::setData);
    }

    void loadWeatherForCityId(long cityId) {
        weatherLoader.getWeatherForCityId(cityId)
                .filter(response -> response != null)
                .subscribe(this::weatherLoaded,
                        throwable -> Timber.e("Error loading for city ID: " + cityId, throwable),
                        this::setData);
    }

    void loadRandom() {
        if (getView() != null) {
            getView().toggleFab(false);
        }
        String[] cities = stringHelper.getStringArray(R.array.cities);
        Random random = new Random();
        if (intervalSubscription != null && !intervalSubscription.isUnsubscribed()) {
            intervalSubscription.unsubscribe();
        }
        intervalSubscription = Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tick -> {
                    Timber.d("interval tick");
                    loadWeatherForCity(cities[random.nextInt(cities.length - 1)]);
                    if (tick == 5) {
                        intervalSubscription.unsubscribe();
                        if (getView() != null) {
                            getView().toggleFab(true);
                        }
                    }
                });
    }

    @Override public void onLocationPermissionResult(boolean granted) {
        if (granted) {
            loadWeatherForLocation();
        } else {
            if (getView() != null) {
                getView().showPermissionDeniedMessage(permissionHelper.shouldShowPermissionRationale(Permission.LOCATION));
            }
        }
    }

    @SuppressWarnings("MissingPermission")
    private Location getLastKnownLocation() {
        Location lastKnownLocation = null;
        if (getView() != null) {
            LocationManager locationManager = (LocationManager)
                    getView().getContext().getSystemService(LOCATION_SERVICE);
            lastKnownLocation = locationManager.getLastKnownLocation(GPS_PROVIDER);
        }
        return lastKnownLocation;
    }

    @Override protected void viewDetached() {

    }
}
