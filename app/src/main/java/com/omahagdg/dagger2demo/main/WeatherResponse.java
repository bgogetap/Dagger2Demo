package com.omahagdg.dagger2demo.main;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.omahagdg.dagger2demo.retrofit.AutoGson;

import java.util.List;

@AutoValue @AutoGson
public abstract class WeatherResponse implements Parcelable {

    public static WeatherResponse create(String name,
                                         long id,
                                         long cod,
                                         List<WeatherItem> weather,
                                         Main main,
                                         Wind wind,
                                         Clouds clouds,
                                         Sys sys) {
        return new AutoValue_WeatherResponse(name, id, cod, weather, main, wind, clouds, sys);
    }

    public abstract String name();

    public abstract long id();

    public abstract long cod();

    public abstract List<WeatherItem> weather();

    public abstract Main main();

    public abstract Wind wind();

    public abstract Clouds clouds();

    public abstract Sys sys();

    @AutoValue @AutoGson
    public abstract static class WeatherItem implements Parcelable {
        public static WeatherItem create(long id, String main, String description, String icon) {
            return new AutoValue_WeatherResponse_WeatherItem(id, main, description, icon);
        }

        public abstract long id();

        public abstract String main();

        public abstract String description();

        public abstract String icon();
    }

    @AutoValue @AutoGson
    public abstract static class Main implements Parcelable {
        public static Main create(double temp, double pressure, double humidity, double temp_min, double temp_max) {
            return new AutoValue_WeatherResponse_Main(temp, pressure, humidity, temp_min, temp_max);
        }

        public abstract double temp();

        public abstract double pressure();

        public abstract double humidity();

        public abstract double temp_min();

        public abstract double temp_max();
    }

    @AutoValue @AutoGson
    public abstract static class Wind implements Parcelable {
        public static Wind create(double speed, double deg) {
            return new AutoValue_WeatherResponse_Wind(speed, deg);
        }

        public abstract double speed();

        public abstract double deg();
    }

    @AutoValue @AutoGson
    public abstract static class Clouds implements Parcelable {
        public static Clouds create(double all) {
            return new AutoValue_WeatherResponse_Clouds(all);
        }

        public abstract double all();
    }

    @AutoValue @AutoGson
    public abstract static class Sys implements Parcelable {
        public static Sys create(double message, String country, long sunrise, long sunset) {
            return new AutoValue_WeatherResponse_Sys(message, country, sunrise, sunset);
        }

        public abstract double message();

        public abstract String country();

        public abstract long sunrise();

        public abstract long sunset();
    }
}
