package com.omahagdg.dagger2demo.main;

import com.omahagdg.dagger2demo.R;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class MainFragmentLoadFromPrefsTest extends FragmentTestBase {

    @Test
    public void testCityIdFromPrefsIsLoadedIfPresent() {
        onView(withId(R.id.tv_city_name)).check(matches(withText(CITY_NAME_FROM_ID)));
    }

    @Override protected boolean returnIdFromSharedPrefs() {
        return true;
    }
}
