package com.omahagdg.dagger2demo.forecast;

import android.support.test.runner.AndroidJUnit4;

import com.omahagdg.dagger2demo.R;
import com.omahagdg.dagger2demo.main.FragmentTestBase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import retrofit2.Response;
import rx.Observable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class ForecastFragmentTest extends FragmentTestBase {

    @Before public void setUp() throws IOException {
        super.setUp();
        MockForecastComponent forecastComponent = applicationComponent.plus(new MockForecastModule());
        ForecastService service = forecastComponent.forecastService();
        when(componentCache.hasComponentForTag(ForecastFragment.class.getName())).thenReturn(true);
        when(componentCache.getComponentForTag(ForecastFragment.class.getName())).thenReturn(forecastComponent);
        Observable<Response<ForecastResponse>> responseObservable = Observable.just(Response.success(getForecastResponse()));
        when(service.getForecast(anyLong())).thenReturn(responseObservable);

        onView(withId(R.id.tv_city_name)).perform(click());
    }

    @Test public void testWeatherResponseDataIsDisplayedInHeader() {
        onView(withId(R.id.tv_city_name)).check(matches(withText(CITY_NAME)));
        onView(withId(R.id.tv_temp)).check(matches(withText(stringHelper.getString(R.string.degree_symbol_format, TEMP))));
    }

    @Test public void testForecastDataLoads() {
        String highTempText = stringHelper.getString(R.string.high_temp_format, 65.0);
        onView(allOf(withId(R.id.tv_temp_max), withText(highTempText))).check(matches(isDisplayed()));
    }

    static ForecastResponse getForecastResponse() {
        return ForecastResponse.create(
                ForecastResponse.City.create(1, "Omaha", "USA"),
                Arrays.asList(
                        ForecastResponse.Day.create(
                                1458669600,
                                ForecastResponse.Temp.create(
                                        60, // Current
                                        40, // Low
                                        65, // High
                                        42, // Night
                                        58, // Evening
                                        42  // Morning
                                ),
                                Collections.emptyList(),
                                35
                        ),
                        ForecastResponse.Day.create(
                                1458756000,
                                ForecastResponse.Temp.create(
                                        70,
                                        50,
                                        75,
                                        52,
                                        68,
                                        52
                                ),
                                Collections.emptyList(),
                                35
                        ),
                        ForecastResponse.Day.create(
                                1458842400,
                                ForecastResponse.Temp.create(
                                        80,
                                        60,
                                        85,
                                        62,
                                        78,
                                        62
                                ),
                                Collections.emptyList(),
                                35
                        )
                ));
    }
}
