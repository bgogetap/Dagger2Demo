package com.omahagdg.dagger2demo.main;

import com.omahagdg.dagger2demo.R;

public enum WeatherState {

    CLEAR_DAY(R.drawable.ic_clear_day, R.color.colorSunny, "01d"),
    CLEAR_NIGHT(R.drawable.ic_clear_night, R.color.colorDefault, "01n"),
    FEW_CLOUDS_DAY(R.drawable.ic_few_clouds_day, R.color.colorDefault, "02d"),
    FEW_CLOUDS_NIGHT(R.drawable.ic_few_clouds_night, R.color.colorDefault, "02n"),
    CLOUDS(R.drawable.ic_clouds, R.color.colorCloudy, "03d", "03n", "04d", "04n"),
    RAIN(R.drawable.ic_rain, R.color.colorRainy, "09d", "09n", "10d", "10n"),
    THUNDERSTORM(R.drawable.ic_thunderstorm, R.color.colorRainy, "11d", "11n"),
    SNOW(R.drawable.ic_snow, R.color.colorSnow, "13d", "13n"),
    MIST(R.drawable.ic_mist, R.color.colorDefault, "50d", "50n"),
    INVALID(R.drawable.ic_mist, R.color.colorDefault, "");

    public final int iconRes;
    public final int colorRes;
    public final String[] iconCodes;

    WeatherState(int iconRes, int colorRes, String... iconCodes) {
        this.iconRes = iconRes;
        this.colorRes = colorRes;
        this.iconCodes = iconCodes;
    }

    public static WeatherState fromApiCode(String apiCode) {
        for (WeatherState weatherState : values()) {
            for (String code : weatherState.iconCodes) {
                if (code.equals(apiCode)) {
                    return weatherState;
                }
            }
        }
        return INVALID;
    }
}
