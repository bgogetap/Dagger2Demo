package com.omahagdg.dagger2demo.main;

import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import com.omahagdg.dagger2demo.base.MainActivity;
import com.omahagdg.dagger2demo.base.MockMyApplicationComponent;
import com.omahagdg.dagger2demo.base.MyApplication;
import com.omahagdg.dagger2demo.dagger.ComponentCache;
import com.omahagdg.dagger2demo.utils.StringHelper;

import org.junit.Before;
import org.junit.Rule;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Collections;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

import static com.omahagdg.dagger2demo.main.MainPresenter.INVALID_CITY_ID;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public abstract class FragmentTestBase {

    protected static final double TEMP = 32;
    protected static final double TEMP_MAX = 40;
    protected static final double TEMP_MIN = 30;
    protected static final String CITY_NAME = "Omaha";
    protected static final String CITY_NAME_FROM_ID = "OmahaID";

    protected MockMyApplicationComponent applicationComponent;

    @Inject protected ComponentCache componentCache;
    @Inject protected StringHelper stringHelper;
    @Inject SharedPreferences sharedPreferences;

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(
            MainActivity.class,
            true,
            false);

    @SuppressLint("CommitPrefEdits") @Before
    public void setUp() throws IOException {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        MyApplication application = (MyApplication) instrumentation.getTargetContext().getApplicationContext();
        applicationComponent = (MockMyApplicationComponent) application.getComponent();
        applicationComponent.inject(this);

        MockMainComponent mainComponent = applicationComponent.plus(new MockMainModule());
        when(componentCache.hasComponentForTag(MainFragment.class.getName())).thenReturn(true);
        when(componentCache.getComponentForTag(MainFragment.class.getName())).thenReturn(mainComponent);
        WeatherService service = mainComponent.weatherService();
        if (returnIdFromSharedPrefs()) {
            when(sharedPreferences.getLong(anyString(), anyLong())).thenReturn(1L);
            when(service.getWeatherForId(anyLong())).thenReturn(Observable.just(Response.success(getMockWeatherResponseFromId())));
        } else {
            when(sharedPreferences.getLong(anyString(), anyLong())).thenReturn(INVALID_CITY_ID);
            when(service.getWeatherForCity(anyString())).thenReturn(Observable.just(Response.success(getMockWeatherResponse())));
        }
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);
        when(editor.putLong(anyString(), anyLong())).thenReturn(editor);

        mainActivityActivityTestRule.launchActivity(new Intent());
    }

    protected abstract boolean returnIdFromSharedPrefs();

    public static WeatherResponse getMockWeatherResponse() {
        return WeatherResponse.create(
                CITY_NAME,
                1,
                1,
                Collections.singletonList(WeatherResponse.WeatherItem.create(1, "", "", "")),
                WeatherResponse.Main.create(TEMP, 1, 1, TEMP_MIN, TEMP_MAX),
                WeatherResponse.Wind.create(1, 1),
                WeatherResponse.Clouds.create(1),
                WeatherResponse.Sys.create(1, "", 1, 1));
    }

    static WeatherResponse getMockWeatherResponseFromId() {
        return WeatherResponse.create(
                CITY_NAME_FROM_ID,
                1,
                1,
                Collections.singletonList(WeatherResponse.WeatherItem.create(1, "", "", "")),
                WeatherResponse.Main.create(TEMP, 1, 1, TEMP_MIN, TEMP_MAX),
                WeatherResponse.Wind.create(1, 1),
                WeatherResponse.Clouds.create(1),
                WeatherResponse.Sys.create(1, "", 1, 1));
    }
}
