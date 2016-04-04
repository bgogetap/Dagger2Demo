package com.omahagdg.dagger2demo.main;

import android.content.Context;

import java.util.List;

/**
 * Created by bgogetap on 4/4/16.
 */
public interface MainView {

    void setData(List<WeatherResponse> data);

    Context getContext();

    void showPermissionDeniedMessage(boolean requestAgain);

    void toggleFab(boolean show);
}
