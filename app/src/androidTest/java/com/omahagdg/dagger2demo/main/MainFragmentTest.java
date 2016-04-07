package com.omahagdg.dagger2demo.main;

import android.support.test.runner.AndroidJUnit4;

import com.omahagdg.dagger2demo.R;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainFragmentTest extends FragmentTestBase {

    @Test
    public void testCorrectWeatherInformationShowsOnCard() {
        onView(withId(R.id.tv_city_name)).check(matches(withText(CITY_NAME)));
        onView(withId(R.id.tv_temp)).check(matches(withText(stringHelper.getString(R.string.degree_symbol_format, TEMP))));
        onView(withId(R.id.tv_high)).check(matches(withText(stringHelper.getString(R.string.high_temp_format, TEMP_MAX))));
        onView(withId(R.id.tv_low)).check(matches(withText(stringHelper.getString(R.string.low_temp_format, TEMP_MIN))));
    }

    @Override protected boolean returnIdFromSharedPrefs() {
        return false;
    }
}
