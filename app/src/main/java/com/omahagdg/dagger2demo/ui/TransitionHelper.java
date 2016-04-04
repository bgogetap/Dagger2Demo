package com.omahagdg.dagger2demo.ui;

import android.content.Context;
import android.os.Build;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;

import com.omahagdg.dagger2demo.R;
import com.omahagdg.dagger2demo.forecast.ForecastFragment;
import com.omahagdg.dagger2demo.main.MainFragment;

public class TransitionHelper {

    private TransitionHelper() {

    }

    public static void setUpDetailsTransition(MainFragment mainFragment, ForecastFragment forecastFragment, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition changeTransform = TransitionInflater.from(context)
                    .inflateTransition(R.transition.image_shared_element_transition);

            mainFragment.setSharedElementReturnTransition(changeTransform);

            forecastFragment.setSharedElementEnterTransition(changeTransform);
        }
    }

    public static String[] getDetailBundle(View sharedImage, View sharedCity, View sharedTemp) {
        String imageTransitionName = "";
        String cityTransitionName = "";
        String tempTransitionName = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageTransitionName = sharedImage.getTransitionName();
            cityTransitionName = sharedCity.getTransitionName();
            tempTransitionName = sharedTemp.getTransitionName();
        }
        return new String[]{imageTransitionName, cityTransitionName, tempTransitionName};
    }
}
